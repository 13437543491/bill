package com.book.report.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.book.base.utils.DateUtils
import com.book.base.widget.MyDateWheelLayout
import com.book.report.ChartAnalytics
import com.book.report.R
import com.book.report.databinding.FragmentBookReportBinding
import com.book.report.detail.BookChartFragment

class BookReportFragment : Fragment() {

    private lateinit var mBinding: FragmentBookReportBinding
    private lateinit var mRadioGroup: RadioGroup

    private var mYear: Int = 0
    private var mMonth: Int = 0

    private var isOnlyYear: Boolean = false

    private val mFragmentList = mutableListOf<Fragment>()

    private val mChartAnalytics = ChartAnalytics()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentBookReportBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mYear = DateUtils.getCurYear()
        mMonth = DateUtils.getCurMonth()

        mRadioGroup = mBinding.root.findViewById(R.id.radioGroup)
        mBinding.root.findViewById<RadioButton>(R.id.rb1).setText(R.string.month_label)
        mBinding.root.findViewById<RadioButton>(R.id.rb2).setText(R.string.year_label)

        mRadioGroup.check(R.id.rb1)
        mRadioGroup.setOnCheckedChangeListener { radioGroup, itemId ->
            isOnlyYear = if (itemId == R.id.rb1) {
                mBinding.viewPager.currentItem = 0
                false
            } else {
                mBinding.viewPager.currentItem = 1
                true
            }
            refreshDate()
        }

        mBinding.viewPager.adapter = object : FragmentPagerAdapter(childFragmentManager) {
            override fun getCount(): Int {
                return 2
            }

            override fun getItem(position: Int): Fragment {
                if (mFragmentList.size < position + 1) {
                    mFragmentList.add(
                        BookChartFragment(
                            DateUtils.getFirstDayOfMonth(
                                mYear,
                                mMonth
                            ), DateUtils.getLastDayOfMonth(mYear, mMonth),
                            position == 1
                        )
                    )
                }
                return mFragmentList[position]
            }
        }

        mBinding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    mRadioGroup.check(R.id.rb1)
                } else {
                    mRadioGroup.check(R.id.rb2)
                }
                mChartAnalytics.addChartTopTabClickRecord(position == 0)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })

        mBinding.tvDate.setOnClickListener {
            mChartAnalytics.addChartDateSelectClickRecord(!isOnlyYear)
            DateUtils.showDatePicker(
                activity = requireActivity(),
                isToday = false,
                curYear = mYear,
                curMonth = mMonth,
                dateShowMode = if (isOnlyYear) {
                    MyDateWheelLayout.DateShowMode.YEAR
                } else {
                    MyDateWheelLayout.DateShowMode.YEAR_MONTH
                }
            ) { year: Int, month: Int, day: Int ->
                mYear = year
                if (!isOnlyYear) {
                    mMonth = month
                }
                refreshDate()
            }
        }

        refreshDate(false)
    }

    private fun refreshDate(isRefresh: Boolean = true) {
        if (isOnlyYear) {
            mBinding.tvDate.text = mYear.toString()
        } else {
            mBinding.tvDate.text = "$mYear-$mMonth"
        }

        if (!isRefresh) {
            return
        }

        val itemFragment = mFragmentList[mBinding.viewPager.currentItem] as BookChartFragment
        if (isOnlyYear) {
            itemFragment.refreshData(
                DateUtils.getFirstDayOfMonth(
                    mYear,
                    1
                ), DateUtils.getLastDayOfMonth(mYear, 12)
            )
        } else {
            itemFragment.refreshData(
                DateUtils.getFirstDayOfMonth(
                    mYear,
                    mMonth
                ), DateUtils.getLastDayOfMonth(mYear, mMonth)
            )
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden && mFragmentList.isNotEmpty()) {
            mFragmentList[mBinding.viewPager.currentItem].onHiddenChanged(hidden)
        }
    }

    override fun onResume() {
        super.onResume()
    }
}