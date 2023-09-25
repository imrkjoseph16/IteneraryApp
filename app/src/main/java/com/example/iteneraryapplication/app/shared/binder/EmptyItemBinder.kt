package com.example.iteneraryapplication.app.shared.binder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.iteneraryapplication.app.shared.dto.layout.EmptyItemViewDto
import com.example.iteneraryapplication.app.widget.RecyclerViewHolder
import com.example.iteneraryapplication.databinding.SharedEmptyListItemBinding

val EmptyItemBinder = object :
    RecyclerViewHolder<SharedEmptyListItemBinding, EmptyItemViewDto> {
    override fun bind(binder: SharedEmptyListItemBinding, item: EmptyItemViewDto) {
        with(binder) {
            data = item
            executePendingBindings()
        }
    }

    override fun inflate(parent: ViewGroup) = SharedEmptyListItemBinding.inflate(
        LayoutInflater.from(parent.context), parent, false)
}