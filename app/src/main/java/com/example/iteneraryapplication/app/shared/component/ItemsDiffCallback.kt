package com.example.iteneraryapplication.app.shared.component

import androidx.recyclerview.widget.DiffUtil

open class ItemsDiffCallback : DiffUtil.Callback() {

    var oldItems: List<Any> = emptyList()

    var newItems: List<Any> = emptyList()

    override fun getOldListSize() = oldItems.size

    override fun getNewListSize() = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldItems[oldItemPosition] == newItems[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldItems[oldItemPosition] == newItems[newItemPosition]
}