package com.example.iteneraryapplication.app.util

import android.app.Activity
import android.net.Uri
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.example.iteneraryapplication.app.widget.NoteBottomSheet

fun Activity.showToastMessage(context: Activity, message: String) = Toast.makeText(context, message, Toast.LENGTH_LONG).show()

fun Activity.formatString(stringResId: Int, vararg args: Any?): String =
    String.format(getString(stringResId, *args))

fun Activity.showBottomNotesOption(context: FragmentManager, canDelete: Boolean = false) {
    val noteBottomSheetFragment = NoteBottomSheet.createInstance(isExistingNotes = canDelete)
    noteBottomSheetFragment.show(context,"Note Bottom Sheet")
}

fun Activity.convertToUri(imageUrl: String?) = Uri.parse(imageUrl)