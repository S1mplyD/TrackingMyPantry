package com.example.trackingmypantry

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.trackingmypantry.LoginPage.Companion.accessToken
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException

class HTTPcalls {
    fun GETPRODUCTS(barcode : String,context : Context){  //API GET PRODUCTS function
        val apiUrl = "https://lam21.modron.network/products?barcode="
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
                        val i = Intent(context, ProductResult::class.java)
                        i.putExtra("productName", products.products[0].name)
                        i.putExtra("productDescription", products.products[0].description)
                        val ciao = ActivityOptions.makeBasic().toBundle()
                        startActivity(context,i,ciao)
                    } else {
                        val looper = Looper.prepare()
                        looper.run { Toast.makeText(
                            context,
                            "No products found",
                            Toast.LENGTH_SHORT
                        ).show()  }
                    }

                }
            }

        })

    }
}

class Product(val products: ArrayList<ProductData>)
class ProductData(val name: String, val description: String)