package com.example.iteneraryapplication.app.shared.dto.layout

import androidx.annotation.DimenRes

/**
 * Reusable component for empty space on the screen
 *
 * Describes data rendered in [com.example.iteneraryapplication.R.layout.shared_simple_list_space_item]
 * */
data class SpaceItemViewDto(
    @DimenRes val heightDimenRes: Int
)