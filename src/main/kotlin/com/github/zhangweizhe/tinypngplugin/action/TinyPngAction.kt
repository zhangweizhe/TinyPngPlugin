package com.github.zhangweizhe.tinypngplugin.action

import com.github.zhangweizhe.tinypngplugin.AbsCompressTask
import com.github.zhangweizhe.tinypngplugin.PngQuantTask
import com.github.zhangweizhe.tinypngplugin.TinyPngTask
import com.github.zhangweizhe.tinypngplugin.Utils.NOTIFICATION_GROUP_ID
import com.github.zhangweizhe.tinypngplugin.Utils.notificationFail
import com.github.zhangweizhe.tinypngplugin.setting.TinyPngSettingState
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.vfs.VirtualFile
import kotlinx.coroutines.*
import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.SystemIndependent
import java.io.File
import java.text.DecimalFormat

/**
 * 压缩入口 action
 */
open class TinyPngAction: AnAction() {

    private val logger = thisLogger()

    override fun actionPerformed(e: AnActionEvent) {
        val selectedFiles = CommonDataKeys.VIRTUAL_FILE_ARRAY.getData(e.dataContext)
        if (selectedFiles.isNullOrEmpty()) {
            return
        }
        ProgressManager.getInstance().run(object : Task.Backgroundable(e.project, "TinyPng", true) {
            override fun run(indicator: ProgressIndicator) {
                indicator.text = "TinyPng..."
                // 用 runBlocking 阻塞整个 Backgroundable
                runBlocking {
                    val coroutineScope = CoroutineScope(Dispatchers.Default)
                    val async = coroutineScope.async {
                        runBlocking {
                            batchTiny(selectedFiles, e.project?.basePath) { finish, total ->
                                indicator.text = "TinyPng $finish/$total"
                                val ratio = finish.toDouble() / total
                                indicator.fraction = ratio
                            }
                        }
                    }
                    async.await()
                }

            }

        })
    }

    /**
     * @param selectedFiles 选中文件集合
     * @param projectPath 项目路径
     */
    private suspend fun batchTiny(
        selectedFiles: Array<VirtualFile>,
        projectPath: @SystemIndependent @NonNls String?,
        tinyProgress: ((finish: Int, total: Int) -> Unit)
    ) {
        if (projectPath.isNullOrEmpty()) {
            println("doTiny projectPath empty")
            return
        }
        withContext(Dispatchers.Default) {
            val deferredList = ArrayList<Deferred<Unit>>()
            var finishCount = 0
            val total = selectedFiles.size
            // 选中的文件，压缩前总大小
            var totalLengthBeforeTiny = 0L
            // 选中的文件，压缩后总大小
            var totalLengthAfterTiny = 0L
            selectedFiles.forEach { selectFile ->
                // 压缩前大小
                val lengthBeforeTiny = selectFile.length
                val deferred = async {
                    getCompressTask(selectFile, projectPath).doCompress()
                }
                deferred.invokeOnCompletion {th ->
                    // 监听每个任务的完成
                    if (th != null) {
                        // 异常处理
                        notificationFail(th, selectFile.name)
                    } else {
                        totalLengthBeforeTiny += lengthBeforeTiny
                        totalLengthAfterTiny += File(selectFile.path).length()
                    }
                    finishCount++
                    // 通知外面任务进度
                    tinyProgress.invoke(finishCount, total)
                }
                deferredList.add(deferred)
            }
            // 启动所有任务，并等待所有任务完成
            deferredList.awaitAll()
            notifySuccess(totalLengthBeforeTiny, totalLengthAfterTiny)
        }
    }

    private fun getCompressTask(selectFile: VirtualFile, projectPath: @SystemIndependent @NonNls String): AbsCompressTask {
        return when (TinyPngSettingState.getInstance().compressMode) {
            TinyPngSettingState.COMPRESS_MODE_TINY_PNG -> TinyPngTask(selectFile, projectPath)
            else -> PngQuantTask(selectFile, projectPath)
        }
    }

    private fun notifySuccess(totalLengthBeforeTiny: Long, totalLengthAfterTiny: Long) {
        val savedLength = totalLengthBeforeTiny - totalLengthAfterTiny
        val savedRatio = savedLength.toFloat() / totalLengthBeforeTiny * 100
        val df = DecimalFormat("#.##")
        val kbBefore = df.format(totalLengthBeforeTiny.toFloat() / 1024) + "kb"
        val kbAfter = df.format(totalLengthAfterTiny.toFloat() / 1024) + "kb"
        val notification = Notification(
            NOTIFICATION_GROUP_ID,
            "Tiny finish",
            "Save ${df.format(savedRatio)}% (${df.format(savedLength.toFloat() / 1024)}kb, $kbBefore -> $kbAfter)",
            NotificationType.INFORMATION
        )
        Notifications.Bus.notify(notification)
    }




    override fun update(e: AnActionEvent) {
        super.update(e)
        val selectedFiles = CommonDataKeys.VIRTUAL_FILE_ARRAY.getData(e.dataContext)
        if (selectedFiles.isNullOrEmpty()) {
            return
        }
        selectedFiles.forEach { selectFile ->
            if (selectFile.isDirectory || !isImage(selectFile)) {
                // 选中的文件中有不是图片的，不显示压缩菜单
                e.presentation.isEnabledAndVisible = false
                return
            }
        }
    }

    /**
     * 简单地用文件后缀名判断文件[file]是不是图片
     */
    private fun isImage(file: VirtualFile): Boolean {
        return file.extension?.lowercase() in IMG_EXTENSIONS
    }

    companion object {
        // 图片后缀
        private val IMG_EXTENSIONS = setOf("png", "jpg", "jpeg", "webp")
    }
}