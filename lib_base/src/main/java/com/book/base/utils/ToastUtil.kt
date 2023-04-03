package com.book.base.utils

import android.content.Context
import android.widget.Toast

object ToastUtil {

    private lateinit var mToast: Toast

    fun showToast(context: Context, msg: String) {
        if (this::mToast.isInitialized) {
            mToast.setText(msg)
        } else {
            mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        }
        mToast.show()
    }
}