package com.example.trackingmypantry

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class LoginPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)
        val loginMail: EditText = findViewById(R.id.loginMail)
        val loginPassword: EditText = findViewById(R.id.loginPassword)
        val mail = loginMail.text
        val password = loginPassword.text
        val loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {
            if (mail.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Empty field", Toast.LENGTH_SHORT).show()
            } else {
                val jsonObject = JSONObject()
                jsonObject.put("email", mail)
                jsonObject.put("password", password)
                val body = jsonObject.toString()
                    .toRequestBody("application/json;charset=utf-8".toMediaTypeOrNull())
                val client = OkHttpClient()
                val loginRequest = Request.Builder()
                        .url("https://lam21.modron.network/auth/login")
                        .post(body)
                        .build()
                client.newCall(loginRequest).enqueue(object : Callback{
                    override fun onResponse(call: Call, response: Response) {
                        val responseBody = response.body?.string()
                        println(responseBody)
                        if(response.isSuccessful){
                            val i = Intent(this@LoginPage, HomePage::class.java)
                            startActivity(i)
                            finishAffinity()
                        }
                    }
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute")
                    }
                })
            }
        }

        val registerLink: TextView = findViewById(R.id.linkToRegister)
        registerLink.setOnClickListener {
            val i = Intent(this, RegisterPage::class.java) //go to login page
            startActivity(i)
        }
    }
}