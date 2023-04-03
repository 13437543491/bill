package com.book.detail.bill.add

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.book.base.ui.BaseFragment
import com.book.base.utils.DateUtils
import com.book.base.utils.DisplayUtil
import com.book.base.widget.MyDateWheelLayout
import com.book.bean.Bill
import com.book.bean.BillCategory
import com.book.detail.BookDetailConstants
import com.book.detail.HomeAnalytics
import com.book.detail.R
import com.book.detail.bean.BillCategoryDecorator
import com.book.detail.bill.detail.BillDetailActivity
import com.book.detail.category.BillCategoryAction
import com.book.detail.category.BillCategoryActivity
import com.book.detail.category.BillCategoryEvent
import com.book.detail.category.BillCategoryViewModel
import com.book.detail.databinding.FragmentAddBillBinding

class AddBillFragment : BaseFragment() {

    private lateinit var mBinding: FragmentAddBillBinding

    private val mViewModel: BillCategoryViewModel = BillCategoryViewModel()

    private var isIncome = false

    private var mCurYear = DateUtils.getCurYear()
    private var mCurMonth = DateUtils.getCurMonth()
    private var mCurDay = DateUtils.getCurDay()

    private var mCategory: BillCategory? = null
    private var mAdapter: AddBillAdapter? = null

    private var mIsEdit = false
    private lateinit var mBill: Bill

    private val mHomeAnalytics = HomeAnalytics()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentAddBillBinding.inflate(inflater, container, false)
        return mBinding.root
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
                is BillCategoryAction.SaveBill -> {
                    showToast(R.string.add_success)
                    requireActivity().finish()
                }
                is BillCategoryAction.EditBill -> {
                    showToast(R.string.edit_success)
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

        arguments?.let {
            isIncome = it.getBoolean(BookDetailConstants.IS_INCOME)
            it.getSerializable(AddBillActivity.BILL_KEY)?.let { bill ->
                mIsEdit = true
                mBill = bill as Bill
            }
        }

        initView()
    }

    override fun onResume() {
        super.onResume()
        if (isIncome) {
            mViewModel.sendAction(BillCategoryAction.RequestIncomeList)
        } else {
            mViewModel.sendAction(BillCategoryAction.RequestExpendList)
        }
    }

    private fun initView() {
        mBinding.tvMoney.setOnClickListener {

        }

        mBinding.llRemard.setOnClickListener {
            RemarkDialog({
                mBinding.tvRemark.text = it
            }, mIsEdit).apply {
                isCancelable = false
                val bundle = Bundle()
                bundle.putString(RemarkDialog.REMARK_INPUT_KEY, mBinding.tvRemark.text.toString())
                arguments = bundle
            }.show(childFragmentManager, "")
        }

        mBinding.flDel.setOnClickListener {
            val currentText = mBinding.tvMoney.text.toString()
            if (currentText == "0") {
                return@setOnClickListener
            }

            val newText = if (currentText.length == 1) {
                "0"
            } else {
                currentText.substring(0, currentText.length - 1)
            }
            mBinding.tvMoney.text = newText
        }

        mBinding.llCalendar.setOnClickListener {
            DateUtils.showDatePicker(
                activity = requireActivity(),
                isToday = false,
                curYear = mCurYear,
                curMonth = mCurMonth,
                curDay = mCurDay,
                dateShowMode = MyDateWheelLayout.DateShowMode.YEAR_MONTH_DAY
            ) { year: Int, month: Int, day: Int ->
                setDate(year, month, day)
            }
        }

        mBinding.tvEnter.setOnClickListener {
            val category = mCategory ?: return@setOnClickListener

            val money = mBinding.tvMoney.text.toString()
            if (money == "0" || money == "0.") {
                return@setOnClickListener
            }

            if (money.toFloat() == 0F) {
                return@setOnClickListener
            }

            mHomeAnalytics.addBillSubmitRecord(!isIncome, mIsEdit)
            DateUtils.formatterDate(mCurYear, mCurMonth, mCurDay) { year, month, day ->
                if (mIsEdit) {
                    mBill.categoryId = category.id
                    mBill.money = money
                    mBill.remark = mBinding.tvRemark.text.toString()
                    mBill.time = "${year}-${month}-${day}"
                    mBill.status = if (isIncome) {
                        BillCategory.GROUP_INCOME
                    } else {
                        BillCategory.GROUP_EXPEND
                    }
                    mViewModel.sendAction(BillCategoryAction.EditBill(mBill))
                } else {
                    mViewModel.sendAction(
                        BillCategoryAction.SaveBill(
                            Bill(
                                category.id,
                                money,
                                mBinding.tvRemark.text.toString(),
                                "${year}-${month}-${day}",
                                if (isIncome) {
                                    BillCategory.GROUP_INCOME
                                } else {
                                    BillCategory.GROUP_EXPEND
                                }
                            )
                        )
                    )
                }
            }
        }

        mBinding.tvNum1.setOnClickListener(mNumClickListener)
        mBinding.tvNum2.setOnClickListener(mNumClickListener)
        mBinding.tvNum3.setOnClickListener(mNumClickListener)
        mBinding.tvNum4.setOnClickListener(mNumClickListener)
        mBinding.tvNum5.setOnClickListener(mNumClickListener)
        mBinding.tvNum6.setOnClickListener(mNumClickListener)
        mBinding.tvNum7.setOnClickListener(mNumClickListener)
        mBinding.tvNum8.setOnClickListener(mNumClickListener)
        mBinding.tvNum9.setOnClickListener(mNumClickListener)
        mBinding.tvNum0.setOnClickListener(mNumClickListener)
        mBinding.tvNumDot.setOnClickListener(mNumClickListener)

        if (mIsEdit) {
            showEditView()
            mBinding.tvMoney.text = mBill.money
            mBinding.tvRemark.text = mBill.remark
            mBill.time.split("-").let {
                setDate(it[0].toInt(), it[1].toInt(), it[2].toInt())
            }
        }
    }

