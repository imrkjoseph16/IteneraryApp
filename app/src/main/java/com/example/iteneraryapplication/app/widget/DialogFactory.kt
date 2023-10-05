package com.example.iteneraryapplication.app.widget

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import androidx.core.view.forEachIndexed
import com.example.iteneraryapplication.R
import com.example.iteneraryapplication.databinding.CustomColumnDialogBinding
import com.example.iteneraryapplication.databinding.CustomReminderDialogBinding

class DialogFactory {

    companion object {

        fun showCustomDialog(
            context: Activity,
            dialogAttributes: DialogAttributes,
            primaryButtonClicked: (() -> Unit)? = null,
            secondaryButtonClicked: (() -> Unit)? = null
        ) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            CustomReminderDialogBinding.inflate(inflater).apply {
                val dialog = Dialog(context, R.style.ThemeDialog)
                dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
                dialog.setContentView(root)

                data = dialogAttributes
                primaryButton.setOnClickListener {
                    primaryButtonClicked?.invoke()
                    dialog.dismiss()
                }
                secondaryButton.setOnClickListener {
                    secondaryButtonClicked?.invoke()
                    dialog.dismiss()
                }

                dialog.show()
            }
        }

        @SuppressLint("SetTextI18n")
        fun showColumnDialog(
            context: Activity,
            onItemClicked: ((spanCount: Int) -> Unit)? = null
        ) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            CustomColumnDialogBinding.inflate(inflater).apply {
                val dialog = Dialog(context, R.style.ThemeDialog)
                dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
                dialog.setContentView(root)

                layoutColumn.forEachIndexed { count, view ->
                    view.setOnClickListener {
                        onItemClicked?.invoke(count.inc()).also { dialog.dismiss() }
                    }
                }

                dialog.show()
            }
        }
    }

    data class DialogAttributes(
        val title: String? = null,
        val subTitle: String? = null,
        val primaryButtonTitle: String? = null,
        val secondaryButtonTitle: String? = null
    )
}