package com.book.base.ui

import androidx.fragment.app.Fragment
import com.book.base.ui.dialog.LoadingDialog
import com.book.base.utils.LogUtil
import com.book.base.utils.SystemUtils
import com.book.base.utils.ToastUtil

abstract class BaseFragment : Fragment() {

    private lateinit var mLoadingDialog: LoadingDialog

    fun showToast(msg: String) {
        ToastUtil.showToast(requireContext(), msg)
    }

    fun showToast(resId: Int) {
        showToast(requireContext().getString(resId))
    }

    fun getStatusBarHeight(): Int {
        return SystemUtils.getStatusBarHeight(requireContext())
    }

    fun showLoading() {
        if (!this::mLoadingDialog.isInitialized) {
            mLoadingDialog = LoadingDialog(requireContext())
            mLoadingDialog.setCancelable(false)
        }
        mLoadingDialog.show()
    }

    fun hideLoading() {
        if (this::mLoadingDialog.isInitialized) {
            mLoadingDialog.dismiss()
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            onPause()
        } else {
            onResume()
        }
    }

    override fun onDestroy() {
        hideLoading()
        super.onDestroy()
    }

    open fun onBackPressed(): Boolean {
        return false
    }

}