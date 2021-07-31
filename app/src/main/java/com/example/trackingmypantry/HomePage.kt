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
        //val btn = findViewById<Button>(R.id.localproductsButton)
        //btn.setOnClickListener { val i = Intent(this, ) }
        val getProducts: Button = findViewById(R.id.getproductsButton)
        getProducts.setOnClickListener {
            //TODO: create another activity to insert barcode or to scan it via camera
            val apiUrl = "https://lam21.modron.network/products?barcode="
            val barcode = "0000000000000"
            val accessToken = intent.getStringExtra("accessToken").toString()
            println(accessToken)
            val client = OkHttpClient()
            val request = Request.Builder()
                    .addHeader("Authorization", "Bearer $accessToken")
                    .url(apiUrl + barcode)
                    .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("Error","failed to execute")
                }

                override fun onResponse(call: Call, response: Response) {
                    val responseBody = response.body?.string()
                    println(responseBody)
                }

            })
        }
    }
}