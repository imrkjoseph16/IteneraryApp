package com.example.iteneraryapplication.app.shared.dto.layout

import android.content.Context
import androidx.annotation.DrawableRes
import com.example.iteneraryapplication.app.shared.component.TextLine

/**
 * Reusable component for empty list item on the screen.
 *
 * Describes data rendered in [com.example.iteneraryapplication.R.layout.shared_empty_list_item]
 * */
data class EmptyItemViewDto(
    @DrawableRes var imageResource: Int,
    var itemEmptyTitle: TextLine,
) {

    fun getItemEmptyTitle(context: Context) = itemEmptyTitle.getString(context)
}