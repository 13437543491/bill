package com.book.detail.main

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.book.base.utils.DisplayUtil
import com.book.base.utils.LogUtil
import com.book.base.utils.ResourceUtil
import com.book.bean.Bill
import com.book.detail.R
import com.book.detail.bean.BillDecorator
import com.book.detail.databinding.RecyclerBillTitleBinding

class HomeBillTitleAdapter(private val mCallback: (bill: Bill) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mBillList = mutableListOf<BillDecorator>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (mBillList.isEmpty()) {
            EmptyViewHolder(layoutInflater.inflate(R.layout.recycler_bill_empty, parent, false))
        } else {
            val binding = RecyclerBillTitleBinding.inflate(layoutInflater, parent, false)
            ViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.refresh(mBillList[position], mCallback)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (mBillList.isEmpty()) {
            0
        } else {
            1
        }
    }

    override fun getItemCount(): Int {
        if (mBillList.isEmpty()) {
            return 1
        }
        return mBillList.size
    }

    fun refresh(billList: List<BillDecorator>) {
        mBillList.clear()
        mBillList.addAll(billList)
        notifyDataSetChanged()
    }

    class ViewHolder(private val mBinding: RecyclerBillTitleBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

        fun refresh(item: BillDecorator, callback: (bill: Bill) -> Unit) {
            val context = itemView.context

            mBinding.tvDate.text = item.date
            mBinding.tvIncome.text =
                "${ResourceUtil.getString(context, R.string.income)}：${item.income}"
            mBinding.tvExpend.text =
                "${ResourceUtil.getString(context, R.string.expend)}：${item.expend}"

            mBinding.recyclerView.layoutManager = LinearLayoutManager(itemView.context)
            if (mBinding.recyclerView.itemDecorationCount == 0) {
                mBinding.recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
                    override fun getItemOffsets(
                        outRect: Rect,
                        itemPosition: Int,
                        parent: RecyclerView
                    ) {
                        outRect.top = DisplayUtil.dp2px(context, 10f)
                    }
                })
            }
            mBinding.recyclerView.adapter = HomeBillAdapter(item.billList, callback)
        }

    }

    class EmptyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    }
}