package com.example.trackingmypantry

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import okhttp3.*

class GetProducts : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_products)
        val barcodeText: EditText = findViewById(R.id.barcode)
        val barcode = barcodeText.text
        val submitBarcode: Button = findViewById(R.id.submitBarcode)
        val scanner : Button = findViewById(R.id.scanButton)

        //Funzione che mi reindirizza allo scanner del barcode
        scanner.setOnClickListener {
            val i = Intent(this,BarcodeScanner::class.java)
            startActivity(i)
        }

        //Funzione che cerca il barcode inserito
        submitBarcode.setOnClickListener {
            //Se il barcode non è vuoto, eseguo la chiamata HTTP get Products
            if (barcode.isNullOrBlank()) {
                Toast.makeText(this, "Empty barcode field", Toast.LENGTH_SHORT).show()
            } else {
                HTTPcalls().getProducts(barcode.toString(),this@GetProducts,supportFragmentManager)
            }
        }

    }
}

