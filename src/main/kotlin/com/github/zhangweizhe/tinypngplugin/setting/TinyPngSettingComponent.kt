package com.github.zhangweizhe.tinypngplugin.setting

import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JPanel

/**
 * 设置的 view 层
 * mvc 中的 view
 */
class TinyPngSettingComponent {

    val apiKeyTextFiled = JBTextField()

    val mainPanel: JPanel by lazy {
        FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Tiny png api key: "), apiKeyTextFiled, 1, true)
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