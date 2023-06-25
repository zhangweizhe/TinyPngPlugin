package com.github.zhangweizhe.tinypngplugin.setting

import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.ui.components.panels.VerticalLayout
import com.intellij.util.ui.FormBuilder
import java.awt.ComponentOrientation
import java.awt.Desktop
import java.awt.LayoutManager
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.net.URI
import javax.swing.ButtonGroup
import javax.swing.ButtonModel
import javax.swing.JPanel
import javax.swing.JRadioButton

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

    /**
     * TinyPng 的单选框
     */
    private val compressModeTinyPng = JRadioButton("TinyPng (Network and api key required)")
        .also {
            it.model.actionCommand = TinyPngSettingState.COMPRESS_MODE_TINY_PNG
        }

    /**
     * PngQuant 的单选框
     */
    private val compressModePngQuant = JRadioButton("PngQuant (No network or api key required)")
        .also {
            it.model.actionCommand = TinyPngSettingState.COMPRESS_MODE_PNG_QUANT
        }

    /**
     * 单选框要放在同一个 ButtonGroup 中，才有单选的效果
     */
    val compressModeGroup = ButtonGroup()
        .also {
            it.add(compressModeTinyPng)
            it.add(compressModePngQuant)
        }

    /**
     * 承载两个单选框的容器
     */
    private val compressModePanel = JPanel(VerticalLayout(1))
        .also {
            it.componentOrientation = ComponentOrientation.LEFT_TO_RIGHT
            it.add(compressModeTinyPng)
            it.add(compressModePngQuant)
        }

    val mainPanel: JPanel by lazy {
        FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Compress Mode"), compressModePanel, 1, true)
            .addLabeledComponent("", compressModeTinyPng, 1, false)
            .addLabeledComponent("", compressModePngQuant, 1, false)
            .addLabeledComponent(JBLabel("Tiny png api key: "), apiKeyTextFiled, 10, true)
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