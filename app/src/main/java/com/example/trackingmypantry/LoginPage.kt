package com.example.trackingmypantry

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class LoginPage : AppCompatActivity() {
    //Companion object creato per salvare l'access token e renderlo disponibile in qualsiasi punto del codice
    companion object {
        var accessToken = ""
    }

    //Shared preferences per salvare il token su file e lo stato del "Ricordami"
    lateinit var sharedPreferences: SharedPreferences
    var isRemembered = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        //Controllo se durante il login l'utente ha scelto di essere ricordato
        sharedPreferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        isRemembered = sharedPreferences.getBoolean("CHECKBOX", false)

        //Se l'utente ha scelto di essere ricordato recupero il suo access token dalle shared preference
        if (isRemembered) {
            accessToken = sharedPreferences.getString("ACCESS_TOKEN", "").toString()
            val i = Intent(this, HomePage::class.java)
            startActivity(i)
            finish()
        }

        val loginMail: EditText = findViewById(R.id.loginMail)
        val loginPassword: EditText = findViewById(R.id.loginPassword)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val rememberMe: CheckBox = findViewById(R.id.rememberUser)

        //Controllo che i campi di log in non siano vuoti e faccio una chiamata http a login
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

        //Link che permette a chi non è registrato di essere reindirizzato alla pagina apposita
        val registerLink: TextView = findViewById(R.id.linkToRegister)
        registerLink.setOnClickListener {
            val i = Intent(this, RegisterPage::class.java)
            startActivity(i)
        }

    }

}

class AccessToken(val accessToken: String)