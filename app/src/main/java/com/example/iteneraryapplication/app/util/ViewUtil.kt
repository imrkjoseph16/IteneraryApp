package com.example.iteneraryapplication.app.util

import android.view.View
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.iteneraryapplication.R
import javax.inject.Inject
class ViewUtil @Inject constructor() {

    fun animateRecyclerView(recyclerView: RecyclerView, isVisible: Boolean) {
        recyclerView.visibility = if (isVisible) View.VISIBLE else View.GONE
        recyclerView.post {
            val context = recyclerView.context
            val animation =
                AnimationUtils.loadLayoutAnimation(
                    context,
                    R.anim.anim_layout_animation_fall_down
                )
            recyclerView.layoutAnimation = animation
            recyclerView.scheduleLayoutAnimation()
        }
    }
}