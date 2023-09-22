package com.example.iteneraryapplication.app.extension

import android.view.View

fun View.setVisible(isShow: Boolean) {
    this.visibility = if (isShow) View.VISIBLE else View.GONE
}