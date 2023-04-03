package com.book.detail

import android.app.Application
import com.book.base.constant.ApplicationPriority
import io.github.prototypez.appjoint.core.ModuleSpec

@ModuleSpec(priority = ApplicationPriority.MODULE_BOOK_SHELF_PRIORITY)
class BookDetailApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}