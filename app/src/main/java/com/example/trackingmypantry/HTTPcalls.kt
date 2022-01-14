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
import com.example.trackingmypantry.remote_database.ProductResult
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.io.Serializable

class HTTPcalls {

    //Chiamata HTTP GET a https://lam21.iot-prism-lab.cs.unibo.it/products?barcode=
    //Funzione che mi permette di ottenere i prodotti con un determinato barcode
    fun getProducts(
        barcode: String,
        context: Context,
        supportFragmentManager: FragmentManager
    ) {

        val apiUrl = "https://lam21.iot-prism-lab.cs.unibo.it/products?barcode="
        val client = OkHttpClient()
        //Creo la richiesta
        val request = Request.Builder()
            .addHeader("Authorization", "Bearer $accessToken")
            .url(apiUrl + barcode)
            .build()
        //Eseguo la richiesta
        client.newCall(request).enqueue(object : Callback {
            //Se la richiesta fallisce lo segnalo all'utente
            override fun onFailure(call: Call, e: IOException) {
                Looper.prepare().run {
                    Toast.makeText(
                        context,
                        "Please check your internet connection",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                //Salvo i prodotti in una variabile
                val responseBody = response.body?.string()
                println(responseBody)
                val gson = GsonBuilder().create()
                val products = gson.fromJson(responseBody, Products::class.java)
                //Se la richiesta ha status 401 allora significa che l'access token è scaduto
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
                    //Porto l'utente alla pagina di login
                    val i = Intent(context, LoginPage::class.java)
                    val options = ActivityOptions.makeBasic().toBundle()
                    startActivity(context, i, options)
                } else if (response.isSuccessful) {
                    //Se la richiesta ha successo ed esiste un prodotto con quel barcode lo mostro all'utente
                    if (products.products.size > 0) {
                        val i = Intent(context, ProductResult::class.java)
                        i.putExtra("products", products.products)
                        i.putExtra("token", products.token)
                        val bundle = ActivityOptions.makeBasic().toBundle()
                        startActivity(context, i, bundle)
                    } else {
                        //Se non esiste un prodotto con quel barcode lo segnalo all'utente e gli chiedo di inserirne i dati
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

    //Chiamata HTTP POST a https://lam21.iot-prism-lab.cs.unibo.it/products
    //Funzione che mi permette di postare su server un nuovo prodotto
    fun postProductsDetails(
        token: String?,
        name: String,
        description: String,
        barcode: String,
        context: Context
    ) {
        val apiUrl = "https://lam21.iot-prism-lab.cs.unibo.it/products"
        val client = OkHttpClient()

        //Creo il body
        val jsonObject = JSONObject()
        jsonObject.put("token", token)
        jsonObject.put("name", name)
        jsonObject.put("description", description)
        jsonObject.put("barcode", barcode)
        jsonObject.put("test", false)
        val body = jsonObject.toString()
            .toRequestBody("application/json;charset=utf-8".toMediaTypeOrNull())
        //Creo la richiesta
        val request =
            Request.Builder().addHeader("Authorization", "Bearer $accessToken").url(apiUrl)
                .post(body).build()
        //Eseguo la chiamata
        client.newCall(request).enqueue(object : Callback {
            //In caso di mancata riuscita della richiesta, segnalo il problema all'utente
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
                //Se lo status della richiesta è 401 allora l'access token è scaduto
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
                    //Porto l'utente alla pagina di login
                    val i = Intent(context, LoginPage::class.java)
                    val options = ActivityOptions.makeBasic().toBundle()
                    startActivity(context, i, options)
                } else if (response.isSuccessful) {
                    //Se la richiesta ha successo notifico all'utente che il prodotto è stato inserito
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
    //Chiamata HTTP POST a https://lam21.iot-prism-lab.cs.unibo.it/votes
    //Funzione che mi permette di dare un voto ad un prodotto
    fun postProductPreference(token: String?, rating: Int, productId: String?, context: Context) {
        if (productId.isNullOrBlank()) {
            Log.d("Error", "Invalid product ID")
        } else {
            val apiUrl = "https://lam21.iot-prism-lab.cs.unibo.it/votes"
            val client = OkHttpClient()
            //Creo il body
            val jsonObject = JSONObject()
            jsonObject.put("token", token)
            jsonObject.put("rating", rating)
            jsonObject.put("productId", productId)
            val body = jsonObject.toString()
                .toRequestBody("application/json;charset=utf-8".toMediaTypeOrNull())
            //Creo la request
            val request =
                Request.Builder().addHeader("Authorization", "Bearer $accessToken").url(apiUrl)
                    .post(body).build()
            //Eseguo la request
            client.newCall(request).enqueue(object : Callback {
                //Se fallisce notifico l'utente
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
                    //Se lo status è 401 significa che l'access token è scaduto
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

                    //se la richiesta è riuscita notifico all'utente che ha avuto successo
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
    //Chiamata HTTP POST a https://lam21.iot-prism-lab.cs.unibo.it/auth/login
    fun login(
        mail: Editable,
        password: Editable,
        context: Context,
        loginPage: LoginPage,
        checked: Boolean
    ) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        //Creo il body della request
        val jsonObject = JSONObject()
        jsonObject.put("email", mail)
        jsonObject.put("password", password)
        val body = jsonObject.toString()
            .toRequestBody("application/json;charset=utf-8".toMediaTypeOrNull())
        val client = OkHttpClient()
        //Creo la request
        val loginRequest = Request.Builder()
            .url("https://lam21.iot-prism-lab.cs.unibo.it/auth/login")
            .post(body)
            .build()
        //Eseguo la chiamata
        client.newCall(loginRequest).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                //Se l'utente in fase di login ha selezionato di rimanere ricordato, allora salvo i paramtetri nelle shared preference
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
                    //Reindirizzo l'utente alla homepage
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
    //Chiamata HTTP POST a https://lam21.iot-prism-lab.cs.unibo.it/users
    fun register(
        name: Editable,
        mail: Editable,
        password: Editable,
        context: Context,
        registerPage: RegisterPage
    ) {
        //Creo il body della richiesta
        val jsonObject = JSONObject()
        jsonObject.put("username", name)
        jsonObject.put("email", mail)
        jsonObject.put("password", password)
        val body = jsonObject.toString()
            .toRequestBody("application/json;charset=utf-8".toMediaTypeOrNull())

        val client = OkHttpClient() //http client

        //Creo la request
        val registerRequest = Request.Builder()
            .url("https://lam21.iot-prism-lab.cs.unibo.it/users")
            .post(body)
            .build()
        //Eseguo la chiamata
        client.newCall(registerRequest).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val resBody = response.body?.string()
                println(resBody)
                //Se la chiamata ha successo, la registrazione è completa e l'utente viene portato sulla login page
                if (response.isSuccessful) {
                    val options = ActivityOptions.makeBasic().toBundle()
                    val i = Intent(context, LoginPage::class.java)
                    startActivity(context, i, options)
                    finishAffinity(registerPage)
                }
                //L'utente esiste già
                else if (response.code == 500){
                    Looper.prepare().run {
                        Toast.makeText(
                            context,
                            "User already existing!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
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

//Classi a cui vanno assegnati i prodotti ricevuti dalle api
//Queste classi sono necessarie per trasformare i dati di un JSON in dati utilizzabili dall'applicazione tramite la libreria Gson
//Products contiene l'array dei prodotti e il token
class Products(val products: ArrayList<ProductData>, val token: String)
//ProductData contiene i dati di un prodotto
class ProductData(val name: String, val description: String, val id: String, val barcode: String) :
    Serializable

