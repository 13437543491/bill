package com.book.report.detail

import com.book.base.mvi.IAction
import com.book.base.mvi.IEvent
import com.book.base.mvi.IState
import com.book.bean.Bill

sealed class BookChartAction : IAction {
    data class QueryBillInfo(val startTime: String, val endTime: String, val status: Int) :
        BookChartAction()
}

data class BookChartState(
    val action: BookChartAction,
    val billList: List<Bill> = mutableListOf(),
    val isSuccess: Boolean = true,
    val isNetError: Boolean = false,
    var errorMsg: String = ""
) : IState

sealed class BookChartEvent : IEvent {
    object ShowLoading : BookChartEvent()
    object DismissLoading : BookChartEvent()
}