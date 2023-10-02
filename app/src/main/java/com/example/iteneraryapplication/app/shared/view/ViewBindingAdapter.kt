package com.example.iteneraryapplication.app.shared.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.iteneraryapplication.R
import com.example.iteneraryapplication.app.extension.removeWord
import com.example.iteneraryapplication.app.util.ViewUtil.Companion.appendStringBuilder
import com.example.iteneraryapplication.dashboard.shared.data.Expenses

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
    if (icon != null) imageView.setImageResource(icon) else imageView.setImageResource(R.drawable.icon_empty_planning)
}

@BindingAdapter("setCustomColor")
fun setBackgroundColor(view: CardView, customColor: String) = view.setCardBackgroundColor(Color.parseColor(customColor))

@BindingAdapter("glideImage")
fun setGlideImageUrl(view: ImageView, imageUrl: String? = null) {
    if (imageUrl != null) Glide.with(view.context)
        .load(imageUrl)
        .into(view)
}

@BindingAdapter("setText")
fun setText(view: TextView, word: String? = null) {
    if (word != null) view.text = word.removeWord("null")
}

@SuppressLint("SetTextI18n")
@BindingAdapter("setCustomAmountExpenses")
fun setCustomExpenses(view: TextView, list: MutableList<Expenses>? = null) {
    list?.onEach {
        view.text = view.text.toString() + appendStringBuilder("${it.typeOfExpenses}: ₱${it.expensesAmount}")

        if (view.id == R.id.total_expenses) view.text = Html.fromHtml("${view.context.getString(R.string.total_amount)}: " +
                "<font color=\"#1FBE94\">₱${it.totalAmount.toString()}</font>")
    }
}

@BindingAdapter("setSpinnerText")
fun setSpinnerText(view: Spinner, word: String? = null) {
    if (word != null) {
        view.context.resources.getStringArray(R.array.gender_list)
            .onEachIndexed { index, gender ->
            if (word == gender) view.setSelection(index)
        }
    }
}
