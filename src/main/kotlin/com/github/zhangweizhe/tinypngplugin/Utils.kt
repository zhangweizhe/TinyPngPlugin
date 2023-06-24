package com.github.zhangweizhe.tinypngplugin

import com.github.zhangweizhe.tinypngplugin.setting.TinyPngSettingConfigurable
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.options.ShowSettingsUtil
import com.tinify.AccountException

object Utils {

    const val NOTIFICATION_GROUP_ID = "TinyPngPlugin"

    /**
     * 错误通知
     */
    fun notificationFail(th: Throwable, fileName: String) {
        thisLogger().error("Tiny $fileName fail", th)
        val notification = Notification(
            NOTIFICATION_GROUP_ID,
            "Tiny $fileName fail",
            th.message ?: "Unknown exception",
            NotificationType.ERROR
        )
        if (th is AccountException) {
            notification.setContent(th.message ?: "Invalid Api Key")
            // apiKey 认证错误
            notification.addAction(object : AnAction("Set api key") {
                override fun actionPerformed(e: AnActionEvent) {
                    // 跳转到设置
                    ShowSettingsUtil.getInstance().showSettingsDialog(
                        null,
                        TinyPngSettingConfigurable::class.java,
                        null
                    )
                }
            })
        }
        Notifications.Bus.notify(notification)
    }

}