    private fun showEditView() {
        if (mBinding.llInput.visibility == View.GONE) {
            mBinding.llInput.visibility = View.VISIBLE
        }
    }

    private fun setDate(year: Int, month: Int, day: Int) {
        mCurYear = year
        mCurMonth = month
        mCurDay = day
        if (year == DateUtils.getCurYear() && month == DateUtils.getCurMonth() && day == DateUtils.getCurDay()) {
            mBinding.tvCalendar.text = getString(R.string.today)
        } else {
            mBinding.tvCalendar.text = "${year}-${month}-${day}"
        }
    }

    private fun initRecyclerView(list: List<BillCategoryDecorator>) {
        val billList = list.toMutableList()
        billList.add(
            BillCategoryDecorator(
                isSelect = false, isSetting = true,
                BillCategory(
                    getString(R.string.category_setting), "icon_setting", if (isIncome) {
                        BillCategory.GROUP_INCOME
                    } else {
                        BillCategory.GROUP_EXPEND
                    }
                )
            )
        )

        mAdapter?.let {
            it.addAll(billList)
            return
        }

        mBinding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 4)
        mBinding.recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                itemPosition: Int,
                parent: RecyclerView
            ) {
                outRect.top = DisplayUtil.dp2px(parent.context, 18f)
            }
        })
        mBinding.recyclerView.adapter = AddBillAdapter() {
            if (it.isSetting) {
                mHomeAnalytics.addCategorySettingClick(!isIncome)
                BillCategoryActivity.actionStart(
                    requireContext(),
                    BillCategoryActivity.CATEGORY_SETTING,
                    isIncome
                )
                return@AddBillAdapter
            }

            mCategory = it.category
            showEditView()
        }.apply {
            mAdapter = this
            addAll(billList)
            if (mIsEdit) {
                setSelectItem(mBill)
            }
        }
    }

    private val mNumClickListener = View.OnClickListener { view ->
        val currentMoneyText = mBinding.tvMoney.text.toString()
        val isEmptyInput = currentMoneyText == "0"

        if (view == mBinding.tvNum0 && isEmptyInput) {
            return@OnClickListener
        }

        val dotText = "."
        if (view == mBinding.tvNumDot) {
            if (currentMoneyText.indexOf(dotText) != -1) {
                return@OnClickListener
            } else if (isEmptyInput) {
                mBinding.tvMoney.text = "0."
                return@OnClickListener
            }
        }

        if (currentMoneyText.indexOf(dotText) != -1) {
            currentMoneyText.split(dotText).apply {
                if (this[1].length >= 2) {
                    return@OnClickListener
                }
            }
        } else if (currentMoneyText.length >= 8 && view != mBinding.tvNumDot) {
            return@OnClickListener
        }

        val moneyText = (view as TextView).text.toString()
        val newText = if (currentMoneyText == "0") {
            moneyText
        } else {
            currentMoneyText + moneyText
        }

        mBinding.tvMoney.text = newText
    }

    override fun onBackPressed(): Boolean {
        if (mBinding.llInput.visibility == View.VISIBLE) {
            mBinding.llInput.visibility = View.GONE
            return true
        }
        return false
    }
}