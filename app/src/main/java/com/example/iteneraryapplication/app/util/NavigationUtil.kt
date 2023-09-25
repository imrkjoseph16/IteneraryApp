package com.example.iteneraryapplication.app.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import javax.inject.Inject

class NavigationUtil @Inject constructor() {

    fun navigateActivity(context: Context, className: Class<*>?) {
        Intent(context, className).apply {
            context.startActivity(this)
        }
    }

    fun navigateActivity(
        context: Context,
        extraValueNamed: String,
        extraValue: String,
        className: Class<*>?
    ) {
        Intent(context, className).apply {
            putExtra(extraValueNamed, extraValue)
            context.startActivity(this)
        }
    }

    fun openExternalBrowser(context: Context, link: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        context.startActivity(intent)
    }
}