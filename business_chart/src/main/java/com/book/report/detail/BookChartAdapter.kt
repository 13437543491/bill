package com.book.report.detail

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.book.base.utils.DateUtils
import com.book.bean.Bill
import com.book.report.R
import com.book.report.databinding.RecyclerChartBillItemBinding
import com.book.report.databinding.RecyclerChartItemBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet

class BookChartAdapter(
    private val isQueryYear: Boolean,
    private val mCallback: (bill: Bill) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mList: MutableList<Bill> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == 0) {
            val binding = RecyclerChartItemBinding.inflate(layoutInflater, parent, false)
            BarChartViewHolder(binding)
        } else {
            val binding = RecyclerChartBillItemBinding.inflate(layoutInflater, parent, false)
            BillItemViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BillItemViewHolder) {
            holder.refresh(mList[position - 1]) {
                mCallback.invoke(mList[position - 1])
            }
        } else if (holder is BarChartViewHolder) {
            holder.refresh(isQueryYear, mList)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return 0
        }
        return 1
    }

    override fun getItemCount(): Int {
        if (mList.isEmpty()) {
            return 0
        }
        return mList.size + 1
    }

    fun refreshData(list: List<Bill>) {
        mList.clear()
        mList.addAll(list)
        notifyDataSetChanged()
    }

    class BarChartViewHolder(private val mBinding: RecyclerChartItemBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun refresh(isQueryYear: Boolean, billList: List<Bill>) {
            val resources = itemView.context.resources
            val barChart = itemView as BarChart

            initBarChart(barChart)
            val entryList = if (isQueryYear) {
                getYearEntryList(billList)
            } else {
                getMonthEntryList(billList)
            }

            if (barChart.data != null && barChart.data.dataSetCount > 0) {
                (barChart.data.getDataSetByIndex(0) as BarDataSet).values = entryList
                barChart.data.notifyDataChanged()
                barChart.notifyDataSetChanged()
            } else {
                val dataSets = ArrayList<IBarDataSet>()
                BarDataSet(entryList, "").apply {
                    setDrawIcons(false)
                    setDrawValues(false)
                    color = resources.getColor(R.color.color_000000)
                    valueTextColor = resources.getColor(R.color.color_03ae7c)
                    dataSets.add(this)
                }

                BarData(dataSets).apply {
                    barWidth = 0.5f
                    isHighlightEnabled = true
                    barChart.data = this
                }
            }
            barChart.invalidate()
            barChart.animateY(200)
        }

        private fun initBarChart(barChart: BarChart) {
            barChart.setNoDataText("")
            barChart.setScaleEnabled(false)
            barChart.description.isEnabled = false
            barChart.legend.isEnabled = false

            barChart.axisLeft.axisMinimum = 0f
            barChart.axisLeft.isEnabled = false
            barChart.axisRight.isEnabled = false
            barChart.xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM;
                setDrawGridLines(false)
                textColor = barChart.context.resources.getColor(R.color.color_000000);
            }
        }

        private fun getYearEntryList(billList: List<Bill>): List<BarEntry> {
            val map = mutableMapOf<Int, Float>()

            for (item in billList) {
                val key = item.time.split("-")[1].toInt()
                if (map.containsKey(key)) {
                    var oldMoney = map[key]!!
                    oldMoney += item.money.toFloat()
                    map[key] = oldMoney
                } else {
                    map[key] = item.money.toFloat()
                }
            }

            val barEntryList = mutableListOf<BarEntry>()
            for (index in 1..12) {
                if (map.containsKey(index)) {
                    barEntryList.add(BarEntry(index.toFloat(), map[index]!!))
                } else {
                    barEntryList.add(BarEntry(index.toFloat(), 0f))
                }
            }
            return barEntryList
        }

        private fun getMonthEntryList(billList: List<Bill>): List<BarEntry> {
            val map = mutableMapOf<Int, Float>()

            for (item in billList) {
                val key = item.time.split("-")[2].toInt()
                if (map.containsKey(key)) {
                    var oldMoney = map[key]!!
                    oldMoney += item.money.toFloat()
                    map[key] = oldMoney
                } else {
                    map[key] = item.money.toFloat()
                }
            }

            val barEntryList = mutableListOf<BarEntry>()
            val dateSplit = billList[0].time.split("-")
            val dayCount = DateUtils.getDayCount(dateSplit[0].toInt(), dateSplit[1].toInt())
            for (index in 1..dayCount) {
                if (map.containsKey(index)) {
                    barEntryList.add(BarEntry(index.toFloat(), map[index]!!))
                } else {
                    barEntryList.add(BarEntry(index.toFloat(), 0f))
                }
            }
            return barEntryList
        }
    }

    class BillItemViewHolder(private val mBinding: RecyclerChartBillItemBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

        fun refresh(item: Bill, onClickListener: View.OnClickListener) {
            val context = itemView.context

            mBinding.ivIcon.setImageResource(
                context.resources.getIdentifier(
                    item.cIcon,
                    "drawable",
                    context.packageName
                )
            )

            mBinding.tvName.text = item.cName
            mBinding.tvMoney.text = item.money
            mBinding.tvDate.text = item.time
            if (TextUtils.isEmpty(item.remark)) {
                mBinding.tvRemark.visibility = View.GONE
            } else {
                mBinding.tvRemark.visibility = View.VISIBLE
                mBinding.tvRemark.text = item.remark
            }

            itemView.setOnClickListener(onClickListener)
        }
    }
}