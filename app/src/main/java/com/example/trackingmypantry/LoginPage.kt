package com.example.trackingmypantry

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class LoginPage : AppCompatActivity() {
    companion object {
        var accessToken = ""
    }

    lateinit var sharedPreferences: SharedPreferences
    var isRemembered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)
        sharedPreferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)

        isRemembered = sharedPreferences.getBoolean("CHECKBOX", false)

        if (isRemembered) {

            accessToken = sharedPreferences.getString("ACCESS_TOKEN","").toString()
            val i = Intent(this, HomePage::class.java)
            startActivity(i)
            finish()
        }

        val loginMail: EditText = findViewById(R.id.loginMail)
        val loginPassword: EditText = findViewById(R.id.loginPassword)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val rememberMe: CheckBox = findViewById(R.id.rememberUser)
        loginButton.setOnClickListener {
            if (loginMail.text.isNullOrBlank() || loginPassword.text.isNullOrBlank()) {
                Toast.makeText(this, "Empty field", Toast.LENGTH_SHORT).show()
            } else {
                HTTPcalls().login(
                    loginMail.text,
                    loginPassword.text,
                    this,
                    this@LoginPage,
                    rememberMe.isChecked
                )
            }
        }

        val registerLink: TextView = findViewById(R.id.linkToRegister)
        registerLink.setOnClickListener {
            val i = Intent(this, RegisterPage::class.java) //go to login page
            startActivity(i)
        }
    }
}

class AccessToken(val accessToken: String)