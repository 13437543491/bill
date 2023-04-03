package com.book.detail.bill.add

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.book.base.utils.DisplayUtil
import com.book.detail.HomeAnalytics
import com.book.detail.databinding.DialogRemarkBinding

class RemarkDialog(private val mCallback: (input: String) -> Unit, private val isModify: Boolean) :
    DialogFragment() {

    companion object {
        const val REMARK_INPUT_KEY = "remark_input_key"
    }

    private val mHomeAnalytics: HomeAnalytics = HomeAnalytics()

    private lateinit var mBinding: DialogRemarkBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mHomeAnalytics.addBillRemarkShowRecord(isModify)
        mBinding = DialogRemarkBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.let {
            val attributes = it.attributes
            attributes.width = DisplayUtil.getScreenWidth(requireContext())
            it.attributes = attributes
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val remark = arguments?.getString(REMARK_INPUT_KEY) ?: ""
        mBinding.etInput.setText(remark)

        mBinding.tvCancel.setOnClickListener {
            dismiss()
        }

        mBinding.tvEnter.setOnClickListener {
            val input = mBinding.etInput.text.toString()
            if (TextUtils.isEmpty(input)) {
                return@setOnClickListener
            }
            mHomeAnalytics.addBillRemarkEnterRecord(isModify)
            mCallback.invoke(input)
            dismiss()
        }
    }

}