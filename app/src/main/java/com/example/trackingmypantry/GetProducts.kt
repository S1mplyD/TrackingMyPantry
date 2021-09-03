package com.example.trackingmypantry

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import okhttp3.*

class GetProducts : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_products)
        val barcodeText: EditText = findViewById(R.id.barcode)
        val barcode = barcodeText.text
        val submitBarcode: Button = findViewById(R.id.submitBarcode)
        val scanner : Button = findViewById(R.id.scanButton)
        scanner.setOnClickListener {
            val i = Intent(this,BarcodeScanner::class.java)
            startActivity(i)
        }
        submitBarcode.setOnClickListener {
            if (barcode.isNullOrBlank()) {
                Toast.makeText(this, "Empty barcode field", Toast.LENGTH_SHORT).show()
            } else {
                val httpCalls = HTTPcalls()
                httpCalls.getProducts(barcode.toString(),this@GetProducts,supportFragmentManager)
            }
        }

    }
}

