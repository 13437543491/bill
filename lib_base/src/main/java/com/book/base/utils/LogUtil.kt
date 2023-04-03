package com.book.base.utils

import android.util.Log

object LogUtil {
    fun logD(tag: String, msg: String) {
        Log.d(tag, msg)
    }

    fun logE(tag: String, msg: String) {
        Log.e(tag, msg)
    }
}