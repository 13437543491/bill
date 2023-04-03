package com.book.base.utils

import android.content.Context
import android.view.WindowManager

object DisplayUtil {
    fun dp2px(context: Context, dp: Float): Int {
        val scale: Float = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    fun px2dp(context: Context, px: Float): Int {
        val scale: Float = context.resources.displayMetrics.density
        return (px / scale + 0.5f).toInt()
    }

    fun getScreenWidth(context: Context): Int {
        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).apply {
            return defaultDisplay.width
        }
    }

    fun getScreenHeight(context: Context): Int {
        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).apply {
            return defaultDisplay.height
        }
    }
}