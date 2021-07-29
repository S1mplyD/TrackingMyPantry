package com.example.trackingmypantry

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class HomePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        val btn = findViewById<Button>(R.id.localproductsButton)
        //btn.setOnClickListener { val i = Intent(this, ) }
    }
}