package com.github.zhangweizhe.tinypngplugin.compare

class CompareManager private constructor(){

    companion object {
        val instance by lazy {
            CompareManager()
        }
    }

    private val _compareList: MutableList<Pair<String, String>> = ArrayList()

    val compareList: List<Pair<String, String>> = _compareList

    fun addCompare(pathBeforeCompress: String, pathAfterCompress: String) {
        _compareList.add(pathBeforeCompress to pathAfterCompress)
    }

    fun clearCompareList() {
        _compareList.clear()
    }

}