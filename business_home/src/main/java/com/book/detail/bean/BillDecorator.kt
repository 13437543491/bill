package com.book.detail.bean

import com.book.bean.Bill

data class BillDecorator(
    val date: String,
    val income: String,
    val expend: String,
    val billList: List<Bill>
)