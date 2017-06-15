package com.github.a5turman.orderbook

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import com.github.a5turman.orderbook.data.Ask
import com.github.a5turman.orderbook.data.Bid
import com.github.a5turman.orderbook.data.Order
import com.github.a5turman.orderbook.data.PriceFactory
import com.github.a5turman.orderbook.engine.Engine
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), Engine.Callback {

    private lateinit var engine: Engine
    private lateinit var adapter: OrdersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        engine = (lastCustomNonConfigurationInstance as Engine?) ?: Engine()
        adapter = OrdersAdapter()

        setContentView(R.layout.activity_main)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
            setHasFixedSize(true)
        }

        buyButton.setOnClickListener {
            val price = getPrice()
            val count = getCount()
            if (price != null && count != null) {
                engine.add(Bid(price, count))
            }
        }
        sellButton.setOnClickListener {
            val price = getPrice()
            val count = getCount()
            if (price != null && count != null) {
                engine.add(Ask(price, count))
            }
        }
    }

    override fun onRetainCustomNonConfigurationInstance() = engine

    override fun onResume() {
        super.onResume()
        engine.register(this)
    }

    override fun onPause() {
        engine.unregister()
        super.onPause()
    }

    override fun onChange(bids: List<Bid>, asks: List<Ask>) {
        val orders = mutableListOf<Order>()
        orders.addAll(asks.reversed())
        orders.addAll(bids)
        adapter.setItems(orders)
    }

    private fun getPrice() = price.text.run {
        if (!TextUtils.isEmpty(this)) PriceFactory.build(this.toString()) else null
    }

    private fun getCount() = count.text.run {
        if (!TextUtils.isEmpty(this)) toString().toIntOrNull() else null
    }

}
