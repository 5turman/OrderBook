package com.github.a5turman.orderbook.data

/**
 * Created by 5turman on 6/14/2017.
 */
object PriceFactory {

    fun build(str: String, realSize: Int = 2): Price? {
        val index = str.indexOf('.')
        if (index >= 0) {
            val integer = str.substring(0, index).toIntOrNull()
            if (integer != null) {
                var real = str.substring(index + 1)
                if (real.length < realSize) {
                    repeat(realSize - real.length) {
                        real += '0'
                    }
                } else if (real.length > realSize) {
                    real = real.substring(0, realSize)
                }
                return Price(integer, real.toInt())
            }
        }

        val integer = str.toIntOrNull()
        return if (integer != null) Price(integer, 0) else null
    }

}
