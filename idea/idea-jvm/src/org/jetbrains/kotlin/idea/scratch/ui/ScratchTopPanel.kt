/*
 * Copyright 2010-2017 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.idea.scratch.ui


import com.intellij.application.options.ModulesComboBox
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.ui.components.panels.HorizontalLayout
import org.jetbrains.annotations.TestOnly
import org.jetbrains.kotlin.idea.scratch.ScratchFile
import org.jetbrains.kotlin.idea.scratch.ScratchFileLanguageProvider
import org.jetbrains.kotlin.idea.scratch.actions.ClearScratchAction
import org.jetbrains.kotlin.idea.scratch.actions.RunScratchAction
import org.jetbrains.kotlin.idea.scratch.getAllEditorsWithScratchPanel
import org.jetbrains.kotlin.idea.scratch.getScratchPanels
import javax.swing.*

val ScratchFile.scratchTopPanel: ScratchTopPanel?
    get() = getScratchPanels(psiFile).firstOrNull { it.scratchFile == this@scratchTopPanel }

class ScratchTopPanel private constructor(val scratchFile: ScratchFile) : JPanel(HorizontalLayout(5)) {
    companion object {
        fun createPanel(project: Project, virtualFile: VirtualFile): ScratchTopPanel? {
            val psiFile = PsiManager.getInstance(project).findFile(virtualFile) ?: return null
            val scratchFile = ScratchFileLanguageProvider.createFile(psiFile) ?: return null
            return ScratchTopPanel(scratchFile)
        }
    }

    val progressBar: JProgressBar

    private val actionsToolBar: ActionToolbar
    private val moduleChooser: ModulesComboBox
    private val isReplCheckbox: JCheckBox
    private val isMakeBeforeRunCheckbox: JCheckBox
    private val isInteractiveModeCheckbox: JCheckBox

    private var isCompilerRunning: Boolean = false

    init {
        actionsToolBar = createActionsToolbar()
        add(actionsToolBar.component)

        isReplCheckbox = JCheckBox("Use REPL", false).customize()
        add(isReplCheckbox)

        add(JSeparator(SwingConstants.VERTICAL))

        isMakeBeforeRunCheckbox = JCheckBox("Make before Run", false).customize()
        add(isMakeBeforeRunCheckbox)

        add(JSeparator(SwingConstants.VERTICAL))

        isInteractiveModeCheckbox = JCheckBox("Interactive Mode", false).customize()
        add(isInteractiveModeCheckbox)

        add(JSeparator(SwingConstants.VERTICAL))

        moduleChooser = createModuleChooser(scratchFile.psiFile.project)
        add(JLabel("Use classpath of module"))
        add(moduleChooser)

        progressBar = JProgressBar()
        progressBar.isVisible = false
        add(progressBar)
    }

    fun getModule(): Module? = moduleChooser.selectedModule

    fun setModule(module: Module) {
        moduleChooser.selectedModule = module
    }

    fun addModuleListener(f: (Module) -> Unit) {
        moduleChooser.addActionListener {
            moduleChooser.selectedModule?.let { f(it) }
        }
    }

    fun isRepl() = isReplCheckbox.isSelected
    fun isMakeBeforeRun() = isMakeBeforeRunCheckbox.isSelected
    fun isInteractiveMode() = isInteractiveModeCheckbox.isSelected

    fun isCompilerRunning() = isCompilerRunning

    @TestOnly
    fun setReplMode(isSelected: Boolean) {
        isReplCheckbox.isSelected = isSelected
    }

    fun startCompilation() {
        isCompilerRunning = true
        actionsToolBar.updateActionsImmediately()
    }

    fun stopCompilation() {
        isCompilerRunning = false
        actionsToolBar.updateActionsImmediately()
    }

    private fun JCheckBox.customize(): JCheckBox {
        verticalTextPosition = SwingConstants.BOTTOM
        horizontalTextPosition = SwingConstants.LEADING
        return this
    }

    private fun createActionsToolbar(): ActionToolbar {
        val toolbarGroup = DefaultActionGroup().apply {
            val runAction = RunScratchAction()
            getAllEditorsWithScratchPanel(scratchFile.psiFile.project, scratchFile.psiFile.virtualFile).forEach { (editor, _) ->
                runAction.registerCustomShortcutSet(CustomShortcutSet.fromString(RunScratchAction.shortcut), editor.component)
            }

            add(runAction)
            addSeparator()
            add(ClearScratchAction())
        }

        return ActionManager.getInstance().createActionToolbar(ActionPlaces.EDITOR_TOOLBAR, toolbarGroup, true)
    }

    private fun createModuleChooser(project: Project): ModulesComboBox {
        return ModulesComboBox().apply {
            fillModules(project)
            selectedIndex = 0
        }
    }
}
