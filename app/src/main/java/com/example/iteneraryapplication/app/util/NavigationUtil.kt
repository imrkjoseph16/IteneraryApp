package com.example.iteneraryapplication.app.util

import android.content.Context
import android.content.Intent
import javax.inject.Inject

class NavigationUtil @Inject constructor() {

    fun navigateActivity(context: Context, className: Class<*>?) {
        Intent(context, className).apply {
            context.startActivity(this)
        }
    }
}