package com.example.trackingmypantry

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*

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
            //Controllo che i campi non siano vuoti
            if (mail.isNullOrBlank() || name.isNullOrBlank() || password.isNullOrBlank()) {
                Toast.makeText(this, "Empty field", Toast.LENGTH_SHORT).show()
            } else {
                //Chiamata HTTP a register
                HTTPcalls().register(registerName.text,registerMail.text,registerPassword.text,this,this@RegisterPage)
            }
        }
    }
}


