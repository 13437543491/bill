package com.book.base.ui.dialog

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import com.book.base.R

class LoadingDialog(context: Context) : AlertDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_loading)
    }

    override fun show() {
        super.show()
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}