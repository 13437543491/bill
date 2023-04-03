package com.book.base.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.github.gzuliyujiang.wheelpicker.annotation.DateMode
import com.github.gzuliyujiang.wheelpicker.widget.DateWheelLayout

class MyDateWheelLayout : DateWheelLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    fun setDateMode(dateMode: DateShowMode) {
        when (dateMode) {
            DateShowMode.NONE -> {
                setDateMode(DateMode.NONE)
            }
            DateShowMode.YEAR_MONTH_DAY -> {
                setDateMode(DateMode.YEAR_MONTH_DAY)
            }
            DateShowMode.YEAR_MONTH -> {
                setDateMode(DateMode.YEAR_MONTH)
            }
            DateShowMode.MONTH_DAY -> {
                setDateMode(DateMode.MONTH_DAY)
            }
            DateShowMode.YEAR -> {
                setDateMode(DateShowMode.YEAR.ordinal)
            }
        }
    }

    override fun setDateMode(dateMode: Int) {
        super.setDateMode(dateMode)
        if (dateMode == DateShowMode.YEAR.ordinal) {
            monthWheelView.visibility = View.GONE
            monthLabelView.visibility = View.GONE
            dayWheelView.visibility = View.GONE
            dayLabelView.visibility = View.GONE
        }
    }

    enum class DateShowMode {
        /**
         * 不显示
         */
        NONE,

        /**
         * 年月日
         */
        YEAR_MONTH_DAY,

        /**
         * 年月
         */
        YEAR_MONTH,

        /**
         * 月日
         */
        MONTH_DAY,

        /**
         * 年
         */
        YEAR,
    }
}