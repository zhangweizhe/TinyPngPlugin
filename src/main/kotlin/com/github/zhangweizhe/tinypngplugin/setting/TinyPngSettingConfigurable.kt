package com.github.zhangweizhe.tinypngplugin.setting

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
        return oldApiKey != editingApiKey
    }

    /**
     * 应用新的配置
     */
    override fun apply() {
        val tinyPngSettingState = TinyPngSettingState.getInstance()
        tinyPngSettingState.apiKey = tinyPngSettingComponent?.getApiKeyText() ?: ""
    }

    override fun reset() {
        val tinyPngSettingState = TinyPngSettingState.getInstance()
        tinyPngSettingComponent?.setApiKeyText(tinyPngSettingState.apiKey)
    }

    /**
     * 设置弹窗关闭了，释放 UI 资源
     */
    override fun disposeUIResources() {
        tinyPngSettingComponent = null
    }

}