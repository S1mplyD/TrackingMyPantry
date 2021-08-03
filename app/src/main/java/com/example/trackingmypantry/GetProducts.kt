package com.example.trackingmypantry

import android.content.ClipDescription
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException
import com.example.trackingmypantry.HTTPcalls
import com.example.trackingmypantry.LoginPage.Companion.accessToken

class GetProducts : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_products)
        val barcodeText: EditText = findViewById(R.id.barcode)
        val barcode = barcodeText.text.toString()
        val submitBarcode: Button = findViewById(R.id.submitBarcode)
        val scanner : Button = findViewById(R.id.scanButton)
        scanner.setOnClickListener {
            val i = Intent(this,BarcodeScanner::class.java)
            startActivity(i)
        }
        submitBarcode.setOnClickListener {
            if (barcode.isEmpty()) {
                Toast.makeText(this, "Empty barcode field", Toast.LENGTH_SHORT).show()
            } else {
                //println(accessToken)
                val httpCalls = HTTPcalls()
                httpCalls.GETPRODUCTS(barcode,this@GetProducts)
            }
        }
runOnUiThread {  }

    }
}

