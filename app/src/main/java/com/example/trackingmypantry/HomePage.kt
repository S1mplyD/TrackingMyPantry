package com.example.trackingmypantry

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import okhttp3.*
import java.io.IOException

class HomePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        val accessToken = intent.getStringExtra("accessToken").toString()
        //val btn = findViewById<Button>(R.id.localproductsButton)
        //btn.setOnClickListener { val i = Intent(this, ) }
        val getProducts: Button = findViewById(R.id.getproductsButton)
        getProducts.setOnClickListener {
            val i = Intent(this,GetProducts::class.java)
            i.putExtra("accessToken",accessToken)
            startActivity(i)
        }
    }
}