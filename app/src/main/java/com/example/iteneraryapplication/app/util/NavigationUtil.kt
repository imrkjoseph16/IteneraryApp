package com.example.iteneraryapplication.app.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.example.iteneraryapplication.R
import javax.inject.Inject

class NavigationUtil @Inject constructor() {

    fun navigateActivity(context: Context, className: Class<*>?) {
        Intent(context, className).apply {
            context.startActivity(this)
        }
    }

    fun navigateActivity(
        context: Context,
        bundle: Bundle? = null,
        className: Class<*>?
    ) {
        Intent(context, className).apply {
            bundle?.let { putExtras(it) }
            context.startActivity(this)
        }
    }

    fun openExternalBrowser(context: Context, link: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        context.startActivity(intent)
    }

    fun navigateImageTransition(
        source: AppCompatActivity,
        target: Class<out AppCompatActivity>,
        bundle: Bundle? = null,
        imageView: View? = null
    ) {
        if (imageView != null) {
            val intent = Intent(source, target).apply {
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                bundle?.let { putExtras(it) }
            }
            val pair1 = Pair.create(imageView, source.formatString(R.string.image_animation_tag))
            val activityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    source,
                    pair1
                )
            source.startActivity(intent, activityOptionsCompat.toBundle())
        } else {
            navigateActivity(
                context = source,
                bundle = bundle,
                className = target
            )
        }
    }
}