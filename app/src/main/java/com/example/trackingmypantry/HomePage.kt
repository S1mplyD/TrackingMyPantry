package com.example.trackingmypantry

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.trackingmypantry.local_database.LocalProducts

class HomePage : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    var checked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        sharedPreferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE)
        val getProducts: Button = findViewById(R.id.getproductsButton)
        getProducts.setOnClickListener {
            val i = Intent(this, GetProducts::class.java)
            startActivity(i)
        }

        val showLocalProducts: Button = findViewById(R.id.localproductsButton)
        showLocalProducts.setOnClickListener {
            val i = Intent(this, LocalProducts::class.java)
            startActivity(i)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        checked = sharedPreferences.getBoolean("CHECKBOX", false)
        menu?.findItem(R.id.options_menu)?.isChecked = checked
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val editor = sharedPreferences.edit()
        if (item.itemId == R.id.options_menu) {
            item.isChecked = !item.isChecked
            editor.putBoolean("CHECKBOX", item.isChecked)
            editor.apply()
        }
        return super.onOptionsItemSelected(item)
    }
}