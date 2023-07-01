package com.github.zhangweizhe.tinypngplugin

import com.github.zhangweizhe.tinypngplugin.Utils.notificationFail
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.vfs.VirtualFile
import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.lang.RuntimeException
import java.lang.StringBuilder
import java.nio.file.Files
import java.nio.file.StandardCopyOption

/**
 * 使用 PngQuant 压缩图片，无需联网
 */
class PngQuantTask(private val selectFile: VirtualFile, private val projectPath: String) :
    AbsCompressTask(selectFile, projectPath) {
    override fun doCompress() {
        quantOneImage()
    }


    private fun quantOneImage() {
//        logger.warn("tinyOneImage ${selectFile.path}")
        // 选中的文件
        val sourceFile = File(selectFile.path)
        // 1、复制到临时目录
        val tmpFile = copyToTmp(sourceFile, projectPath)

        val binaryFileName = if (SystemInfo.isMac) {
            "pngquant"
        } else if (SystemInfo.isWindows) {
            "pngquant.exe"
        } else {
            ""
        }

        val inputStream = javaClass.classLoader.getResourceAsStream("pngquant/$binaryFileName")
            ?: throw FileNotFoundException("未找到资源文件")

        // 将二进制文件复制到临时目录
        val isTmpFile = Files.createTempFile("binary", System.currentTimeMillis().toString()).toFile()
        isTmpFile.deleteOnExit()
        Files.copy(inputStream, isTmpFile.toPath(), StandardCopyOption.REPLACE_EXISTING)

        // 设置临时文件为可执行
        isTmpFile.setExecutable(true)

        kotlin.runCatching {
            val process = ProcessBuilder(isTmpFile.absolutePath,
                tmpFile.absolutePath,
                "-f",
                "--output",
                sourceFile.absolutePath
            )
                .redirectOutput(ProcessBuilder.Redirect.INHERIT) // 将输出重定向到当前进程的输出
                .start()
            val exitCode = process.waitFor()
            if (exitCode != 0) {
                // 从错误输出流中逐行读取错误信息
                val errorStr = StringBuilder("execute pngquant fail, exitCode=$exitCode\n")
                val errorStream = BufferedReader(InputStreamReader(process.errorStream))
                errorStream.useLines { lines ->
                    lines.forEach { line ->
                        errorStr.append(line).append("\n")
                    }
                }
                notificationFail(RuntimeException(errorStr.toString()), sourceFile.name)
            }
            exitCode
        }.onFailure {
            notificationFail(it, sourceFile.name)
        }
    }
}