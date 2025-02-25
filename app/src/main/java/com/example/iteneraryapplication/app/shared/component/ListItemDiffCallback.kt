package com.example.iteneraryapplication.app.shared.component

class ListItemDiffCallback : ItemsDiffCallback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition] as? ListItem
        val newItem = newItems[newItemPosition] as? ListItem

        return when {
            oldItem != null && newItem != null -> oldItem.id == newItem.id
            else -> super.areItemsTheSame(oldItemPosition, newItemPosition)
        }
    }
}

interface ListItem {
    val id: Any
}