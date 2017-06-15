package com.github.a5turman.orderbook.engine

import com.github.a5turman.orderbook.data.Ask
import com.github.a5turman.orderbook.data.Bid

/**
 * Created by 5turman on 6/13/2017.
 */
class Engine {

    interface Callback {
        fun onChange(bids: List<Bid>, asks: List<Ask>)
    }

    private val orderBook = OrderBook()

    private var callback: Callback? = null

    fun add(bid: Bid) {
        orderBook.add(bid)
        orderBook.match()

        callback?.onChange(orderBook.getBids(), orderBook.getAsks())
    }

    fun add(ask: Ask) {
        orderBook.add(ask)
        orderBook.match()

        callback?.onChange(orderBook.getBids(), orderBook.getAsks())
    }

    fun register(callback: Callback) {
        this.callback = callback
    }

    fun unregister() {
        this.callback = null
    }

}
