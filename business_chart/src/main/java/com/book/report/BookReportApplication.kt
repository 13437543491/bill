package com.book.report

import android.app.Application
import com.book.base.constant.ApplicationPriority
import io.github.prototypez.appjoint.core.ModuleSpec

@ModuleSpec(priority = ApplicationPriority.MODULE_BOOK_FEATURED_PRIORITY)
class BookReportApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}