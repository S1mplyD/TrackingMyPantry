package com.example.trackingmypantry

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class RegisterPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_page)
        val registerName: EditText = findViewById(R.id.registerName)
        val registerMail: EditText = findViewById(R.id.registerMail)
        val registerPassword: EditText = findViewById(R.id.registerPassword)
        val mail = registerMail.text
        val name = registerName.text
        val password = registerPassword.text
        val registerButton = findViewById<Button>(R.id.registerButton)
        registerButton.setOnClickListener {

            if (mail.isEmpty() || name.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Empty field", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("registrato", "registrato")
                val jsonObject = JSONObject()
                jsonObject.put("username", name)
                jsonObject.put("email", mail)
                jsonObject.put("password", password)

                val body = jsonObject.toString()
                    .toRequestBody("application/json;charset=utf-8".toMediaTypeOrNull())
                val client = OkHttpClient() //http client
                val registerRequest = Request.Builder() //POST request (REGISTER)
                    .url("https://lam21.modron.network/users")
                    .post(body)
                    .build()
                client.newCall(registerRequest).enqueue(object : Callback {
                    override fun onResponse(call: Call, response: Response) {
                        val resBody = response.body?.string()
                        println(resBody)
                        if (response.isSuccessful) {
                            val i = Intent(this@RegisterPage, LoginPage::class.java)
                            startActivity(i)
                            finish()
                        }
                    }

                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute")
                    }

                })
            }

        }

    }

}


