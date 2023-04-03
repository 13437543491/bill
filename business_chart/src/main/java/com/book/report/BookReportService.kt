package com.book.report

import androidx.fragment.app.Fragment
import com.book.report.main.BookReportFragment
import com.book.router.IBookReportService
import io.github.prototypez.appjoint.core.ServiceProvider

@ServiceProvider
class BookReportService : IBookReportService {
    override fun getBookFeaturedFragment(): Fragment {
        return BookReportFragment()
    }

}