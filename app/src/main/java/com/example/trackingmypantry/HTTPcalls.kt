package com.example.trackingmypantry

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Looper
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentManager
import com.example.trackingmypantry.LoginPage.Companion.accessToken
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.io.Serializable

class HTTPcalls {


    fun getProducts(
        barcode: String,
        context: Context,
        supportFragmentManager: FragmentManager
    ) {  //API GET PRODUCTS function

        val apiUrl = "https://lam21.iot-prism-lab.cs.unibo.it/products?barcode="
        val client = OkHttpClient()
        val request = Request.Builder()
            .addHeader("Authorization", "Bearer $accessToken")
            .url(apiUrl + barcode)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Error", "failed to execute")
                Looper.prepare().run {
                    Toast.makeText(
                        context,
                        "Please check your internet connection",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                println(responseBody)
                val gson = GsonBuilder().create()
                val products = gson.fromJson(responseBody, Products::class.java)
                if (response.code == 401) {
                    val sharedPreferences: SharedPreferences =
                        context.getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putBoolean("CHECKBOX", false)
                    editor.apply()
                    Looper.prepare().run {
                        Toast.makeText(
                            context,
                            "Access token expired, please log in again",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    val i = Intent(context, LoginPage::class.java)
                    val options = ActivityOptions.makeBasic().toBundle()
                    startActivity(context, i, options)
                } else if (response.isSuccessful) {
                    if (products.products.size > 0) {
                        val i = Intent(context, ProductResult::class.java)
                        i.putExtra("products", products.products)
                        i.putExtra("token", products.token)
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
                        val dialog = PostProductDetails(products.token, barcode)
                        dialog.show(supportFragmentManager, "PostProductDetails")
                    }


                }
            }
        })
    }

    fun postProductsDetails(
        token: String?,
        name: String,
        description: String,
        barcode: String,
        context: Context
    ) {
        val apiUrl = "https://lam21.iot-prism-lab.cs.unibo.it/products"
        val client = OkHttpClient()
        val jsonObject = JSONObject()
        jsonObject.put("token", token)
        jsonObject.put("name", name)
        jsonObject.put("description", description)
        jsonObject.put("barcode", barcode)
        jsonObject.put("test", false)
        val body = jsonObject.toString()
            .toRequestBody("application/json;charset=utf-8".toMediaTypeOrNull())

        val request =
            Request.Builder().addHeader("Authorization", "Bearer $accessToken").url(apiUrl)
                .post(body).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Error", "Failed to execute")
                Looper.prepare().run {
                    Toast.makeText(
                        context,
                        "Please check your internet connection",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                println(responseBody)
                if (response.code == 401) {
                    val sharedPreferences: SharedPreferences =
                        context.getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putBoolean("CHECKBOX", false)
                    editor.apply()
                    Looper.prepare().run {
                        Toast.makeText(
                            context,
                            "Access token expired, please log in again",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    val i = Intent(context, LoginPage::class.java)
                    val options = ActivityOptions.makeBasic().toBundle()
                    startActivity(context, i, options)
                } else if (response.isSuccessful) {
                    Looper.prepare().run {
                        Toast.makeText(
                            context,
                            "Product added correctly",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })
    }

    fun postProductPreference(token: String?, rating: Int, productId: String?, context: Context) {
        if (productId.isNullOrBlank()) {
            Log.d("Error", "Invalid product ID")
        } else {
            val apiUrl = "https://lam21.iot-prism-lab.cs.unibo.it/votes"
            val client = OkHttpClient()
            val jsonObject = JSONObject()
            jsonObject.put("token", token)
            jsonObject.put("rating", rating)
            jsonObject.put("productId", productId)
            val body = jsonObject.toString()
                .toRequestBody("application/json;charset=utf-8".toMediaTypeOrNull())
            val request =
                Request.Builder().addHeader("Authorization", "Bearer $accessToken").url(apiUrl)
                    .post(body).build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("Error", "Failed to execute")
                    Looper.prepare().run {
                        Toast.makeText(
                            context,
                            "Please check your internet connection",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    val responseBody = response.body?.string()
                    println(responseBody)
                    if (response.code == 401) {
                        val sharedPreferences: SharedPreferences =
                            context.getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
                        val editor: SharedPreferences.Editor = sharedPreferences.edit()
                        editor.putBoolean("CHECKBOX", false)
                        editor.apply()
                        Toast.makeText(
                            context,
                            "Access token expired, please log in again",
                            Toast.LENGTH_SHORT
                        ).show()
                        val i = Intent(context, LoginPage::class.java)
                        val options = ActivityOptions.makeBasic().toBundle()
                        startActivity(context, i, options)
                    }


                    else if (response.isSuccessful) {
                        Looper.prepare().run {
                            Toast.makeText(
                                context,
                                "Product vote submitted",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }


                }

            })
        }

    }

    //    fun delete(id: String) {
//        val apiUrl = "https://lam21.modron.network/products/"
//        val client = OkHttpClient()
//        val request =
//            Request.Builder().addHeader("Authorization", "Bearer $accessToken").url(apiUrl + id)
//                .delete().build()
//        client.newCall(request).enqueue(object : Callback{
//            override fun onFailure(call: Call, e: IOException) {
//                Log.d("Error", "Failed to execute request")
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                val responseBody = response.body?.string()
//                println(responseBody)
//            }
//
//        })
//    }
    fun login(
        mail: Editable,
        password: Editable,
        context: Context,
        loginPage: LoginPage,
        checked: Boolean
    ) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)

        val jsonObject = JSONObject()
        jsonObject.put("email", mail)
        jsonObject.put("password", password)
        val body = jsonObject.toString()
            .toRequestBody("application/json;charset=utf-8".toMediaTypeOrNull())
        val client = OkHttpClient()
        val loginRequest = Request.Builder()
            .url("https://lam21.iot-prism-lab.cs.unibo.it/auth/login")
            .post(body)
            .build()
        client.newCall(loginRequest).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                val gson = GsonBuilder().create()
                val token = gson.fromJson(responseBody, AccessToken::class.java)
                if (response.isSuccessful) {
                    accessToken = token.accessToken
                    if (checked) {
                        val editor: SharedPreferences.Editor = sharedPreferences.edit()
                        editor.putBoolean("CHECKBOX", true)
                        editor.putString("ACCESS_TOKEN", accessToken)
                        editor.apply()
                    }
                    val i = Intent(context, HomePage::class.java)
                    val options = ActivityOptions.makeBasic().toBundle()
                    startActivity(context, i, options)
                    finishAffinity(loginPage)
                } else {
                    Log.d("Error", "Wrong data")
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Looper.prepare().run {
                    Toast.makeText(
                        context,
                        "Please check your internet connection",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    fun register(
        name: Editable,
        mail: Editable,
        password: Editable,
        context: Context,
        registerPage: RegisterPage
    ) {
        Log.d("registrato", "registrato")
        val jsonObject = JSONObject()
        jsonObject.put("username", name)
        jsonObject.put("email", mail)
        jsonObject.put("password", password)

        val body = jsonObject.toString()
            .toRequestBody("application/json;charset=utf-8".toMediaTypeOrNull())
        val client = OkHttpClient() //http client
        val registerRequest = Request.Builder() //POST request (REGISTER)
            .url("https://lam21.iot-prism-lab.cs.unibo.it/users")
            .post(body)
            .build()
        client.newCall(registerRequest).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val resBody = response.body?.string()
                println(resBody)
                if (response.isSuccessful) {
                    val options = ActivityOptions.makeBasic().toBundle()
                    val i = Intent(context, LoginPage::class.java)
                    startActivity(context, i, options)
                    finishAffinity(registerPage)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Looper.prepare().run {
                    Toast.makeText(
                        context,
                        "Please check your internet connection",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        })
    }

}

class Products(val products: ArrayList<ProductData>, val token: String)
class ProductData(val name: String, val description: String, val id: String, val barcode: String) :
    Serializable

