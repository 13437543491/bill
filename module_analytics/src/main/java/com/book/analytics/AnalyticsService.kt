package com.book.analytics

import android.os.Bundle
import com.book.router.IBookAnalyticsService
import com.google.firebase.analytics.FirebaseAnalytics
import io.github.prototypez.appjoint.core.ServiceProvider

@ServiceProvider
class AnalyticsService : IBookAnalyticsService {

    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    init {
        mFirebaseAnalytics = AnalyticsApplication.getFireBaseAnalytics()
    }

    override fun addAppStartRecord() {
        mFirebaseAnalytics?.logEvent(FirebaseAnalytics.Event.APP_OPEN, null)
    }

    override fun addEventRecord(event: String, bundle: Bundle?) {
        mFirebaseAnalytics?.logEvent(event, bundle)
    }

}