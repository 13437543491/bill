package com.book.base.utils

import android.os.Build

import android.text.TextUtils

import android.view.WindowManager

import android.app.Activity
import android.content.Context

import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception


object SystemUtils {
    /**
     * 是否有导航栏
     */
    private var hasNavigation: Boolean = false

    /**
     * 是否有导航栏
     */
    fun hasNavigationBar(activity: Activity): Boolean {
        if (!hasNavigation) {
            val windowHeight = getScreenHeight(activity)
            val dm = DisplayMetrics()
            activity.windowManager.defaultDisplay.getRealMetrics(dm)
            val screenHeight = dm.heightPixels
            hasNavigation = screenHeight - windowHeight > 0
        }
        return hasNavigation
    }

    fun getScreenWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    fun getScreenHeight(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }

    /**
     * 全屏显示的初始化，在setContentView（）方法前调用
     *
     * @param decorView getWindow().getDecorView()，不同view也可以
     */
    fun systemUiInit(activity: Activity, decorView: View) {
        if (!hasNavigationBar(activity)) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            return
        }
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }

    private fun hideStatusBar(activity: Activity) {
        val attrs = activity.window.attributes
        attrs.flags = attrs.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
        activity.window.attributes = attrs
    }

    private fun showStatusBar(activity: Activity) {
        val attrs = activity.window.attributes
        attrs.flags = attrs.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
        activity.window.attributes = attrs
    }


    /**
     * 隐藏导航栏和状态栏
     *
     * @param activity  上下文
     * @param decorView getWindow().getDecorView()，不同view也可以
     */
    fun systemUiHide(activity: Activity, decorView: View) {
        if (!hasNavigationBar(activity)) {
            hideStatusBar(activity)
            return
        }
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    /**
     * 显示导航栏和状态栏
     *
     * @param activity  上下文
     * @param decorView getWindow().getDecorView()，不同view也可以
     */
    fun systemUiShow(activity: Activity, decorView: View) {
        if (!hasNavigationBar(activity)) {
            showStatusBar(activity)
            return
        }
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
    }

    /**
     * 获取状态栏高度
     *
     * @param cxt 上下文
     * @return 状态栏高度，单位PX
     */
    fun getStatusBarHeight(context: Context): Int {
        val resources = context.resources
        val resourceId: Int = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }

    fun setStatusDark(activity: Activity, darkmode: Boolean) {
        if (isFlymeV4OrAbove()) {
            MeiZuStatusUtils.setStatusBarDarkIcon(activity, darkmode)
            return
        }
        if (isMIUIV6OrAbove()) {
            setStatusTextBlackMi(activity, darkmode)
            return
        }
        setStatusTextBlackAndroid(activity, darkmode)
    }

    private fun setStatusTextBlackMi(activity: Activity, isDarkMode: Boolean) {
        try {
            val darkModeFlag = Class.forName("android.view.MiuiWindowManager\$LayoutParams").apply {
                val field = getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                field.getInt(this)
            }

            activity.window::class.java.getMethod("setExtraFlags", Int::class.java, Int::class.java)
                .apply {
                    invoke(activity.window, if (isDarkMode) darkModeFlag else 0, darkModeFlag)
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        setStatusTextBlackAndroid(activity, isDarkMode)
    }


    private fun setStatusTextBlackAndroid(activity: Activity, isDarkMode: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val window: Window = activity.window
            if (isDarkMode) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                val flag: Int = window.decorView
                    .systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                window.decorView.systemUiVisibility = flag
            }
        }
    }


    private fun isFlymeV4OrAbove(): Boolean {
        val displayId = Build.DISPLAY
        if (!TextUtils.isEmpty(displayId) && displayId.contains("Flyme")) {
            val displayIdArray = displayId.split(" ").toTypedArray()
            for (temp in displayIdArray) {
                //版本号4以上，形如4.x.
                if ("^[4-9]\\.(\\d+\\.)+\\S*".toRegex().matches(temp)) {
                    return true
                }
            }
        }
        return false
    }

    //MIUI V6对应的versionCode是4
    //MIUI V7对应的versionCode是5
    private fun isMIUIV6OrAbove(): Boolean {
        val miuiVersionCodeStr = getSystemProperty("ro.miui.ui.version.code")
        if (!TextUtils.isEmpty(miuiVersionCodeStr)) {
            try {
                val miuiVersionCode = miuiVersionCodeStr!!.toInt()
                if (miuiVersionCode >= 4) {
                    return true
                }
            } catch (e: Exception) {
            }
        }
        return false
    }

    private fun getSystemProperty(propName: String): String? {
        val line: String
        var input: BufferedReader? = null
        try {
            val p = Runtime.getRuntime().exec("getprop $propName")
            input = BufferedReader(InputStreamReader(p.inputStream), 1024)
            line = input.readLine()
            input.close()
        } catch (ex: IOException) {
            return null
        } finally {
            if (input != null) {
                try {
                    input.close()
                } catch (e: IOException) {
                }
            }
        }
        return line
    }
}