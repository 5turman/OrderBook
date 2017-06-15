package com.github.a5turman.orderbook.data

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * Created by 5turman on 6/14/2017.
 */
class PriceFactoryTest {

    @Test
    fun invalid() {
        assertNull(PriceFactory.build(""))
        assertNull(PriceFactory.build("abc"))
        assertNull(PriceFactory.build("146,8"))
        assertNull(PriceFactory.build("111222333444555.8"))
    }

    @Test
    fun noRealPart() {
        val price = PriceFactory.build("147", 3)

        assertNotNull(price)

        assertThat(price!!.integer, `is`(147))
        assertThat(price.real, `is`(0))
    }

    @Test
    fun shortRealPart() {
        arrayOf("147.3", "147.30").forEach { str ->
            val price = PriceFactory.build(str, 3)

            assertNotNull(price)

            assertThat(price!!.integer, `is`(147))
            assertThat(price.real, `is`(300))
        }
    }

    @Test
    fun fullRealPart() {
        val price = PriceFactory.build("147.352", 3)

        assertNotNull(price)

        assertThat(price!!.integer, `is`(147))
        assertThat(price.real, `is`(352))
    }

    @Test
    fun bigRealPart() {
        val price = PriceFactory.build("147.35289", 3)

        assertNotNull(price)

        assertThat(price!!.integer, `is`(147))
        assertThat(price.real, `is`(352))
    }

}
