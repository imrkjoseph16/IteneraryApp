package com.example.iteneraryapplication.app.shared.component

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kotlin.reflect.KClass

class CustomRecyclerView(context: Context, attrs: AttributeSet?) : RecyclerView(context, attrs) {
    val recyclerViewAdapter = CustomRecyclerViewAdapter()
    
    init {
        adapter = recyclerViewAdapter
    }

    inline fun <B : ViewBinding, reified I : Any> addItemBindings(
        viewHolders: RecyclerViewHolder<B, I>
    ) {
        @Suppress("UNCHECKED_CAST")
        recyclerViewAdapter.bindings[calculateItemViewType(I::class)] = viewHolders as RecyclerViewHolder<ViewBinding, Any>
    }

    fun setItems(items: List<Any>?) = recyclerViewAdapter.updateItems(items ?: emptyList())

    companion object {
        fun calculateItemViewType(clazz: KClass<*>): Int = clazz.hashCode()
    }
}