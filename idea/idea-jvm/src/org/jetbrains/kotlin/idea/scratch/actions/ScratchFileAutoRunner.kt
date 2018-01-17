/*
 * Copyright 2000-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.idea.scratch.actions

import com.intellij.openapi.components.AbstractProjectComponent
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.Alarm
import org.jetbrains.kotlin.idea.scratch.getScratchPanels
import org.jetbrains.kotlin.idea.scratch.ui.ScratchTopPanel

class ScratchFileAutoRunner(project: Project) : AbstractProjectComponent(project) {
    companion object {
        fun getInstance(project: Project): ScratchFileAutoRunner = project.getComponent(ScratchFileAutoRunner::class.java)
    }

    private val myAlarm = Alarm(Alarm.ThreadToUse.SWING_THREAD, project)

    fun addListener(document: Document) {
        val psiFile = PsiDocumentManager.getInstance(myProject).getPsiFile(document) ?: return

        getScratchPanels(psiFile).forEach { panel ->
            document.addDocumentListener(MyDocumentAdapter(panel))
        }
    }

    fun removeListener(document: Document) {
        // todo doc.removeDocumentListener()
    }

    inner class MyDocumentAdapter(private val scratchTopPanel: ScratchTopPanel) : DocumentListener {

        override fun documentChanged(event: DocumentEvent) {
            if (myProject.isDisposed) return
            if (!scratchTopPanel.isInteractiveMode()) return
            if (scratchTopPanel.isCompilerRunning()) return

            myAlarm.cancelAllRequests()

            myAlarm.addRequest(
                {
                    val psiFile = scratchTopPanel.scratchFile.psiFile
                    if (psiFile.isValid && !PsiTreeUtil.hasErrorElements(psiFile)) {
                        RunScratchAction.doAction(scratchTopPanel)
                    }
                }, 1400, true
            )
        }
    }
}