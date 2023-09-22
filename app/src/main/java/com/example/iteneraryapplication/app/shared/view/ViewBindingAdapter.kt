package com.example.iteneraryapplication.app.shared.view

import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.databinding.BindingAdapter

@BindingAdapter("visible")
fun setVisible(view: View, visible: Boolean) {
    view.isVisible = visible
}

@BindingAdapter("setCustomHeight")
fun setCustomHeight(view: View, @DimenRes margin: Int) {
    view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
        height = view.resources.getDimension(margin).toInt()
    }
}