package com.book.base.utils

import android.os.Handler
import android.os.Looper

object HandlerUtil {

    private val mHandler: Handler = Handler(Looper.getMainLooper())

    fun post(runnable: Runnable) {
        mHandler.post(runnable)
    }

    fun postDelayed(runnable: Runnable, delayMillis: Long) {
        mHandler.postDelayed(runnable, delayMillis)
    }

    fun remove(runnable: Runnable) {
        mHandler.removeCallbacks(runnable)
    }

    fun removeAll() {
        mHandler.removeCallbacksAndMessages(null)
    }
}