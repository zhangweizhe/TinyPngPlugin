package com.github.zhangweizhe.tinypngplugin

import com.github.zhangweizhe.tinypngplugin.Utils.notificationFail
import com.github.zhangweizhe.tinypngplugin.setting.TinyPngSettingState
import com.intellij.openapi.vfs.VirtualFile
import com.tinify.Tinify
import java.io.File

/**
 * 使用 TinyPng 压缩图片，需要联网和 TinyPng ApiKey
 */
class TinyPngTask(private val selectFile: VirtualFile, private val projectPath: String) :
    AbsCompressTask(selectFile, projectPath) {
    override fun doCompress() {
        tinyOneImage()
    }


    /**
     * 压缩一张图片
     * 1、把图片复制到临时文件夹 /build/tiny 中
     * 2、上传压缩临时图片
     * 3、下载压缩后的图片
     * 4、覆盖原图片
     */
    private fun tinyOneImage() {
        Tinify.setKey(TinyPngSettingState.getInstance().apiKey)
        // 选中的文件
        val sourceFile = File(selectFile.path)
        // 1、复制到临时目录
        val tmpFile = copyToTmp(sourceFile, projectPath)

        kotlin.runCatching {
            // 2、上传压缩临时图片
            // 3、下载压缩后的图片
            // 4、覆盖原图片
            Tinify.fromFile(tmpFile.absolutePath).toFile(sourceFile.absolutePath)
        }.onFailure {
            notificationFail(it, sourceFile.name)
        }
    }
}