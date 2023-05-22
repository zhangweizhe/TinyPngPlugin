package com.github.zhangweizhe.tinypngplugin.setting

import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import java.awt.Desktop
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.net.URI
import javax.swing.JPanel

/**
 * 设置的 view 层
 * mvc 中的 view
 */
class TinyPngSettingComponent {

    companion object {
        private const val GET_API_KEY_URL = "https://tinypng.com/developers"
        private const val GET_API_KEY_LINK = "<html><a href='$GET_API_KEY_URL'>TinyPng</a></html>"
    }

    val apiKeyTextFiled = JBTextField()

    private val getApiKeyLabel = JBLabel(GET_API_KEY_LINK).also {
        it.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                if (e?.button == MouseEvent.BUTTON1) {
                    // 默认浏览器打开
                    val desktop: Desktop = Desktop.getDesktop()
                    desktop.browse(URI(GET_API_KEY_URL))
                }
            }
        })
    }

    val mainPanel: JPanel by lazy {
        FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Tiny png api key: "), apiKeyTextFiled, 1, true)
            .addLabeledComponent(JBLabel("Get api key: "), getApiKeyLabel, 10, true)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    /**
     * 返回 apiKey 输入框中的文本
     */
    fun getApiKeyText(): String {
        return apiKeyTextFiled.text ?: ""
    }

    fun setApiKeyText(text: String) {
        apiKeyTextFiled.text = text
    }
}