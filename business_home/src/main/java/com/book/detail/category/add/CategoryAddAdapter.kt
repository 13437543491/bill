package com.book.detail.category.add

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.book.base.utils.LogUtil
import com.book.base.utils.ResourceUtil
import com.book.detail.R
import com.book.detail.databinding.RecyclerCategoryAddItemBinding
import com.book.detail.databinding.RecyclerCategoryAddTitleBinding

class CategoryAddAdapter(
    private val mCategoryMap: Map<String, List<String>>,
    private val mRecyclerView: RecyclerView,
    private val mSelectCallback: (value: String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mSelectPosition = -1
    private var mSelectValue: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == 0) {
            val binding = RecyclerCategoryAddTitleBinding.inflate(layoutInflater, parent, false)
            TitleViewHolder(binding)
        } else {
            val binding = RecyclerCategoryAddItemBinding.inflate(layoutInflater, parent, false)
            ItemViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TitleViewHolder) {
            var index = 0
            for ((key, value) in mCategoryMap) {
                if (position == index) {
                    holder.refresh(key)
                    break
                }
                index += value.size + 1
            }
        } else if (holder is ItemViewHolder) {
            var range = 0
            var subIndex = 0
            for ((key, value) in mCategoryMap) {
                range += value.size + 1
                subIndex++
                if (position <= range) {
                    val item = value[position - subIndex]
                    holder.itemView.tag = item
                    val isSelect = mSelectPosition == position
                    holder.refresh(item, isSelect)
                    break
                }
                subIndex += value.size
            }

            if (mSelectPosition == -1 && position == 1) {
                mSelectPosition = position
                holder.setSelect(true)
                mSelectValue = holder.getValue()
                invokeSelect()
            }

            holder.itemView.setOnClickListener {
                if (mSelectPosition == position) {
                    return@setOnClickListener
                }

                mRecyclerView.findViewHolderForAdapterPosition(mSelectPosition)?.apply {
                    if (this is ItemViewHolder) {
                        this.setSelect(false)
                    }
                }
                mSelectPosition = position
                holder.setSelect(true)
                mSelectValue = holder.getValue()
                invokeSelect()
            }
        }
    }

    private fun invokeSelect() {
        mSelectCallback.invoke(mSelectValue ?: "")
    }

    override fun getItemCount(): Int {
        var size = 0
        for ((key, value) in mCategoryMap) {
            size++
            size += value.size
        }
        return size
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return 0
        }

        var index = 1
        for ((key, value) in mCategoryMap) {
            index += value.size
            if (position <= index) {
                if (position == index) {
                    return 0
                }
                break
            }
            index++
        }

        return 1
    }

    class TitleViewHolder(private val mBinding: RecyclerCategoryAddTitleBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

        fun refresh(title: String) {
            mBinding.tvTitle.text = title
        }
    }

    class ItemViewHolder(private val mBinding: RecyclerCategoryAddItemBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

        private var mValue: String? = null

        fun refresh(value: String, isSelect: Boolean) {
            mValue = value
            mBinding.ivIcon.apply {
                setImageResource(ResourceUtil.getDrawable(context, value))
                setSelect(isSelect)
            }
        }

        fun setSelect(isSelect: Boolean) {
            mBinding.ivIcon.apply {
                if (isSelect) {
                    setBackgroundResource(R.drawable.item_account_select)
                } else {
                    setBackgroundResource(R.drawable.item_account_no_select)
                }
            }
        }

        fun getValue(): String? {
            return mValue
        }
    }
}