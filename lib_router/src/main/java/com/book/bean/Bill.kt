package com.book.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.book.bean.Bill.Companion.TAB_NAME
import java.io.Serializable

@Entity(tableName = TAB_NAME)
class Bill(
    @ColumnInfo(name = "category_id", typeAffinity = ColumnInfo.INTEGER)
    var categoryId: Long,

    @ColumnInfo(name = "money", typeAffinity = ColumnInfo.TEXT)
    var money: String,

    @ColumnInfo(name = "remark", typeAffinity = ColumnInfo.TEXT)
    var remark: String,

    @ColumnInfo(name = "time", typeAffinity = ColumnInfo.TEXT)
    var time: String,

    @ColumnInfo(name = "status", typeAffinity = ColumnInfo.INTEGER)
    var status: Int,

    val cIcon: String = "",
    val cName: String = ""
) : Serializable {

    companion object {
        const val TAB_NAME = "bill"
    }

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}

