package com.book.base.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.book.base.ui.dialog.LoadingDialog
import com.book.base.utils.SystemUtils
import com.book.base.utils.ToastUtil

abstract class BaseActivity : AppCompatActivity() {

    private lateinit var mLoadingDialog: LoadingDialog

    fun showToast(msg: String) {
        ToastUtil.showToast(this, msg)
    }

    fun showToast(resId: Int) {
        showToast(getString(resId))
    }

    fun hideActionBar() {
        supportActionBar?.hide()
    }

    fun getStatusBarHeight(): Int {
        return SystemUtils.getStatusBarHeight(this)
    }

    fun showLoading() {
        if (!this::mLoadingDialog.isInitialized) {
            mLoadingDialog = LoadingDialog(this)
            mLoadingDialog.setCancelable(false)
        }
        mLoadingDialog.show()
    }

    fun hideLoading() {
        if (this::mLoadingDialog.isInitialized) {
            mLoadingDialog.dismiss()
        }
    }

    fun showToolbarBack(toolbar: Toolbar) {
        this.setSupportActionBar(toolbar);
        supportActionBar?.let {
            it.setHomeButtonEnabled(true);
            it.setDisplayHomeAsUpEnabled(true);
            it.setDisplayShowTitleEnabled(false);
        }
    }

    override fun onDestroy() {
        hideLoading()
        super.onDestroy()
    }
}