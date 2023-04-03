package com.book.report.detail

import androidx.lifecycle.viewModelScope
import com.book.base.mvi.BaseViewModel
import com.book.router.IBookBillService
import io.github.prototypez.appjoint.AppJoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookChartViewModel : BaseViewModel<BookChartState, BookChartEvent, BookChartAction>() {
    override fun handleAction(action: BookChartAction) {
        when (action) {
            is BookChartAction.QueryBillInfo -> {
                requestBillInfo(action)
            }
        }
    }

    private fun requestBillInfo(action: BookChartAction.QueryBillInfo) {
        viewModelScope.launch(Dispatchers.Main) {
            sendEvent(BookChartEvent.ShowLoading)

            val response = AppJoint.service(IBookBillService::class.java)
                .queryBillList(action.startTime, action.endTime)

            setState(
                BookChartState(
                    action = action,
                    billList = response,
                    isSuccess = true
                )
            )

            sendEvent(BookChartEvent.DismissLoading)
        }
    }
}