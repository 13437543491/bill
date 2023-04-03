package com.book.detail.category.add

import android.graphics.Rect
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.book.base.ui.BaseFragment
import com.book.base.utils.DisplayUtil
import com.book.base.utils.LogUtil
import com.book.base.utils.ResourceUtil
import com.book.bean.BillCategory
import com.book.detail.BookDetailConstants
import com.book.detail.HomeAnalytics
import com.book.detail.R
import com.book.detail.category.BillCategoryAction
import com.book.detail.category.BillCategoryActivity
import com.book.detail.category.BillCategoryEvent
import com.book.detail.category.BillCategoryViewModel
import com.book.detail.databinding.FragmentAddCategoryBinding

class CategoryAddFragment : BaseFragment() {

    private lateinit var mBinding: FragmentAddCategoryBinding

    private val mViewModel = BillCategoryViewModel()

    private var mSelectIcon = ""
    private var isIncome: Boolean = false

    private val mHomeAnalytics = HomeAnalytics()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentAddCategoryBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel.subscribeState(this) { state ->
            when (state.action) {
                is BillCategoryAction.GetAllCategoryList -> {
                    initRecyclerView(state.categoryMap)
                }
                is BillCategoryAction.SaveCategory -> {
                    showToast(R.string.add_success)
                    requireActivity().finish()
                }
            }
        }

//        mViewModel.subscribeEvent(this) { event ->
//            if (event == BillCategoryEvent.ShowLoading) {
//                showLoading()
//            } else if (event == BillCategoryEvent.DismissLoading) {
//                hideLoading()
//            }
//        }

        mBinding.tvSubmit.setOnClickListener {
            val name = mBinding.etName.text.toString()
            if (TextUtils.isEmpty(name)) {
                showToast(R.string.category_add_hint)
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(mSelectIcon)) {
                return@setOnClickListener
            }

            mHomeAnalytics.addCategoryAddEnterClick(name)

            mViewModel.sendAction(
                BillCategoryAction.SaveCategory(
                    name, mSelectIcon, if (isIncome) {
                        BillCategory.GROUP_INCOME
                    } else {
                        BillCategory.GROUP_EXPEND
                    }
                )
            )
        }

        arguments?.let {
            isIncome = it.getBoolean(BookDetailConstants.IS_INCOME)
        }

        mViewModel.sendAction(BillCategoryAction.GetAllCategoryList(requireContext()))
    }

    private fun initRecyclerView(categoryMap: Map<String, List<String>>) {
        val adapter = CategoryAddAdapter(categoryMap, mBinding.recyclerView) {
            mSelectIcon = it
            mBinding.ivIcon.setImageResource(
                ResourceUtil.getDrawable(
                    requireContext(),
                    it
                )
            )
        }
        mBinding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 5).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (adapter.getItemViewType(position) == 0) {
                        5
                    } else {
                        1
                    }
                }
            }
        }
        mBinding.recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                itemPosition: Int,
                parent: RecyclerView
            ) {
                outRect.top = DisplayUtil.dp2px(parent.context, 18f)
            }
        })
        mBinding.recyclerView.adapter = adapter
    }
}