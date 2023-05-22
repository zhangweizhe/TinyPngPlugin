package com.github.zhangweizhe.tinypngplugin.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.vfs.VirtualFile
import com.tinify.Tinify
import kotlinx.coroutines.*
import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.SystemIndependent
import java.io.File
import java.util.Random

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
        Tinify.setKey("o2cE734RVZPHS361lATPkLwcwGxGiG9D")
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
            selectedFiles.forEach {
                val deferred = async { tinyOneImage(it, projectPath) }
                deferred.invokeOnCompletion {
                    // todo 异常处理
                    // 监听每个任务的完成
                    finishCount++
                    // 通知外面任务进度
                    tinyProgress.invoke(finishCount, total)
                }
                deferredList.add(deferred)
            }
            // 启动所有任务，并等待所有任务完成
            deferredList.awaitAll()
        }
    }

    /**
     * 压缩一张图片
     * 1、把图片复制到临时文件夹 /build/tiny 中
     * 2、上传压缩临时图片
     * 3、下载压缩后的图片
     * 4、覆盖原图片
     */
    private fun tinyOneImage(selectFile: VirtualFile, projectPath: String) {
//        logger.warn("tinyOneImage ${selectFile.path}")
        // 选中的文件
        val sourceFile = File(selectFile.path)
        // 1、复制到临时目录
        val tmpFile = copyToTmp(sourceFile, projectPath)

        // 2、上传压缩临时图片
        // 3、下载压缩后的图片
        // 4、覆盖原图片
        Tinify.fromFile(tmpFile.absolutePath).toFile(sourceFile.absolutePath)
    }

    /**
     * 复制到临时目录
     * @param sourceFile 源文件
     * @return 复制后的临时文件
     */
    private fun copyToTmp(sourceFile: File, projectPath: String): File {
        // 临时保存的文件名
        val fileName = sourceFile.nameWithoutExtension +
                "_" + System.currentTimeMillis() +
                "." + sourceFile.extension
        val tmpFile = File("$projectPath/build/tiny/$fileName")
        tmpFile.mkdirs()
        sourceFile.copyTo(tmpFile, true)
        return tmpFile
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
        private val IMG_EXTENSIONS = setOf("png", "jpg", "jpeg")
    }
}