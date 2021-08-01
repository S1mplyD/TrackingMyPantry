package com.example.trackingmypantry

import android.content.ClipDescription
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException

class GetProducts : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_products)
        val accessToken = intent.getStringExtra("accessToken").toString()
        val barcodeText: EditText = findViewById(R.id.barcode)
        val barcode = barcodeText.text
        val submitBarcode: Button = findViewById(R.id.submitBarcode)
        submitBarcode.setOnClickListener {
            if (barcode.isEmpty()) {
                Toast.makeText(this, "Empty barcode field", Toast.LENGTH_SHORT).show()
            } else {
                val apiUrl = "https://lam21.modron.network/products?barcode="
                println(accessToken)
                val client = OkHttpClient()
                val request = Request.Builder()
                    .addHeader("Authorization", "Bearer $accessToken")
                    .url(apiUrl + barcode)
                    .build()
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.d("Error", "failed to execute")
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val responseBody = response.body?.string()
                        val gson = GsonBuilder().create()
                        val products = gson.fromJson(responseBody, Product::class.java)
                        if (response.isSuccessful) {
                            if (products.products.size > 0) {
                                val i = Intent(this@GetProducts, ProductResult::class.java)
                                i.putExtra("productName", products.products[0].name)
                                i.putExtra("productDescription", products.products[0].description)
                                startActivity(i)
                            } else {
                                runOnUiThread {
                                    Toast.makeText(
                                        this@GetProducts,
                                        "No products found",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                        }
                    }

                })
            }
        }


    }
}

class Product(val products: ArrayList<ProductData>)
class ProductData(val name: String, val description: String)