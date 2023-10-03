package com.example.iteneraryapplication.app.extension

import android.view.View
import android.widget.Spinner

fun View.setVisible(isShow: Boolean) {
    this.visibility = if (isShow) View.VISIBLE else View.GONE
}

fun String.removeWord(text: String) = replace(text, "")

fun String.convertInt() = try { Integer.parseInt(this) } catch (e: Exception) { 0 }

fun String.replaceWord(text: String, replaceText: String) = replace(text, replaceText)

fun <T>MutableList<T>.notEmpty() = if (this.isEmpty()) null else this

fun Spinner.notEmpty() = if (selectedItem != null) selectedItem.toString() else null