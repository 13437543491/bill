package com.book.detail.category

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
import com.book.detail.BookDetailConstants
import com.book.detail.HomeAnalytics
import com.book.detail.R
import com.book.detail.category.add.CategoryAddFragment
import com.book.detail.bill.add.AddBillFragment
import com.book.detail.category.edit.CategoryEditFragment
import com.book.detail.databinding.ActivityBillCategoryBinding
import java.lang.RuntimeException

class BillCategoryActivity : BaseActivity() {

    companion object {
        const val OPEN_MODEL_KEY = "open_model_key"

        const val CATEGORY_SETTING = 2
        const val ADD_CATEGORY = 3

        fun actionStart(context: Context, model: Int, isIncome: Boolean = false) {
            Intent(context, BillCategoryActivity::class.java).apply {
                putExtra(OPEN_MODEL_KEY, model)
                putExtra(BookDetailConstants.IS_INCOME, isIncome)
                context.startActivity(this)
            }
        }
    }

    private lateinit var mBinding: ActivityBillCategoryBinding
    private lateinit var mRadioGroup: RadioGroup

    private var mOpenModel: Int = -1
    private var mIsIncome: Boolean = false

    private var mCurrentFragment: Fragment? = null

    private val mHomeAnalytics = HomeAnalytics()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityBillCategoryBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mOpenModel = intent.getIntExtra(OPEN_MODEL_KEY, -1)
        if (mOpenModel == -1) {
            throw RuntimeException()
        }

        if (mOpenModel == CATEGORY_SETTING) {
            mBinding.llAddCategory.visibility = View.VISIBLE
        }

        mBinding.llAddCategory.setOnClickListener {
            mHomeAnalytics.addCategoryAddClick(!mIsIncome)
            actionStart(this, ADD_CATEGORY, mIsIncome)
        }

        mIsIncome = intent.getBooleanExtra(BookDetailConstants.IS_INCOME, false)

        mRadioGroup = mBinding.root.findViewById(R.id.radioGroup)
        mBinding.root.findViewById<RadioButton>(R.id.rb1).setText(R.string.expend)
        mBinding.root.findViewById<RadioButton>(R.id.rb2).setText(R.string.income)

        initToolBar()
        initViewPager()
    }

    private fun initToolBar() {
        mBinding.toolBar.apply {
            showToolbarBack(this)
            when (mOpenModel) {
                CATEGORY_SETTING -> {
                    title = getString(R.string.category_setting_title)
                }
                ADD_CATEGORY -> {
                    title = getString(R.string.category_add)
                }
            }
        }
    }

    private fun initViewPager() {
        if (mOpenModel != ADD_CATEGORY) {
            mRadioGroup.setOnCheckedChangeListener { radioGroup, itemId ->
                mIsIncome = itemId == R.id.rb2
                when (mOpenModel) {
                    CATEGORY_SETTING -> {
                        showFragment(CategoryEditFragment().apply {
                            val bundle = Bundle()
                            bundle.putBoolean(BookDetailConstants.IS_INCOME, mIsIncome)
                            arguments = bundle
                        })
                    }
                }
            }
            if (mOpenModel == CATEGORY_SETTING) {
                if (mIsIncome) {
                    mRadioGroup.check(R.id.rb2)
                } else {
                    mRadioGroup.check(R.id.rb1)
                }
            } else {
                mRadioGroup.check(R.id.rb1)
            }
        } else {
            mRadioGroup.visibility = View.GONE
            showFragment(CategoryAddFragment().apply {
                val bundle = Bundle()
                bundle.putBoolean(BookDetailConstants.IS_INCOME, mIsIncome)
                arguments = bundle
            })
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