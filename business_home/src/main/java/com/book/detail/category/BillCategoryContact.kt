package com.book.detail.category

import android.content.Context
import com.book.base.mvi.IAction
import com.book.base.mvi.IEvent
import com.book.base.mvi.IState
import com.book.bean.Bill
import com.book.bean.BillCategory
import com.book.detail.bean.BillCategoryDecorator

sealed class BillCategoryAction : IAction {
    object RequestIncomeList : BillCategoryAction()
    object RequestExpendList : BillCategoryAction()
    data class RemoveCategory(val item: BillCategoryDecorator) : BillCategoryAction()
    data class GetAllCategoryList(val context: Context) : BillCategoryAction()
    data class SwapCategorySort(val items: List<BillCategory>) : BillCategoryAction()
    data class SaveCategory(val name: String, val icon: String, val group: Int) :
        BillCategoryAction()

    data class QueryBill(val id: Long) : BillCategoryAction()
    data class SaveBill(val bill: Bill) : BillCategoryAction()
    data class EditBill(val bill: Bill) : BillCategoryAction()
    data class DeleteBill(val bill: Bill) : BillCategoryAction()
}

data class BillCategoryState(
    val action: BillCategoryAction,
    var bill: Bill? = null,
    val categoryList: List<BillCategoryDecorator> = mutableListOf(),
    val categoryMap: Map<String, List<String>> = mutableMapOf(),
    val isSuccess: Boolean = true,
    val isNetError: Boolean = false,
    var errorMsg: String = ""
) : IState

sealed class BillCategoryEvent : IEvent {
    object ShowLoading : BillCategoryEvent()
    object DismissLoading : BillCategoryEvent()
}