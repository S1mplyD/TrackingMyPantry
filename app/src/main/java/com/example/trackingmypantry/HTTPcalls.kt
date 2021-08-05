package com.example.trackingmypantry

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Looper
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.trackingmypantry.LoginPage.Companion.accessToken
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class HTTPcalls {
    fun getProducts(barcode: String, context: Context) {  //API GET PRODUCTS function
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
                        val bundle = ActivityOptions.makeBasic().toBundle()
                        startActivity(context, i, bundle)
                    } else {
                        val looper = Looper.prepare()
                        looper.run {
                            Toast.makeText(
                                context,
                                "No products found",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        val i = Intent(context, PostProductDetails::class.java)
                        i.putExtra("token", products.token)
                        i.putExtra("barcode", barcode)
                        val bundle = ActivityOptions.makeBasic().toBundle()
                        startActivity(context, i, bundle)
                    }
                }
            }
        })
    }

    fun postProductsDetails(token: String, name: Editable, description: Editable, barcode: String) {
        val apiUrl = "https://lam21.modron.network/products"
        val client = OkHttpClient()
        val jsonObject = JSONObject()
        jsonObject.put("token", token)
        jsonObject.put("name", name)
        jsonObject.put("description", description)
        jsonObject.put("barcode", barcode)
        jsonObject.put("test", true)
        val body = jsonObject.toString()
            .toRequestBody("application/json;charset=utf-8".toMediaTypeOrNull())

        val request =
            Request.Builder().addHeader("Authorization", "Bearer $accessToken").url(apiUrl)
                .post(body).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Error", "Failed to execute")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                println(responseBody)
            }

        })
    }

    fun postProductPreference() {

    }

    fun delete(id: String) {
        val apiUrl = "https://lam21.modron.network/products/"
        val client = OkHttpClient()
        val request =
            Request.Builder().addHeader("Authorization", "Bearer $accessToken").url(apiUrl + id)
                .delete().build()
        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Error", "Failed to execute request")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                println(responseBody)
            }

        })
    }

}

class Product(val products: ArrayList<ProductData>, val token: String)
class ProductData(val name: String, val description: String)