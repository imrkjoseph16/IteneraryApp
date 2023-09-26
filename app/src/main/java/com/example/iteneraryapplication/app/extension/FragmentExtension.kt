package com.example.iteneraryapplication.app.extension

import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.Fragment

fun Fragment.showPopupMenu(onMenuItemClick: (itemId: Int) -> Unit, rootView: View) {
    val popupMenu = PopupMenu(this.context, rootView)

    popupMenu.menuInflater.inflate(com.example.iteneraryapplication.R.menu.pop_up_item_menu, popupMenu.menu)
    popupMenu.setOnMenuItemClickListener {
        onMenuItemClick(it.itemId)
        true
    }
    popupMenu.show()
}