/*
 * Copyright 2000-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.idea.scratch

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import org.jetbrains.kotlin.idea.scratch.ui.ScratchTopPanel
import org.jetbrains.kotlin.psi.UserDataProperty

fun getAllEditorsWithoutScratchPanel(project: Project, virtualFile: VirtualFile): List<TextEditor> =
    getAllTextEditors(project, virtualFile).filter { it.scratchTopPanel == null }

fun getAllEditorsWithScratchPanel(project: Project, virtualFile: VirtualFile): List<Pair<TextEditor, ScratchTopPanel>> =
    getAllTextEditors(project, virtualFile).mapNotNull {
        val panel = it.scratchTopPanel
        if (panel != null) it to panel else null
    }

fun getScratchPanels(psiFile: PsiFile): List<ScratchTopPanel> =
    getAllTextEditors(psiFile.project, psiFile.virtualFile).mapNotNull { it.scratchTopPanel }

fun getAllEditorsWithScratchPanel(project: Project): List<Pair<TextEditor, ScratchTopPanel>> =
    FileEditorManager.getInstance(project).allEditors.filterIsInstance<TextEditor>().mapNotNull {
        val panel = it.scratchTopPanel
        if (panel != null) it to panel else null
    }

fun getScratchPanelFromSelectedEditor(project: Project): ScratchTopPanel? =
    FileEditorManager.getInstance(project).selectedEditors.asSequence()
        .filterIsInstance<TextEditor>()
        .mapNotNull { it.scratchTopPanel }
        .firstOrNull()

private fun getAllTextEditors(project: Project, virtualFile: VirtualFile) =
    FileEditorManager.getInstance(project).getAllEditors(virtualFile).filterIsInstance<TextEditor>()

fun TextEditor.addScratchPanel(panel: ScratchTopPanel) {
    scratchTopPanel = panel
    FileEditorManager.getInstance(panel.scratchFile.psiFile.project).addTopComponent(this, panel)
}

fun TextEditor.removeScratchPanel(panel: ScratchTopPanel) {
    scratchTopPanel = null
    FileEditorManager.getInstance(panel.scratchFile.psiFile.project).removeTopComponent(this, panel)
}

private var TextEditor.scratchTopPanel: ScratchTopPanel? by UserDataProperty<TextEditor, ScratchTopPanel>(Key.create("scratch.panel"))
