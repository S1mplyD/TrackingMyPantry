package com.example.trackingmypantry

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProductResult : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_result)

         val recView : RecyclerView = findViewById(R.id.productRecyclerView)
        recView.layoutManager = LinearLayoutManager(this)
        recView.adapter = ProductAdapter()
    }

}