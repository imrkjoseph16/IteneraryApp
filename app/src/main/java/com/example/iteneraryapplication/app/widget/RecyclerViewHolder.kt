package com.example.iteneraryapplication.app.widget

import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

interface RecyclerViewHolder<T : ViewBinding, I : Any> {

    fun bind(binder: T, item: I)

    fun bind(binder: T, item: I, payload: List<Any?>) {}

    fun inflate(parent: ViewGroup): T
}