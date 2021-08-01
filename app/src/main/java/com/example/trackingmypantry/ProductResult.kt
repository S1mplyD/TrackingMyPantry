package com.example.trackingmypantry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ProductResult : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_result)
        val productName : TextView = findViewById(R.id.productName)
        val productDescription : TextView = findViewById(R.id.productDescription)
        val name = intent.getStringExtra("productName").toString()
        val description = intent.getStringExtra("productDescription").toString()
        productName.text = name
        productDescription.text = description
    }
}