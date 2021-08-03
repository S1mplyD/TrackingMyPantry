package com.example.trackingmypantry

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HomePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        //val btn = findViewById<Button>(R.id.localproductsButton)
        //btn.setOnClickListener { val i = Intent(this, ) }
        val getProducts: Button = findViewById(R.id.getproductsButton)
        getProducts.setOnClickListener {
            val i = Intent(this,GetProducts::class.java)
            startActivity(i)
        }
    }
}