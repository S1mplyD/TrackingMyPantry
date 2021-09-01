package com.example.trackingmypantry

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HomePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        val getProducts: Button = findViewById(R.id.getproductsButton)
        getProducts.setOnClickListener {
            val i = Intent(this,GetProducts::class.java)
            startActivity(i)
        }

        val showLocalProducts : Button = findViewById(R.id.localproductsButton)
        showLocalProducts.setOnClickListener {
            val i = Intent(this,LocalProducts::class.java)
            startActivity(i)
        }


    }
}