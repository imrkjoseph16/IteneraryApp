package com.example.iteneraryapplication.app.util

import android.app.Activity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

fun Activity.showToastMessage(context: Activity, message: String) = Toast.makeText(context, message, Toast.LENGTH_LONG).show()

fun Activity.formatString(stringResId: Int, vararg args: Any?): String =
    String.format(getString(stringResId, *args))