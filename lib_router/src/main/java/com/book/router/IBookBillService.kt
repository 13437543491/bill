package com.book.router

import com.book.bean.Bill
import com.book.bean.BillCategory

interface IBookBillService {

    suspend fun getIncomeList(): List<BillCategory>

    suspend fun getExpendList(): List<BillCategory>

    suspend fun removeBillCategory(item: BillCategory)

    suspend fun swapCategorySort(items: List<BillCategory>)

    suspend fun addCategory(billCategory: BillCategory)

    suspend fun addBill(bill: Bill)

    suspend fun queryBill(id: Long): Bill

    suspend fun queryBillList(startTime: String, endTime: String): List<Bill>

    suspend fun queryBillRank(startTime: String, endTime: String, status: Int): List<Bill>

    suspend fun deleteBill(bill: Bill)

    suspend fun editBill(bill: Bill)
}