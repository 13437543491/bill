package com.book.bill.data

import androidx.room.*
import com.book.bean.Bill
import com.book.bean.BillCategory

@Dao
interface IBillDao {

    @Insert
    fun insertBill(item: Bill)

    @Query("select bill.id,bill.category_id,bill.money,bill.remark,bill.time,bill.status,category.icon as cIcon,category.name as cName from ${Bill.TAB_NAME} as bill INNER JOIN ${BillCategory.TAB_NAME} as category ON bill.category_id = category.id where bill.id=:id")
    fun queryBill(id: Long): Bill

    @Query("select bill.id,bill.category_id,bill.money,bill.remark,bill.time,bill.status,category.icon as cIcon,category.name as cName from ${Bill.TAB_NAME} as bill INNER JOIN ${BillCategory.TAB_NAME} as category ON bill.category_id = category.id where bill.time between :startTime and :endTime order by bill.time asc")
    fun queryBillList(startTime: String, endTime: String): List<Bill>

    @Query("select bill.id,bill.category_id,bill.money,bill.remark,bill.time,bill.status,category.icon as cIcon,category.name as cName from ${Bill.TAB_NAME} as bill INNER JOIN ${BillCategory.TAB_NAME} as category ON bill.category_id = category.id where bill.time between :startTime and :endTime and bill.status=:status order by bill.money desc")
    fun queryBillRank(startTime: String, endTime: String, status: Int): List<Bill>

    @Delete
    fun deleteBill(item: Bill)

    @Update
    fun editBill(item: Bill)
}