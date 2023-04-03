package com.simple.book

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.book.base.utils.LogUtil
import io.github.prototypez.appjoint.core.AppSpec

@AppSpec
class App : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
    }
}