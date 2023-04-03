package com.book.detail.category.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.book.base.ui.BaseFragment
import com.book.base.utils.LogUtil
import com.book.bean.BillCategory
import com.book.detail.BookDetailConstants
import com.book.detail.HomeAnalytics
import com.book.detail.R
import com.book.detail.bean.BillCategoryDecorator
import com.book.detail.category.BillCategoryAction
import com.book.detail.category.BillCategoryActivity
import com.book.detail.category.BillCategoryEvent
import com.book.detail.category.BillCategoryViewModel
import java.util.*

class CategoryEditFragment : BaseFragment() {

    private lateinit var mRecyclerView: RecyclerView

    private val mViewModel = BillCategoryViewModel()

    private var mAdapter: CategoryEditAdapter? = null

    private var isIncome = false

    private val mHomeAnalytics = HomeAnalytics()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mRecyclerView = RecyclerView(requireContext())
        return mRecyclerView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel.subscribeState(this) { state ->
            when (state.action) {
                is BillCategoryAction.RequestIncomeList -> {
                    initRecyclerView(state.categoryList)
                }
                is BillCategoryAction.RequestExpendList -> {
                    initRecyclerView(state.categoryList)
                }
                is BillCategoryAction.RemoveCategory -> {
                    mAdapter?.delItem(state.action.item)
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

        arguments?.let {
            isIncome = it.getBoolean(BookDetailConstants.IS_INCOME)
        }
    }

    override fun onResume() {
        super.onResume()
        if (isIncome) {
            mViewModel.sendAction(BillCategoryAction.RequestIncomeList)
        } else {
            mViewModel.sendAction(BillCategoryAction.RequestExpendList)
        }
    }

    private fun initRecyclerView(list: List<BillCategoryDecorator>) {
        mRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        mRecyclerView.adapter = CategoryEditAdapter { item ->
            mHomeAnalytics.addCategoryDelClick(item.category.name, !isIncome)
            showDelDialog(item)
        }.apply {
            addAll(list)
            mAdapter = this
        }

        val helper = ItemTouchHelper(object : ItemTouchHelper.Callback() {

            private var formPosition = -1
            private var targetPosition = -1

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags: Int = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                val swipeFlags: Int = ItemTouchHelper.ACTION_STATE_IDLE
                return makeMovementFlags(dragFlags, swipeFlags)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                if (formPosition == -1) {
                    formPosition = viewHolder.bindingAdapterPosition
                }
                targetPosition = target.bindingAdapterPosition
                mAdapter?.swap(viewHolder, target)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            }

            override fun isLongPressDragEnabled(): Boolean {
                return true
            }

            override fun isItemViewSwipeEnabled(): Boolean {
                return false
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)

                if (formPosition != targetPosition) {
                    mAdapter?.let {
                        val items = mutableListOf<BillCategory>()

                        if (formPosition < targetPosition) {
                            for (index in formPosition..targetPosition) {
                                items.add(it.getItem(index).category)
                            }
                        } else {
                            for (index in targetPosition..formPosition) {
                                items.add(it.getItem(index).category)
                            }
                        }

                        mViewModel.sendAction(BillCategoryAction.SwapCategorySort(items))
                    }
                }

                formPosition = -1
                targetPosition = -1
            }
        })
        helper.attachToRecyclerView(mRecyclerView)
    }

    private fun showDelDialog(item: BillCategoryDecorator) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.delete) + item.category.name)
            .setMessage(R.string.delete_category_note)
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.enter) { dialog, which ->
                mHomeAnalytics.addCategoryDelEnterClick(item.category.name, !isIncome)
                mViewModel.sendAction(BillCategoryAction.RemoveCategory(item))
            }.create().show()
    }
}