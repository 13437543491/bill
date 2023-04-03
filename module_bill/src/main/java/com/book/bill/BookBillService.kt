package com.book.bill

import com.book.bean.Bill
import com.book.bean.BillCategory
import com.book.bill.data.BillDataBase
import com.book.router.IBookBillService
import io.github.prototypez.appjoint.core.ServiceProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@ServiceProvider
class BookBillService : IBookBillService {
    override suspend fun getIncomeList(): List<BillCategory> = withContext(Dispatchers.IO) {
        BillDataBase.getInstance().getCategoryDao().getIncomeList()
    }

    override suspend fun getExpendList(): List<BillCategory> = withContext(Dispatchers.IO) {
        BillDataBase.getInstance().getCategoryDao().getExpendList()
    }

    override suspend fun removeBillCategory(item: BillCategory) = withContext(Dispatchers.IO) {
        item.status = 0
        BillDataBase.getInstance().getCategoryDao().deleteCategory(item)
    }

    override suspend fun swapCategorySort(items: List<BillCategory>) = withContext(Dispatchers.IO) {
        BillDataBase.getInstance().getCategoryDao().setSoft(items)
    }

    override suspend fun addCategory(billCategory: BillCategory) = withContext(Dispatchers.IO) {
        val categoryDao = BillDataBase.getInstance().getCategoryDao()
        billCategory.sort = categoryDao.getAllCount() + 1
        categoryDao.insertCategory(billCategory)
    }

    override suspend fun addBill(bill: Bill) = withContext(Dispatchers.IO) {
        BillDataBase.getInstance().getBillDao().insertBill(bill)
    }

    override suspend fun queryBill(id: Long): Bill = withContext(Dispatchers.IO) {
        BillDataBase.getInstance().getBillDao().queryBill(id)
    }

    override suspend fun queryBillList(startTime: String, endTime: String): List<Bill> =
        withContext(Dispatchers.IO) {
            BillDataBase.getInstance().getBillDao().queryBillList(startTime, endTime)
        }

    override suspend fun queryBillRank(
        startTime: String,
        endTime: String,
        status: Int
    ) = withContext(Dispatchers.IO) {
        BillDataBase.getInstance().getBillDao().queryBillRank(startTime, endTime, status)
    }

    override suspend fun deleteBill(bill: Bill) = withContext(Dispatchers.IO) {
        BillDataBase.getInstance().getBillDao().deleteBill(bill)
    }

    override suspend fun editBill(bill: Bill) = withContext(Dispatchers.IO) {
        BillDataBase.getInstance().getBillDao().editBill(bill)
    }

}