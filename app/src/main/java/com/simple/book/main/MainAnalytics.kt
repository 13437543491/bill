package com.simple.book.main

import com.book.router.IBookAnalyticsService
import io.github.prototypez.appjoint.AppJoint

class MainAnalytics {

    /**
     * 首页TAB点击事件
     */
    fun addHomeTabClick() {
        AppJoint.service(IBookAnalyticsService::class.java).addEventRecord("home_tab_click")
    }

    /**
     * 报表TAB点击事件
     */
    fun addChartTabClick() {
        AppJoint.service(IBookAnalyticsService::class.java).addEventRecord("chart_tab_click")
    }
}