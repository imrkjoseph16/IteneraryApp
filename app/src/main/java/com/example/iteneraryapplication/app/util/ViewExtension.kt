package com.example.iteneraryapplication.app.util

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.example.iteneraryapplication.app.shared.component.NoteBottomSheet

fun Activity.showToastMessage(context: Activity, message: String) = Toast.makeText(context, message, Toast.LENGTH_LONG).show()

fun Activity.formatString(stringResId: Int, vararg args: Any?): String =
    String.format(getString(stringResId, *args))

fun Activity.showBottomSheet(context: FragmentManager) {
    val noteBottomSheetFragment = NoteBottomSheet.createInstance()
    noteBottomSheetFragment.show(context,"Note Bottom Sheet")
}