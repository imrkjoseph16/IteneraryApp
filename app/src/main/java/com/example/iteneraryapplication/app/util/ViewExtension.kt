package com.example.iteneraryapplication.app.util

import android.app.Activity
import android.widget.Toast

fun Activity.showToastMessage(context: Activity, message: String) = Toast.makeText(context, message, Toast.LENGTH_LONG).show()