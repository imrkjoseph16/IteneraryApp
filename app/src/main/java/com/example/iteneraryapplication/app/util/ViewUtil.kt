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

    companion object {
        fun generateRandomCharacters() : String {
            // Descriptive alphabet using three CharRange objects, concatenated
            val alphabet: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

            // Build list from 20 random samples from the alphabet,
            // and convert it to a string using "" as element separator
            return List(20) { alphabet.random() }.joinToString("")
        }
    }
}