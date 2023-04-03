package com.book.analytics

import android.app.Application
import com.book.base.constant.ApplicationPriority
import com.book.router.IBookAnalyticsService
import com.google.firebase.analytics.FirebaseAnalytics
import io.github.prototypez.appjoint.AppJoint
import io.github.prototypez.appjoint.core.ModuleSpec

@ModuleSpec(priority = ApplicationPriority.MODULE_BOOK_SHELF_PRIORITY)
class AnalyticsApplication : Application() {

    companion object {
        private var mFirebaseAnalytics: FirebaseAnalytics? = null

        fun getFireBaseAnalytics(): FirebaseAnalytics? {
            return mFirebaseAnalytics
        }
    }

    override fun onCreate() {
        super.onCreate()
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        AppJoint.service(IBookAnalyticsService::class.java).addAppStartRecord()
    }
}