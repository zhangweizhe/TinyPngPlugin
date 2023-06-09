package com.github.zhangweizhe.tinypngplugin.setting

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

/**
 * 设置 model 层
 * mvc 中的 model 层
 */
@State(
    name = "com.github.zhangweizhe.tinypngplugin.setting.TinyPngSettingState",
    storages = [Storage("TinyPngSettingsPlugin.xml")]
)
class TinyPngSettingState: PersistentStateComponent<TinyPngSettingState> {

    var apiKey: String = ""

    var compressMode: String = COMPRESS_MODE_TINY_PNG

    companion object {

        const val COMPRESS_MODE_TINY_PNG = "TinyPng"

        const val COMPRESS_MODE_PNG_QUANT = "PngQuant"

        fun getInstance(): TinyPngSettingState {
            return ApplicationManager.getApplication().getService(TinyPngSettingState::class.java)
        }
    }

    override fun getState(): TinyPngSettingState {
        return this
    }

    override fun loadState(state: TinyPngSettingState) {
        XmlSerializerUtil.copyBean(state, this)
    }
}