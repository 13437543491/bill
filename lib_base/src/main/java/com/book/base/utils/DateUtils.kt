package com.book.base.utils

import android.app.Activity
import com.book.base.R
import com.book.base.widget.MyDatePicker
import com.book.base.widget.MyDateWheelLayout
import com.github.gzuliyujiang.wheelpicker.DatePicker
import com.github.gzuliyujiang.wheelpicker.annotation.DateMode
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity
import com.github.gzuliyujiang.wheelpicker.impl.SimpleDateFormatter
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    /**
     * 获取某月有多少天
     * @return 该月的天数
     */
    fun getDayCount(year: Int, month: Int): Int {
        Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1)
            return getActualMaximum(Calendar.DAY_OF_MONTH)
        }
    }

    fun getCurYear(): Int {
        return Calendar.getInstance().get(Calendar.YEAR)
    }

    fun getCurMonth(): Int {
        return Calendar.getInstance().get(Calendar.MONTH) + 1
    }

    fun getCurDay(): Int {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    }

    fun showDatePicker(
        activity: Activity,
        dateShowMode: MyDateWheelLayout.DateShowMode,
        isToday: Boolean = true,
        curYear: Int = 0,
        curMonth: Int = 0,
        curDay: Int = 0,
        lastYear: Int = getCurYear(),
        lastMonth: Int = getCurMonth(),
        lastDay: Int = getCurDay(),
        callback: (year: Int, month: Int, day: Int) -> Unit
    ) {
        val picker = MyDatePicker(activity)
        (picker.wheelLayout as MyDateWheelLayout).apply {
            setDateMode(dateShowMode)

            activity.resources.apply {
                setDateLabel(
                    getString(R.string.year_label),
                    getString(R.string.month_label),
                    getString(R.string.day_label)
                )
            }

            setRange(
                DateEntity.target(1990, 1, 1),
                DateEntity.target(
                    lastYear,
                    lastMonth,
                    lastDay
                ),
                if (isToday) {
                    DateEntity.today()
                } else {
                    DateEntity.target(curYear, curMonth, curDay)
                }
            )

            setCurtainEnabled(false)
        }
        picker.setOnDatePickedListener { year, month, day ->
            callback.invoke(year, month, day)
        }
        picker.show()
    }

    fun formatterYear(year: Int): String {
        if (year < 1000) {
            return (year + 1000).toString()
        }
        return year.toString()
    }

    fun formatterMonth(month: Int): String {
        return if (month < 10) {
            "0$month"
        } else {
            month.toString()
        }
    }

    fun formatterDay(day: Int): String {
        return if (day < 10) {
            "0$day"
        } else {
            day.toString()
        }
    }

    fun formatterDate(
        year: Int,
        month: Int,
        day: Int,
        callback: (year: String, month: String, day: String) -> Unit
    ) {
        SimpleDateFormatter().apply {
            callback.invoke(formatYear(year), formatMonth(month), formatDay(day))
        }
    }

    fun dateToStamp(date: String, pattern: DatePattern): Long {
        val simpleDateFormat = SimpleDateFormat(pattern.value)
        return try {
            simpleDateFormat.parse(date).time
        } catch (e: ParseException) {
            e.printStackTrace()
            0
        }
    }

    fun stampToDate(time: Long, pattern: DatePattern): String {
        val simpleDateFormat = SimpleDateFormat(pattern.value)
        return simpleDateFormat.format(time)
    }

    fun getFirstDayOfMonth(year: Int, month: Int): String {
        Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1)
            set(Calendar.DAY_OF_MONTH, 1)
            return stampToDate(timeInMillis, DatePattern.ONLY_DAY)
        }
    }

    fun getLastDayOfMonth(year: Int, month: Int): String {
        Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, 0)
            return stampToDate(timeInMillis, DatePattern.ONLY_DAY)
        }
    }

    enum class DatePattern(val value: String) {
        /**
         * 格式："yyyy-MM-dd HH:mm:ss"
         */
        ALL_TIME("yyyy-MM-dd HH:mm:ss"),

        /**
         * 格式："yyyy-MM"
         */
        ONLY_MONTH("yyyy-MM"),

        /**
         * 格式："yyyy-MM-dd"
         */
        ONLY_DAY("yyyy-MM-dd"),

        /**
         * 格式："yyyy-MM-dd HH"
         */
        ONLY_HOUR("yyyy-MM-dd HH"),

        /**
         * 格式："yyyy-MM-dd HH:mm"
         */
        ONLY_MINUTE("yyyy-MM-dd HH:mm"),

        /**
         * 格式："MM-dd"
         */
        ONLY_MONTH_DAY("MM-dd"),

        /**
         * 格式："MM-dd HH:mm"
         */
        ONLY_MONTH_SEC("MM-dd HH:mm"),

        /**
         * 格式："HH:mm:ss"
         */
        ONLY_TIME("HH:mm:ss"),

        /**
         * 格式："HH:mm"
         */
        ONLY_HOUR_MINUTE("HH:mm")
    }
}