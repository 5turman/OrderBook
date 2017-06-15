package com.github.a5turman.orderbook

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.a5turman.orderbook.data.Bid
import com.github.a5turman.orderbook.data.Order

/**
 * Created by 5turman on 6/14/2017.
 */
class OrdersAdapter : BaseAdapter<Order, OrdersAdapter.ViewHolder>() {

    override fun getItemViewType(position: Int) =
            if (getItem(position) is Bid) R.layout.item_bid_order else R.layout.item_ask_order

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(viewType))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val order = getItem(position)
        viewHolder.price.text = order.price.toString()
        viewHolder.count.text = order.count.toString()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val price = itemView.find<TextView>(R.id.price)
        val count = itemView.find<TextView>(R.id.count)
    }

}
