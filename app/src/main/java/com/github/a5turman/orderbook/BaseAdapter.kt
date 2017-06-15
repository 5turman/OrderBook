package com.github.a5turman.orderbook

import android.support.v7.widget.RecyclerView

/**
 * Created by 5turman on 22.03.2017.
 */
abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    private val values = mutableListOf<T>()

    override fun getItemCount(): Int = values.size

    fun getItems(): List<T> = values

    fun getItem(position: Int) = values[position]

    fun setItem(position: Int, item: T) {
        values[position] = item
        notifyItemChanged(position)
    }

    fun setItems(items: Collection<T>) {
        values.clear()
        values.addAll(items)
        notifyDataSetChanged()
    }

    fun add(item: T) {
        values.add(item)
        notifyItemInserted(values.size - 1)
    }

    fun add(position: Int, item: T) {
        values.add(position, item)
        notifyItemInserted(position)
    }

    fun remove(position: Int) {
        values.removeAt(position)
        notifyItemRemoved(position)
    }

}
