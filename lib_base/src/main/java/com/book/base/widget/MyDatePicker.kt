package com.book.base.widget

import android.app.Activity
import android.view.View
import com.book.base.R
import com.github.gzuliyujiang.wheelpicker.DatePicker

class MyDatePicker : DatePicker {

    constructor(activity: Activity) : super(activity)
    constructor(activity: Activity, themeResId: Int) : super(activity, themeResId)

    override fun createBodyView(): View {
        wheelLayout = MyDateWheelLayout(activity)
        return wheelLayout
    }

    override fun initView() {
        super.initView()
        okView.text = activity.getString(R.string.enter)
        cancelView.text = activity.getString(R.string.cancel)
    }
}