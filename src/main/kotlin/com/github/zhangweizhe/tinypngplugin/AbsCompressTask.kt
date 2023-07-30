package com.github.zhangweizhe.tinypngplugin


import com.github.zhangweizhe.tinypngplugin.compare.CompareManager
import com.intellij.openapi.vfs.VirtualFile
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

    fun saveCompare(pathBeforeCompress: String, pathAfterCompress: String) {
        CompareManager.instance.addCompare(pathBeforeCompress, pathAfterCompress)
    }

    companion object {

    }
}