package com.book.detail.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.book.base.ui.BaseFragment
import com.book.base.utils.DateUtils
import com.book.base.widget.MyDateWheelLayout
import com.book.bean.Bill
import com.book.bean.BillCategory
import com.book.detail.HomeAnalytics
import com.book.detail.bean.BillDecorator
import com.book.detail.bill.add.AddBillActivity
import com.book.detail.bill.detail.BillDetailActivity
import com.book.detail.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment() {

    private lateinit var mBinding: FragmentHomeBinding

    private val mViewModel = HomeViewModel()

    private var mYear: Int = 0
    private var mMonth: Int = 0

    private val mHomeAnalytics = HomeAnalytics()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel.subscribeState(this) { state ->
            when (state.action) {
                is HomeMainAction.RequestBillInfo -> {
                    initRecyclerView(state.billList)
                }
            }
        }

//        mViewModel.subscribeEvent(this) { event ->
//            if (event == HomeMainEvent.ShowLoading) {
//                showLoading()
//            } else if (event == HomeMainEvent.DismissLoading) {
//                hideLoading()
//            }
//        }

        mYear = DateUtils.getCurYear()
        mMonth = DateUtils.getCurMonth()

        mBinding.llDate.setOnClickListener {
            mHomeAnalytics.addHomeDateSelectClick()
            DateUtils.showDatePicker(
                activity = requireActivity(),
                isToday = false,
                curYear = mYear,
                curMonth = mMonth,
                dateShowMode = MyDateWheelLayout.DateShowMode.YEAR_MONTH
            ) { year: Int, month: Int, day: Int ->
                mYear = year
                mMonth = month
                requestBillList()
            }
        }

        mBinding.btnAdd.setOnClickListener {
            mHomeAnalytics.addBillAddClickRecord()
            AddBillActivity.actionStart(requireContext())
        }

        mBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        mBinding.recyclerView.adapter = HomeBillTitleAdapter {
            mHomeAnalytics.addHomeBillDetailClick()
            BillDetailActivity.actionStart(requireContext(), it.id)
        }
    }

    override fun onResume() {
        super.onResume()
        requestBillList()
    }

    private fun requestBillList() {
        mBinding.tvYear.text = DateUtils.formatterYear(mYear)
        mBinding.tvMonth.text = DateUtils.formatterMonth(mMonth)

        mViewModel.sendAction(
            HomeMainAction.RequestBillInfo(
                DateUtils.getFirstDayOfMonth(
                    mYear,
                    mMonth
                ), DateUtils.getLastDayOfMonth(mYear, mMonth)
            )
        )
    }

    private fun initRecyclerView(list: List<Bill>) {
        var totalIncome = 0f
        var totalExpend = 0f
        val adapter = mBinding.recyclerView.adapter

        if (list.isEmpty()) {
            if (adapter is HomeBillTitleAdapter) {
                adapter.refresh(mutableListOf())
            }
        } else {
            val map = mutableMapOf<String, MutableList<Bill>>()

            for (item in list) {
                val key = item.time
                if (map.containsKey(key)) {
                    map[key]!!.add(item)
                } else {
                    mutableListOf<Bill>().apply {
                        add(item)
                        map[key] = this
                    }
                }
            }

            var income = 0f
            var expend = 0f
            val billDecoratorList = mutableListOf<BillDecorator>()
            for ((key, value) in map) {
                value.forEach {
                    if (it.status == BillCategory.GROUP_INCOME) {
                        income += it.money.toFloat()
                        totalIncome += it.money.toFloat()
                    } else {
                        expend += it.money.toFloat()
                        totalExpend += it.money.toFloat()
                    }
                }

                billDecoratorList.add(
                    BillDecorator(
                        key,
                        income.toString(),
                        expend.toString(),
                        value
                    )
                )
                income = 0f
                expend = 0f
            }

            if (adapter is HomeBillTitleAdapter) {
                adapter.refresh(billDecoratorList)
            }
        }

        mBinding.tvTotalIncome.text = totalIncome.toString()
        mBinding.tvTotalExpend.text = totalExpend.toString()
    }

}