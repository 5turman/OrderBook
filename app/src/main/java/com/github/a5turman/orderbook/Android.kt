package com.github.a5turman.orderbook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by 5turman on 6/15/2017.
 */
fun ViewGroup.inflate(resource: Int, attachToRoot: Boolean = false): View =
        LayoutInflater.from(this.context).inflate(resource, this, attachToRoot)

@Suppress("UNCHECKED_CAST")
fun <T> View.find(id: Int) = findViewById(id) as T
