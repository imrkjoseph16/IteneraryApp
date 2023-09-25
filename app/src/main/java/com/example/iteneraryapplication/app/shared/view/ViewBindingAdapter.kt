package com.example.iteneraryapplication.app.shared.view

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.iteneraryapplication.R

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

@BindingAdapter("android:src")
fun setSrcIcon(imageView: AppCompatImageView, icon: Int?) {
    if (icon != null) imageView.setImageResource(icon) else imageView.setImageResource(R.drawable.icon_empty_512px)
}


@BindingAdapter("setCustomColor")
fun setBackgroundColor(view: CardView, customColor: String) = view.setCardBackgroundColor(Color.parseColor(customColor))

@BindingAdapter("glideImage")
fun setGlideImageUrl(view: AppCompatImageView, imageUrl: String? = null) {
    if (imageUrl != null) Glide.with(view.context)
        .load(imageUrl)
        .into(view)
}