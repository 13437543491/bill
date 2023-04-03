package com.book.report.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.book.base.ui.BaseFragment
import com.book.base.utils.DateUtils
import com.book.base.utils.LogUtil
import com.book.bean.Bill
import com.book.bean.BillCategory
import com.book.report.ChartAnalytics
import com.book.report.R
import com.book.report.databinding.FragmentBookChartBinding
import com.book.report.main.BookReportFragment
import com.book.router.IBookBillService
import com.book.router.IBookDetailService
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import io.github.prototypez.appjoint.AppJoint

class BookChartFragment(
    private var mStartTime: String,
    private var mEndTime: String,
    private val mIsQueryYear: Boolean
) : BaseFragment() {

    private lateinit var mBinding: FragmentBookChartBinding
    private lateinit var mAdapter: BookChartAdapter

    private val mViewModel = BookChartViewModel()
    private var mIsSelectIncome = false

    private var mBillList: List<Bill>? = null

    private val mChartAnalytics = ChartAnalytics()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentBookChartBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.subscribeState(this) { state ->
            when (state.action) {
                is BookChartAction.QueryBillInfo -> {
                    initData(state.billList)
                }
            }
        }

        mBinding.radioGroup.setOnCheckedChangeListener { radioGroup, itemId ->
            mIsSelectIncome = itemId == R.id.rb_income
            mBillList?.let {
                initData(it)
            }
            mChartAnalytics.addChartBillTypeClickRecord(!mIsSelectIncome, !mIsQueryYear)
        }
        mBinding.radioGroup.check(R.id.rb_expend)

        mAdapter = BookChartAdapter(mIsQueryYear) {
            mChartAnalytics.addChartBillDetailClickRecord(!mIsQueryYear)
            AppJoint.service(IBookDetailService::class.java)
                .jumpBillDetailPage(requireContext(), it.id)
        }
        mBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        mBinding.recyclerView.adapter = mAdapter
    }

    private fun initData(billList: List<Bill>) {
        mBillList = billList

        val realBillList = mutableListOf<Bill>()

        var totalIncome = 0f
        var totalExpend = 0f
        billList.forEach {
            if (it.status == BillCategory.GROUP_INCOME) {
                totalIncome += it.money.toFloat()
                if (mIsSelectIncome) {
                    realBillList.add(it)
                }
            } else {
                totalExpend += it.money.toFloat()
                if (!mIsSelectIncome) {
                    realBillList.add(it)
                }
            }
        }

        mBinding.rbExpend.text = "${getString(R.string.expend)} $totalExpend"
        mBinding.rbIncome.text = "${getString(R.string.income)} $totalIncome"

        if (realBillList.isEmpty()) {
            mBinding.recyclerView.visibility = View.GONE
            mBinding.llEmpty.visibility = View.VISIBLE
            return
        }
        mBinding.recyclerView.visibility = View.VISIBLE
        mBinding.llEmpty.visibility = View.GONE

        mAdapter.refreshData(realBillList)
    }

    fun refreshData(startTime: String, endTime: String) {
        mStartTime = startTime
        mEndTime = endTime
        requestBillInfo()
    }

    private fun requestBillInfo() {
        mViewModel.sendAction(
            BookChartAction.QueryBillInfo(
                mStartTime, mEndTime, if (mIsSelectIncome) {
                    BillCategory.GROUP_INCOME
                } else {
                    BillCategory.GROUP_EXPEND
                }
            )
        )
    }

    override fun onResume() {
        super.onResume()
        requestBillInfo()
    }
}