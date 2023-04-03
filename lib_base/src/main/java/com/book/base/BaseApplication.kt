package com.book.base

import android.app.Application
import com.book.base.constant.ApplicationPriority
import com.book.base.utils.LogUtil
import com.tencent.mmkv.MMKV
import io.github.prototypez.appjoint.AppJoint
import io.github.prototypez.appjoint.core.ModuleSpec

@ModuleSpec(priority = ApplicationPriority.LIB_BASE_PRIORITY)
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
    }
}