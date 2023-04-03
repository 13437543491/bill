package com.book.base.utils

import android.content.Context

object ResourceUtil {

    fun getString(context: Context, id: Int): String {
        return context.resources.getString(id)
    }

    fun getColor(context: Context, id: Int): Int {
        return context.resources.getColor(id)
    }

    fun getDrawable(context: Context, name: String): Int {
        return getIdentifier(context, "drawable", name)
    }

    private fun getIdentifier(context: Context, type: String, name: String): Int {
        return context.resources.getIdentifier(name, type, context.packageName)
    }

}