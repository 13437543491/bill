package com.book.base.utils

import com.tencent.mmkv.MMKV

object KVUtils {

    private val mKV = MMKV.defaultMMKV()

    fun putInt(name: String, value: Int) {
        mKV.putInt(name, value)
    }

    fun getInt(name: String, default: Int = -1): Int {
        return mKV.decodeInt(name, default)
    }

    fun putString(name: String, value: String) {
        mKV.putString(name, value)
    }

    fun getString(name: String, default: String = ""): String {
        return mKV.getString(name, default)!!
    }

    fun putBoolean(name: String, value: Boolean) {
        mKV.putBoolean(name, value)
    }

    fun getBoolean(name: String, default: Boolean = false): Boolean {
        return mKV.getBoolean(name, default)
    }

}