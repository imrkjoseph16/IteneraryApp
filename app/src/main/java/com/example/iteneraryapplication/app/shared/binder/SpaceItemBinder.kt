package com.example.iteneraryapplication.app.shared.binder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.iteneraryapplication.app.shared.dto.layout.SpaceItemViewDto
import com.example.iteneraryapplication.app.widget.RecyclerViewHolder
import com.example.iteneraryapplication.databinding.SharedSimpleListSpaceItemBinding

val SpaceItemViewDtoBinder = object :
    RecyclerViewHolder<SharedSimpleListSpaceItemBinding, SpaceItemViewDto> {
    override fun bind(binder: SharedSimpleListSpaceItemBinding, item: SpaceItemViewDto) {
        with(binder) {
            data = item
            executePendingBindings()
        }
    }

    override fun inflate(parent: ViewGroup) = SharedSimpleListSpaceItemBinding.inflate(
        LayoutInflater.from(parent.context), parent, false)
}