package com.book.detail.main

import com.book.base.mvi.IAction
import com.book.base.mvi.IEvent
import com.book.base.mvi.IState
import com.book.bean.Bill

sealed class HomeMainAction : IAction {
    data class RequestBillInfo(val startTime: String, val endTime: String) : HomeMainAction()
}

data class HomeMainState(
    val action: HomeMainAction,
    val billList: List<Bill> = mutableListOf(),
    val isSuccess: Boolean = true,
    val isNetError: Boolean = false,
    var errorMsg: String = ""
) : IState

sealed class HomeMainEvent : IEvent {
    object ShowLoading : HomeMainEvent()
    object DismissLoading : HomeMainEvent()
}