package com.book.bill.data

import androidx.room.*
import com.book.bean.BillCategory

@Dao
interface ICategoryDao {

    @Insert
    fun insertCategory(item: BillCategory)

    @Insert
    fun insertAll(items: List<BillCategory>)

    @Update
    fun deleteCategory(item: BillCategory)

    @Query("select * from ${BillCategory.TAB_NAME} where `group`=${BillCategory.GROUP_INCOME} and `status`=1 order by sort asc")
    fun getIncomeList(): List<BillCategory>

    @Query("select * from ${BillCategory.TAB_NAME} where `group`=${BillCategory.GROUP_EXPEND} and `status`=1 order by sort asc")
    fun getExpendList(): List<BillCategory>

    @Update
    fun setSoft(items: List<BillCategory>)

    @Query("select count(id) from ${BillCategory.TAB_NAME}")
    fun getAllCount(): Int
}