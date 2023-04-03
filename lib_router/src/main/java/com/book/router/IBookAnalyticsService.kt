package com.book.router

import android.os.Bundle

interface IBookAnalyticsService {

    fun addAppStartRecord()

    fun addEventRecord(event: String, bundle: Bundle? = null)
}