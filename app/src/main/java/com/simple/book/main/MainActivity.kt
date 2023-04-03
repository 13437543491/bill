package com.simple.book.main

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.simple.book.R
import com.simple.book.databinding.ActivityMainBinding
import com.book.base.ui.BaseActivity
import com.book.base.utils.SystemUtils
import com.book.base.widget.tabs.OnTabChangedListener
import com.book.router.IBookReportService
import com.book.router.IBookDetailService
import io.github.prototypez.appjoint.AppJoint
import java.lang.RuntimeException

class MainActivity : BaseActivity(), OnTabChangedListener {

    private var mCurrentFragment: Fragment? = null

    private val mFragments = arrayOfNulls<Fragment>(4)

    private lateinit var mBinding: ActivityMainBinding

    private val mAnalytics = MainAnalytics()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        SystemUtils.systemUiInit(this, window.decorView)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.ivMore.setOnClickListener {
            AlertDialog.Builder(this).setTitle(R.string.about_me).setMessage(R.string.about_content)
                .setPositiveButton(R.string.enter) { p0, p1 -> }
                .create().show()
        }

        mBinding.alphaTabs.setOnTabChangedListener(this)
        onTabSelected(0)
    }

    override fun onTabSelected(tabNum: Int) {
        if (tabNum == 0) {
            mAnalytics.addHomeTabClick()
        } else if (tabNum == 1) {
            mAnalytics.addChartTabClick()
        }

        var fragment: Fragment? = mFragments[tabNum]
        if (mCurrentFragment != null && mCurrentFragment == fragment) {
            return
        }

        if (fragment == null) {
            fragment = when (tabNum) {
                0 -> {
                    AppJoint.service(IBookDetailService::class.java).getBookShelfFragment()
                }
                1 -> {
                    AppJoint.service(IBookReportService::class.java).getBookFeaturedFragment()
                }
                else -> {
                    throw RuntimeException("unknown index $tabNum")
                }
            }
            mFragments[tabNum] = fragment
        }

        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()

        mCurrentFragment?.let {
            transaction.hide(it)
        }

        if (fragment.isAdded) {
            transaction.show(fragment).commit()
        } else {
            transaction.add(R.id.fl_root, fragment).commit()
        }

        mCurrentFragment = fragment
    }
}