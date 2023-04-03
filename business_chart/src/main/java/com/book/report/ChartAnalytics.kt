package com.book.report

import android.os.Bundle
import com.book.router.IBookAnalyticsService
import io.github.prototypez.appjoint.AppJoint

class ChartAnalytics {

    fun addChartDateSelectClickRecord(isMonth: Boolean) {
        val bundle = Bundle()
        bundle.putString(
            "type", if (isMonth) {
                "month"
            } else {
                "year"
            }
        )
        AppJoint.service(IBookAnalyticsService::class.java)
            .addEventRecord("chart_date_select_click", bundle)
    }

    fun addChartTopTabClickRecord(isMonth: Boolean) {
        val bundle = Bundle()
        bundle.putString(
            "type", if (isMonth) {
                "month"
            } else {
                "year"
            }
        )
        AppJoint.service(IBookAnalyticsService::class.java)
            .addEventRecord("chart_top_tab_click", bundle)
    }

    fun addChartBillTypeClickRecord(isExpend: Boolean, isMonth: Boolean) {
        val bundle = Bundle()
        bundle.putString(
            "bill_type", if (isExpend) {
                "expend"
            } else {
                "income"
            }
        )
        bundle.putString(
            "date_type", if (isMonth) {
                "month"
            } else {
                "year"
            }
        )
        AppJoint.service(IBookAnalyticsService::class.java)
            .addEventRecord("chart_bill_type_click", bundle)
    }

    fun addChartBillDetailClickRecord(isMonth: Boolean) {
        val bundle = Bundle()
        bundle.putString(
            "type", if (isMonth) {
                "month"
            } else {
                "year"
            }
        )
        AppJoint.service(IBookAnalyticsService::class.java)
            .addEventRecord("chart_bill_detail_click")
    }
}