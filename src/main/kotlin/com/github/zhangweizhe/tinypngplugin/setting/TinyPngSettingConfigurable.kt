package com.github.zhangweizhe.tinypngplugin.setting

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.options.Configurable
import javax.swing.JComponent


/**
 * 设置的 controller 层
 * mvc 的 controller
 */
class TinyPngSettingConfigurable: Configurable {

    private var tinyPngSettingComponent: TinyPngSettingComponent? = TinyPngSettingComponent()

    override fun getDisplayName(): String {
        return "TinyPngPlugin"
    }

    override fun createComponent(): JComponent? {
        return tinyPngSettingComponent?.mainPanel
    }

    override fun getPreferredFocusedComponent(): JComponent? {
        return tinyPngSettingComponent?.apiKeyTextFiled
    }

    /**
     * 返回配置是否修改了
     */
    override fun isModified(): Boolean {
        // 旧的 api key
        val oldApiKey = TinyPngSettingState.getInstance().apiKey
        // 输入中的 api key
        val editingApiKey = tinyPngSettingComponent?.getApiKeyText()
        val apiKeyChanged = oldApiKey != editingApiKey
        if (apiKeyChanged) {
            return true
        }

        // 旧的压缩方式
        val oldCompressMode = TinyPngSettingState.getInstance().compressMode
        // 新的压缩方式
        val newCompressMode = tinyPngSettingComponent?.compressModeGroup?.selection?.actionCommand

        return oldCompressMode != newCompressMode
    }

    /**
     * 应用新的配置
     */
    override fun apply() {
        val tinyPngSettingState = TinyPngSettingState.getInstance()
        tinyPngSettingComponent?.also {
            tinyPngSettingState.apiKey = it.getApiKeyText() ?: ""
            tinyPngSettingState.compressMode = it.compressModeGroup.selection.actionCommand
        } ?: thisLogger().warn("apply fail, tinyPngSettingComponent is null")
    }

    override fun reset() {
        val tinyPngSettingState = TinyPngSettingState.getInstance()
        // 重置 apiKey
        tinyPngSettingComponent?.setApiKeyText(tinyPngSettingState.apiKey)
        // 重置压缩方式
        val compressMode = tinyPngSettingState.compressMode
        val elements = tinyPngSettingComponent?.compressModeGroup?.elements
        if (elements != null) {
            for (i in elements) {
                tinyPngSettingComponent?.compressModeGroup
                    ?.setSelected(i.model, i.actionCommand == compressMode)
            }
        }
    }

    /**
     * 设置弹窗关闭了，释放 UI 资源
     */
    override fun disposeUIResources() {
        tinyPngSettingComponent = null
    }

}