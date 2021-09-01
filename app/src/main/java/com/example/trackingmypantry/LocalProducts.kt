package com.example.trackingmypantry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.database.ProductViewModel

class LocalProducts : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_products)
        val mviewmodel: ProductViewModel
        val recview: RecyclerView = findViewById(R.id.localProductsRecView)
        val adapter = LocalProductsAdapter()
        recview.layoutManager = LinearLayoutManager(this)
        recview.adapter = adapter
        mviewmodel = ViewModelProvider(this).get(ProductViewModel::class.java)
        mviewmodel.allProducts.observe(this, Observer { product ->
            adapter.setData(product)
        })
    }

}