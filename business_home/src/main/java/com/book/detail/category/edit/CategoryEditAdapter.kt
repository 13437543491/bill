package com.book.detail.category.edit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.book.base.utils.LogUtil
import com.book.detail.bean.BillCategoryDecorator
import com.book.detail.databinding.RecyclerCategoryEditItemBinding
import java.util.*

class CategoryEditAdapter(private val mDelCallback: (item: BillCategoryDecorator) -> Unit) :
    RecyclerView.Adapter<CategoryEditAdapter.ViewHolder>() {

    private val mList = mutableListOf<BillCategoryDecorator>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecyclerCategoryEditItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.refresh(mList[position]) {
            mDelCallback.invoke(mList[holder.bindingAdapterPosition])
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

    fun delItem(position: Int) {
        mList.removeAt(position)
        notifyDataSetChanged()
    }

    fun delItem(item: BillCategoryDecorator) {
        mList.remove(item)
        notifyDataSetChanged()
    }

    fun swap(viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) {
        val from = viewHolder.bindingAdapterPosition
        val to = target.bindingAdapterPosition
        if (from > to) {
            mList[from].category.sort--
            mList[to].category.sort++
        } else {
            mList[from].category.sort++
            mList[to].category.sort--
        }

        Collections.swap(mList, viewHolder.bindingAdapterPosition, target.bindingAdapterPosition)
        notifyItemMoved(viewHolder.bindingAdapterPosition, target.bindingAdapterPosition)
    }

    fun getItem(index: Int): BillCategoryDecorator {
        return mList[index]
    }

    class ViewHolder(private val mBinding: RecyclerCategoryEditItemBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun refresh(item: BillCategoryDecorator, delClickListener: View.OnClickListener) {
            val billCategory = item.category

            mBinding.ivDel.setOnClickListener(delClickListener)

            val context = itemView.context
            mBinding.ivIcon.setImageResource(
                context.resources.getIdentifier(
                    billCategory.icon,
                    "drawable",
                    context.packageName
                )
            )

            mBinding.tvTitle.text = billCategory.name
        }
    }
}