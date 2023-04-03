package com.book.bill.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.book.base.constant.RoomConstants
import com.book.bean.Bill
import com.book.bean.BillCategory

@Database(
    entities = [Bill::class, BillCategory::class],
    version = RoomConstants.ROOM_VERSION,
    exportSchema = false
)
abstract class BillDataBase : RoomDatabase() {

    companion object {

        private lateinit var mData: BillDataBase

        fun init(context: Context) {
            if (!this::mData.isInitialized) {
                mData = Room.databaseBuilder(
                    context,
                    BillDataBase::class.java,
                    RoomConstants.ROOM_NAME
                ).build()
            }
        }

        @Synchronized
        fun getInstance(): BillDataBase {
            return mData
        }
    }

    abstract fun getCategoryDao(): ICategoryDao

    abstract fun getBillDao(): IBillDao
}