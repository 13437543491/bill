package com.book.router

import android.content.Context
import androidx.fragment.app.Fragment

interface IBookDetailService {

    fun getBookShelfFragment(): Fragment

    fun jumpBillDetailPage(context: Context, id: Long)

}