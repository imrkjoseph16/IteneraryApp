package com.example.iteneraryapplication.app.widget

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.iteneraryapplication.app.widget.CustomRecyclerView.Companion.calculateItemViewType
import java.lang.IllegalStateException

open class ViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

class CustomRecyclerViewAdapter : RecyclerView.Adapter<ViewHolder>() {

    private val items: MutableList<Any> = ArrayList()
    val bindings: HashMap<Int, RecyclerViewHolder<ViewBinding, Any>> = hashMapOf()
    private var itemsDiffCallBack: ItemsDiffCallback? = null

    private fun getBinding(viewType: Int): RecyclerViewHolder<ViewBinding, Any> {
        return bindings[viewType] ?: run {
            val itemType = items.first { calculateItemViewType(it::class) == viewType }::class.simpleName
            throw IllegalStateException("Have you forgotten to add the recyclerView item binding for the item type $itemType ?")
        }
    }

    fun setDiffUtilCallBack(diffUtilCallback: ItemsDiffCallback) {
        this.itemsDiffCallBack = diffUtilCallback
    }

    /**
     * This method will update the new data items in the existing list of adapter items using [DiffUtil]
     * */

    fun updateItems(items: List<Any>) {
        itemsDiffCallBack?.let {
            val diffCallback = it.apply {
                oldItems = this@CustomRecyclerViewAdapter.items
                newItems = items
            }

            val diffResult = DiffUtil.calculateDiff(diffCallback)
            this.items.clear()
            this.items.addAll(items)
            diffResult.dispatchUpdatesTo(this)
        } ?: run {
            this.items.clear()
            this.items.addAll(items)
            notifyDataSetChanged()
        }
    }

    fun getItemAt(position: Int): Any? = items.getOrNull(position)

    /**
     * To set/reset new data items in the adapter
     * */

    fun setDataItems(items: List<Any>) {
        this.items.apply {
            clear()
            addAll(items)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(getBinding(viewType).inflate(parent))

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) =
        if (payloads.isEmpty()) onBindViewHolder(holder, position)
        else getBinding(calculateItemViewType(items[position]::class)).bind(holder.binding, items[position], payloads)

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = calculateItemViewType(items[position]::class)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = getBinding(calculateItemViewType(items[position]::class)).bind(holder.binding, items[position])
}