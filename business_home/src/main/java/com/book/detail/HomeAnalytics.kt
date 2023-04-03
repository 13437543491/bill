package com.book.detail

import android.os.Bundle
import com.book.router.IBookAnalyticsService
import io.github.prototypez.appjoint.AppJoint

class HomeAnalytics {

    /**
     * 上报添加账单按钮点击事件
     */
    fun addBillAddClickRecord() {
        AppJoint.service(IBookAnalyticsService::class.java).addEventRecord("bill_add_click")
    }

    /**
     * 首页点击账单事件
     */
    fun addHomeBillDetailClick() {
        AppJoint.service(IBookAnalyticsService::class.java).addEventRecord("home_bill_detail_click")
    }

    /**
     * 首页选择日期点击
     */
    fun addHomeDateSelectClick() {
        AppJoint.service(IBookAnalyticsService::class.java).addEventRecord("home_date_select_click")
    }

    /**
     * 分类界面设置按钮点击
     */
    fun addCategorySettingClick(isExpend: Boolean) {
        val bundle = Bundle()
        bundle.putString(
            "type", if (isExpend) {
                "expend"
            } else {
                "income"
            }
        )
        AppJoint.service(IBookAnalyticsService::class.java)
            .addEventRecord("category_setting_click", bundle)
    }

    /**
     * 分类界面点击添加按钮
     */
    fun addCategoryAddClick(isExpend: Boolean) {
        val bundle = Bundle()
        bundle.putString(
            "type", if (isExpend) {
                "expend"
            } else {
                "income"
            }
        )
        AppJoint.service(IBookAnalyticsService::class.java)
            .addEventRecord("category_add_click", bundle)
    }

    /**
     * 分类界面点击确认添加按钮
     */
    fun addCategoryAddEnterClick(name: String) {
        val bundle = Bundle()
        bundle.putString("name", name)
        AppJoint.service(IBookAnalyticsService::class.java)
            .addEventRecord("category_add_enter_click", bundle)
    }

    /**
     * 分类界面点击删除按钮
     */
    fun addCategoryDelClick(name: String, isExpend: Boolean) {
        val bundle = Bundle()
        bundle.putString("name", name)
        bundle.putString(
            "type", if (isExpend) {
                "expend"
            } else {
                "income"
            }
        )
        AppJoint.service(IBookAnalyticsService::class.java)
            .addEventRecord("category_del_click", bundle)
    }

    /**
     * 分类界面确认删除
     */
    fun addCategoryDelEnterClick(name: String, isExpend: Boolean) {
        val bundle = Bundle()
        bundle.putString("name", name)
        bundle.putString(
            "type", if (isExpend) {
                "expend"
            } else {
                "income"
            }
        )
        AppJoint.service(IBookAnalyticsService::class.java)
            .addEventRecord("category_del_enter_click", bundle)
    }

    fun addBillRemarkShowRecord(isModify: Boolean) {
        val bundle = Bundle()
        bundle.putString(
            "type", if (isModify) {
                "modify"
            } else {
                "none"
            }
        )
        AppJoint.service(IBookAnalyticsService::class.java)
            .addEventRecord("bill_remark_pager_show", bundle)
    }

    fun addBillRemarkEnterRecord(isModify: Boolean) {
        val bundle = Bundle()
        bundle.putString(
            "type", if (isModify) {
                "modify"
            } else {
                "none"
            }
        )
        AppJoint.service(IBookAnalyticsService::class.java)
            .addEventRecord("bill_remark_enter", bundle)
    }

    fun addBillSubmitRecord(isExpend: Boolean, isModify: Boolean) {
        val bundle = Bundle()
        bundle.putString(
            "type", if (isExpend) {
                "expend"
            } else {
                "income"
            }
        )
        bundle.putString(
            "add_type", if (isModify) {
                "modify"
            } else {
                "none"
            }
        )
        AppJoint.service(IBookAnalyticsService::class.java)
            .addEventRecord("bill_submit_enter", bundle)
    }
}