package com.github.a5turman.orderbook.engine

import com.github.a5turman.orderbook.data.Ask
import com.github.a5turman.orderbook.data.Bid
import java.util.LinkedList

/**
 * Created by 5turman on 6/13/2017.
 */
class OrderBook {
    /*
     * The bids are sorted by price in descending order
     */
    private val bids = LinkedList<Bid>()
    /*
     * The asks are sorted by price in ascending order
     */
    private val asks = LinkedList<Ask>()

    fun add(bid: Bid) {
        if (bids.isEmpty()) {
            bids.add(bid)
        } else {
            val price = bid.price
            val iterator = bids.listIterator()
            while (iterator.hasNext()) {
                val current = iterator.next()
                if (price > current.price) {
                    iterator.previous()
                    iterator.add(bid)
                    return
                } else if (price == current.price) {
                    iterator.set(Bid(price, current.count + bid.count))
                    return
                }
            }
            bids.add(bid)
        }
    }

    fun add(ask: Ask) {
        if (asks.isEmpty()) {
            asks.add(ask)
        } else {
            val price = ask.price
            val iterator = asks.listIterator()
            while (iterator.hasNext()) {
                val current = iterator.next()
                if (price < current.price) {
                    iterator.previous()
                    iterator.add(ask)
                    return
                } else if (price == current.price) {
                    iterator.set(Ask(price, current.count + ask.count))
                    return
                }
            }
            asks.add(ask)
        }
    }

    fun getBids(): List<Bid> {
        return bids.toList()
    }

    fun getAsks(): List<Ask> {
        return asks.toList()
    }

    fun match() {
        while (bids.isNotEmpty() && asks.isNotEmpty()) {
            val bid = bids.first
            val ask = asks.first

            if (ask.price <= bid.price) {
                bids.removeFirst()
                asks.removeFirst()

                val minCount = Math.min(bid.count, ask.count)
                if (bid.count != minCount) {
                    bids.addFirst(Bid(bid.price, bid.count - minCount))
                }

                if (ask.count != minCount) {
                    asks.addFirst(Ask(ask.price, ask.count - minCount))
                }
            } else {
                break
            }
        }
    }

}
