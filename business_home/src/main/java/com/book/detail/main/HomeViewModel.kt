package com.book.detail.main

import androidx.lifecycle.viewModelScope
import com.book.base.mvi.BaseViewModel
import com.book.router.IBookBillService
import io.github.prototypez.appjoint.AppJoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : BaseViewModel<HomeMainState, HomeMainEvent, HomeMainAction>() {
    override fun handleAction(action: HomeMainAction) {
        when (action) {
            is HomeMainAction.RequestBillInfo -> {
                requestBillInfo(action)
            }
        }
    }

    private fun requestBillInfo(action: HomeMainAction.RequestBillInfo) {
        viewModelScope.launch(Dispatchers.Main) {
            sendEvent(HomeMainEvent.ShowLoading)
            AppJoint.get().moduleApplications()
            val response = AppJoint.service(IBookBillService::class.java)
                .queryBillList(action.startTime, action.endTime)

            setState(
                HomeMainState(
                    action = action,
                    billList = response,
                    isSuccess = true
                )
            )

            sendEvent(HomeMainEvent.DismissLoading)
        }
    }

}