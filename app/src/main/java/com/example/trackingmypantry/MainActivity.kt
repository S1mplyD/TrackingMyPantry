package com.example.trackingmypantry

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val registerButton = findViewById<Button>(R.id.registerButton)
        registerButton.setOnClickListener { val i = Intent(this, HomePage::class.java) //register button
        startActivity(i) //go to home page
        finish() //kill the register activity once the user is registered
        }
        val loginLink : TextView = findViewById(R.id.clickableText)
        loginLink.setOnClickListener { val i = Intent(this,LoginPage::class.java) //go to login page
        startActivity(i) }
    }

}