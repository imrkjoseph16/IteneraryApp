package com.example.iteneraryapplication.app.shared.binder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.iteneraryapplication.app.shared.dto.layout.NoteItemViewDto
import com.example.iteneraryapplication.app.widget.RecyclerViewHolder
import com.example.iteneraryapplication.databinding.SharedListNoteItemBinding

inline fun <reified T : Any> getListNoteItemBinder(
    crossinline dtoReceiverCard: (T) -> NoteItemViewDto,
    crossinline onItemClick: (SharedListNoteItemBinding, NoteItemViewDto) -> Unit = { _: SharedListNoteItemBinding, _: NoteItemViewDto -> },
) = object : RecyclerViewHolder<SharedListNoteItemBinding, T> {

    override fun bind(binder: SharedListNoteItemBinding, item: T) {
        with(binder) {
            val dto = dtoReceiverCard(item)
            data = dto
            root.setOnClickListener { onItemClick(binder, dto) }
            executePendingBindings()
        }
    }

    override fun inflate(parent: ViewGroup) = SharedListNoteItemBinding.inflate(
        LayoutInflater.from(parent.context), parent, false)
}