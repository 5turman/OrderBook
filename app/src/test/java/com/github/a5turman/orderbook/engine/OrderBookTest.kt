package com.github.a5turman.orderbook.engine

import com.github.a5turman.orderbook.data.Ask
import com.github.a5turman.orderbook.data.Bid
import com.github.a5turman.orderbook.data.PriceFactory
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matchers.empty
import org.hamcrest.Matchers.hasSize
import org.junit.Assert.assertThat
import org.junit.Test

/**
 * Created by 5turman on 6/13/2017.
 */
class OrderBookTest {

    private val book = OrderBook()

    @Test
    fun addBid() {
        book.add(bid("147.1", 10))
        book.add(bid("147.10", 3))
        book.add(bid("147.109", 5))

        val bids = book.getBids()
        assertThat(bids, hasSize(1))

        val bid = bids[0]
        assertThat(bid.price.toString(), `is`("147.10"))
        assertThat(bid.count, `is`(18))
    }

    @Test
    fun bidsAreOrderedByDesc() {
        book.add(bid("147.15", 5))
        book.add(bid("147.0", 10))
        book.add(bid("147.30", 3))

        val bids = book.getBids()
        assertThat(bids, hasSize(3))

        assertThat(bids.map { it.price.toString() }, `is`(listOf("147.30", "147.15", "147.00")))
    }

    @Test
    fun addAsk() {
        book.add(ask("147.3", 5))
        book.add(ask("147.30", 4))
        book.add(ask("147.301", 3))

        val asks = book.getAsks()
        assertThat(asks, hasSize(1))

        val ask = asks[0]
        assertThat(ask.price.toString(), `is`("147.30"))
        assertThat(ask.count, `is`(12))
    }

    @Test
    fun asksAreOrderedByAsc() {
        book.add(ask("147.15", 3))
        book.add(ask("147.30", 3))
        book.add(ask("147.0", 10))

        val asks = book.getAsks()
        assertThat(asks, hasSize(3))

        assertThat(asks.map { it.price.toString() }, `is`(listOf("147.00", "147.15", "147.30")))
    }

    @Test
    fun matching_orderBookIsEmpty() {
        book.match()

        assertThat(book.getBids(), empty())
        assertThat(book.getAsks(), empty())

        book.add(bid("147.10", 3))
    }

    @Test
    fun matching_noMatches() {
        book.add(bid("147.10", 3))
        book.add(bid("147.15", 5))

        book.add(ask("147.20", 5))
        book.add(ask("147.30", 10))

        book.match()

        book.getBids().let {
            assertThat(it, hasSize(2))
            assertThat(it[0].price.toString(), `is`("147.15"))
            assertThat(it[0].count, `is`(5))
            assertThat(it[1].price.toString(), `is`("147.10"))
            assertThat(it[1].count, `is`(3))
        }

        book.getAsks().let {
            assertThat(it, hasSize(2))
            assertThat(it[0].price.toString(), `is`("147.20"))
            assertThat(it[0].count, `is`(5))
            assertThat(it[1].price.toString(), `is`("147.30"))
            assertThat(it[1].count, `is`(10))
        }
    }

    @Test
    fun matching_full() {
        book.add(bid("147.10", 3))
        book.add(bid("147.15", 5))

        book.add(ask("147.15", 5))
        book.add(ask("147.30", 10))

        book.match()

        book.getBids().let {
            assertThat(it, hasSize(1))
            assertThat(it[0].price.toString(), `is`("147.10"))
        }

        book.getAsks().let {
            assertThat(it, hasSize(1))
            assertThat(it[0].price.toString(), `is`("147.30"))
        }
    }

    @Test
    fun matching_bidCountIsLessThanAskCount() {
        book.add(bid("147.15", 3))
        book.add(ask("147.15", 5))

        book.match()

        assertThat(book.getBids(), empty())

        book.getAsks().let {
            assertThat(it, hasSize(1))
            assertThat(it[0].count, `is`(2))
        }
    }

    @Test
    fun matching_bidCountIsMoreThanAskCount() {
        book.add(bid("147.15", 8))
        book.add(ask("147.15", 5))

        book.match()

        book.getBids().let {
            assertThat(it, hasSize(1))
            assertThat(it[0].count, `is`(3))
        }

        assertThat(book.getAsks(), empty())
    }

    @Test
    fun matching_bidPriceIsMoreThanAskPrice() {
        book.add(bid("147.15", 3))
        book.add(ask("147.10", 5))

        book.match()

        assertThat(book.getBids(), empty())

        book.getAsks().let {
            assertThat(it, hasSize(1))
            assertThat(it[0].count, `is`(2))
        }
    }

    @Test
    fun matching_multipleBidMatches() {
        book.add(bid("147.12", 3))
        book.add(bid("147.15", 4))

        book.add(ask("147.10", 8))

        book.match()

        assertThat(book.getBids(), empty())

        book.getAsks().let {
            assertThat(it, hasSize(1))
            assertThat(it[0].count, `is`(1))
        }
    }

    @Test
    fun matching_multipleAskMatches() {
        book.add(bid("147.15", 5))

        book.add(ask("147.10", 3))
        book.add(ask("147.13", 4))

        book.match()

        assertThat(book.getBids(), empty())

        book.getAsks().let {
            assertThat(it, hasSize(1))
            assertThat(it[0].price.toString(), `is`("147.13"))
            assertThat(it[0].count, `is`(2))
        }
    }

    @Test
    fun matching_timeline() {
        book.add(ask("128.50", 20))
        book.match() // +20

        book.add(bid("128.50", 30))
        book.match() // -10

        book.add(ask("128.50", 15))
        book.match() // +5

        book.add(bid("128.50", 5))
        book.match() // 0

        assertThat(book.getBids(), empty())
        assertThat(book.getAsks(), empty())
    }

    fun bid(price: String, count: Int): Bid {
        return Bid(PriceFactory.build(price)!!, count)
    }

    fun ask(price: String, count: Int): Ask {
        return Ask(PriceFactory.build(price)!!, count)
    }

}
