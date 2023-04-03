package com.book.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.book.bean.BillCategory.Companion.TAB_NAME

@Entity(tableName = TAB_NAME)
data class BillCategory(
    @ColumnInfo(name = "name", typeAffinity = ColumnInfo.TEXT)
    val name: String,

    @ColumnInfo(name = "icon", typeAffinity = ColumnInfo.TEXT)
    val icon: String,

    @ColumnInfo(name = "group", typeAffinity = ColumnInfo.INTEGER)
    val group: Int,

    @ColumnInfo(name = "sort", typeAffinity = ColumnInfo.INTEGER)
    var sort: Int = -1,

    @ColumnInfo(name = "status", typeAffinity = ColumnInfo.INTEGER)
    var status: Int = 1
) {

    companion object {
        const val TAB_NAME = "bill_category"
        const val GROUP_INCOME = 1
        const val GROUP_EXPEND = 2
    }

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}