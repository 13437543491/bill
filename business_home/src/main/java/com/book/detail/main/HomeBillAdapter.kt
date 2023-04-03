package com.book.detail.main

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.book.base.utils.ResourceUtil
import com.book.bean.Bill
import com.book.bean.BillCategory
import com.book.detail.R
import com.book.detail.databinding.RecyclerBillItemBinding

class HomeBillAdapter(
    private val mBillList: List<Bill>,
    private val mCallback: (bill: Bill) -> Unit
) : RecyclerView.Adapter<HomeBillAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecyclerBillItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.refresh(mBillList[position]) {
            mCallback.invoke(mBillList[position])
        }
    }

    override fun getItemCount(): Int {
        return mBillList.size
    }

    class ViewHolder(private val mBinding: RecyclerBillItemBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun refresh(item: Bill, listener: View.OnClickListener) {
            val context = itemView.context

            mBinding.ivIcon.setImageResource(
                context.resources.getIdentifier(
                    item.cIcon,
                    "drawable",
                    context.packageName
                )
            )

            if (!TextUtils.isEmpty(item.remark)) {
                mBinding.tvRemark.text = item.remark
            } else {
                mBinding.tvRemark.text = item.cName
            }

            if (item.status == BillCategory.GROUP_INCOME) {
                mBinding.tvMoney.setTextColor(ResourceUtil.getColor(context, R.color.color_03ae7c))
                mBinding.tvMoney.text = "+ ${item.money}"
            } else {
                mBinding.tvMoney.setTextColor(ResourceUtil.getColor(context, R.color.color_ff8282))
                mBinding.tvMoney.text = "- ${item.money}"
            }

            itemView.setOnClickListener(listener)
        }
    }
}