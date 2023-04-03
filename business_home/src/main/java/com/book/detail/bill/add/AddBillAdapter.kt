package com.book.detail.bill.add

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.book.bean.Bill
import com.book.detail.R
import com.book.detail.bean.BillCategoryDecorator
import com.book.detail.databinding.RecyclerAddAccountItemBinding

class AddBillAdapter(
    private val mCallback: (item: BillCategoryDecorator) -> Unit
) : RecyclerView.Adapter<AddBillAdapter.ViewHolder>() {

    private val mList: MutableList<BillCategoryDecorator> = mutableListOf()

    private var mSelectPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        RecyclerAddAccountItemBinding.inflate(layoutInflater, parent, false).apply {
            return ViewHolder(this)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        if (mSelectPosition == position) {
            mList[position].isSelect = true
            mCallback.invoke(mList[position])
        }

        holder.refresh(mList[position]) {
            val item = mList[position]
            if (item.isSetting) {
                mCallback.invoke(item)
                return@refresh
            }

            if (mSelectPosition == position) {
                return@refresh
            }

            if (mSelectPosition != -1) {
                mList[mSelectPosition].isSelect = false
                notifyItemChanged(mSelectPosition)
            }

            item.isSelect = true
            notifyItemChanged(position)

            mSelectPosition = position
            mCallback.invoke(item)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun addAll(list: List<BillCategoryDecorator>) {
        mList.clear()
        mList.addAll(list)
        notifyDataSetChanged()
    }

    fun setSelectItem(bill: Bill) {
        var index = 0
        mList.forEach {
            if (it.category.icon == bill.cIcon) {
                mSelectPosition = index
                return@forEach
            }
            index++
        }

        if (mSelectPosition == -1) {
            mSelectPosition = 0
        }

        notifyDataSetChanged()
    }

    class ViewHolder(private val mBinding: RecyclerAddAccountItemBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun refresh(item: BillCategoryDecorator, onClickListener: View.OnClickListener) {
            val context = itemView.context

            if (item.isSelect) {
                mBinding.ivIcon.setBackgroundResource(R.drawable.item_account_select)
            } else {
                mBinding.ivIcon.setBackgroundResource(R.drawable.item_account_no_select)
            }

            mBinding.ivIcon.setImageResource(
                context.resources.getIdentifier(
                    item.category.icon,
                    "drawable",
                    context.packageName
                )
            )

            mBinding.tvName.text = item.category.name

            itemView.setOnClickListener(onClickListener)
        }
    }
}