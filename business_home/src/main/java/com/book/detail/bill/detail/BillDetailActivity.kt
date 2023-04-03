package com.book.detail.bill.detail

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.book.base.ui.BaseActivity
import com.book.base.utils.ResourceUtil
import com.book.bean.Bill
import com.book.bean.BillCategory
import com.book.detail.R
import com.book.detail.bill.add.AddBillActivity
import com.book.detail.category.BillCategoryAction
import com.book.detail.category.BillCategoryViewModel
import com.book.detail.databinding.ActivityBillDetailBinding

class BillDetailActivity : BaseActivity() {

    companion object {
        private const val BILL_ID_KEY = "bill_id_key"

        fun actionStart(context: Context, id: Long) {
            Intent(context, BillDetailActivity::class.java).apply {
                putExtra(BILL_ID_KEY, id)
                context.startActivity(this)
            }
        }
    }

    private val mViewModel = BillCategoryViewModel()

    private var mBillId = 0L
    private lateinit var mBinding: ActivityBillDetailBinding
    private lateinit var mBill: Bill

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityBillDetailBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mViewModel.subscribeState(this) { state ->
            when (state.action) {
                is BillCategoryAction.DeleteBill -> {
                    showToast(R.string.delete_success)
                    finish()
                }

                is BillCategoryAction.QueryBill -> {
                    state.bill?.let {
                        mBill = it
                        setBillInfo()
                    }
                }
            }
        }

        mBillId = intent.getLongExtra(BILL_ID_KEY, mBillId)

        mBinding.toolBar.apply {
            showToolbarBack(this)
            title = ""
        }

        mBinding.tvEdit.setOnClickListener {
            if (this::mBill.isInitialized) {
                AddBillActivity.actionStart(this, mBill)
            }
        }

        mBinding.tvDelete.setOnClickListener {
            if (this::mBill.isInitialized) {
                showDeleteDialog()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mViewModel.sendAction(BillCategoryAction.QueryBill(mBillId))
    }

    private fun setBillInfo() {
        mBinding.ivIcon.setImageResource(ResourceUtil.getDrawable(this, mBill.cIcon))
        mBinding.tvTitle.text = mBill.cName
        mBinding.tvType.text = if (mBill.status == BillCategory.GROUP_INCOME) {
            getString(R.string.income)
        } else {
            getString(R.string.expend)
        }
        mBinding.tvMoney.text = mBill.money
        mBinding.tvDate.text = mBill.time
        mBinding.tvRemark.text = if (!TextUtils.isEmpty(mBill.remark)) {
            mBill.remark
        } else {
            mBill.cName
        }
    }

    private fun showDeleteDialog() {
        AlertDialog.Builder(this).setTitle(R.string.warring)
            .setMessage(R.string.delete_bill_tip)
            .setNegativeButton(R.string.cancel, object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {

                }
            })
            .setPositiveButton(R.string.enter, object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    mViewModel.sendAction(BillCategoryAction.DeleteBill(mBill))
                }
            }).create().show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item);
    }
}