package com.github.zhangweizhe.tinypngplugin

import com.github.zhangweizhe.tinypngplugin.action.TinyPngAction
import com.github.zhangweizhe.tinypngplugin.setting.TinyPngSettingConfigurable
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.tinify.AccountException
import java.io.File

abstract class AbsCompressTask(
    selectFile: VirtualFile,
    projectPath: String
) {

    abstract fun doCompress()



    /**
     * 复制到临时目录
     * @param sourceFile 源文件
     * @return 复制后的临时文件
     */
    fun copyToTmp(sourceFile: File, projectPath: String): File {
        // 临时保存的文件名
        val fileName = sourceFile.nameWithoutExtension +
                "_" + System.currentTimeMillis() +
                "." + sourceFile.extension
        val tmpFile = File("$projectPath/build/tiny/$fileName")
        tmpFile.mkdirs()
        sourceFile.copyTo(tmpFile, true)
        return tmpFile
    }

    companion object {

    }
}