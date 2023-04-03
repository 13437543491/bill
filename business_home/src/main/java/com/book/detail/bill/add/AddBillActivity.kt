package com.book.detail.bill.add

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import com.book.base.ui.BaseActivity
import com.book.base.ui.BaseFragment
import com.book.bean.Bill
import com.book.bean.BillCategory
import com.book.detail.BookDetailConstants
import com.book.detail.R
import com.book.detail.bill.detail.BillDetailActivity
import com.book.detail.category.BillCategoryActivity
import com.book.detail.category.add.CategoryAddFragment
import com.book.detail.category.edit.CategoryEditFragment
import com.book.detail.databinding.ActivityAddBillBinding
import com.book.detail.databinding.ActivityBillCategoryBinding
import java.lang.RuntimeException

class AddBillActivity : BaseActivity() {

    companion object {
        const val BILL_KEY = "bill_key"

        fun actionStart(context: Context, bill: Bill? = null) {
            Intent(context, AddBillActivity::class.java).apply {
                putExtra(BILL_KEY, bill)
                context.startActivity(this)
            }
        }
    }

    private lateinit var mBinding: ActivityAddBillBinding
    private lateinit var mRadioGroup: RadioGroup

    private var mCurrentFragment: Fragment? = null

    private var mBill: Bill? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAddBillBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mRadioGroup = mBinding.root.findViewById(R.id.radioGroup)
        mBinding.root.findViewById<RadioButton>(R.id.rb1).setText(R.string.expend)
        mBinding.root.findViewById<RadioButton>(R.id.rb2).setText(R.string.income)

        mBill = intent.getSerializableExtra(BILL_KEY) as Bill?

        initToolBar()
        initViewPager()
    }

    private fun initToolBar() {
        mBinding.toolBar.apply {
            showToolbarBack(this)
            title = getString(R.string.add_bill)
        }
    }

    private fun initViewPager() {
        mRadioGroup.setOnCheckedChangeListener { radioGroup, itemId ->
            val isIncome = itemId == R.id.rb2
            showFragment(AddBillFragment().apply {
                val bundle = Bundle()
                bundle.putBoolean(BookDetailConstants.IS_INCOME, isIncome)
                if (mBill != null) {
                    bundle.putSerializable(BILL_KEY, mBill)
                }
                arguments = bundle
            })
        }

        if (mBill != null) {
            if (mBill?.status == BillCategory.GROUP_INCOME) {
                mRadioGroup.check(R.id.rb2)
            } else {
                mRadioGroup.check(R.id.rb1)
            }
        } else {
            mRadioGroup.check(R.id.rb1)
        }
    }

    private fun showFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_root, fragment)
        transaction.commit()
        mCurrentFragment = fragment
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item);
    }

    override fun onBackPressed() {
        mCurrentFragment?.let {
            if (it is BaseFragment && it.onBackPressed()) {
                return
            }
        }
        super.onBackPressed()
    }
}