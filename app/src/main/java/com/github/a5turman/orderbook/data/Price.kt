package com.github.a5turman.orderbook.data

/**
 * Created by 5turman on 6/13/2017.
 */
data class Price(val integer: Int, val real: Int) : Comparable<Price> {

    override fun toString() =
            integer.toString() + "." + String.format("%-2d", real).replace(' ', '0')

    override fun compareTo(other: Price): Int {
        if (integer < other.integer) return -1

        if (integer > other.integer) return 1

        if (real < other.real) return -1

        if (real > other.real) return 1

        return 0
    }

}
