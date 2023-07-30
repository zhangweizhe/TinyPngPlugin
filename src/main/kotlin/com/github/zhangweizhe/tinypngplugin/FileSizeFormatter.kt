package com.github.zhangweizhe.tinypngplugin

import java.text.DecimalFormat

object FileSizeFormatter {
    private val decimalFormat = DecimalFormat("#.##")

    fun formatBytes(bytes: Long): String {
        return when {
            bytes < 1024 -> "$bytes B"
            bytes < 1024 * 1024 -> formatKilobytes(bytes.toDouble() / 1024)
            bytes < 1024 * 1024 * 1024 -> formatMegabytes(bytes.toDouble() / (1024 * 1024))
            else -> formatGigabytes(bytes.toDouble() / (1024 * 1024 * 1024))
        }
    }

    private fun formatKilobytes(kilobytes: Double): String {
        return "${decimalFormat.format(kilobytes)} KB"
    }

    private fun formatMegabytes(megabytes: Double): String {
        return "${decimalFormat.format(megabytes)} MB"
    }

    private fun formatGigabytes(gigabytes: Double): String {
        return "${decimalFormat.format(gigabytes)} GB"
    }
}