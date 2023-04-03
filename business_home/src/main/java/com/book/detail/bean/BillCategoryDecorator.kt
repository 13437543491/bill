package com.book.detail.bean

import com.book.bean.BillCategory

data class BillCategoryDecorator(
    var isSelect: Boolean = false,
    var isSetting: Boolean = false,
    val category: BillCategory
)