package com.example.trackingmypantry.remote_database

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.ProductData
import com.example.trackingmypantry.R

class ProductResult : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_result)
        //Prendo i valori passati tramite intent
        val products = intent.getSerializableExtra("products")
        val token = intent.getStringExtra("token")
        //Creo la recycler view per i prodotti
        val recView: RecyclerView = findViewById(R.id.productRecyclerView)
        recView.layoutManager = LinearLayoutManager(this)
        recView.adapter =
            ProductAdapter(products as ArrayList<ProductData>, token, supportFragmentManager)
    }
}