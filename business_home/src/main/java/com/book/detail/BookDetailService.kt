package com.book.detail

import android.content.Context
import androidx.fragment.app.Fragment
import com.book.detail.bill.detail.BillDetailActivity
import com.book.detail.main.HomeFragment
import com.book.router.IBookDetailService
import io.github.prototypez.appjoint.core.ServiceProvider

@ServiceProvider
class BookDetailService : IBookDetailService {
    override fun getBookShelfFragment(): Fragment {
        return HomeFragment()
    }

    override fun jumpBillDetailPage(context: Context, id: Long) {
        BillDetailActivity.actionStart(context, id)
    }
}