package com.example.trackingmypantry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class PostProductDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_product_details)

        val nameDetails : EditText = findViewById(R.id.nameDetails)
        val descriptionDetails : EditText = findViewById(R.id.descriptionDetails)
        val submitButton : Button = findViewById(R.id.submitDetails)
        val name = nameDetails.text
        val description = descriptionDetails.text

        val token = intent.getStringExtra("token")
        val barcode = intent.getStringExtra("barcode")

        submitButton.setOnClickListener {
            if(name.isEmpty() || description.isEmpty()){
                Toast.makeText(this,"Empty field",Toast.LENGTH_SHORT).show()
            }
            else{
                val httpCalls = HTTPcalls()
                httpCalls.postProductsDetails(token.toString(),name,description,barcode.toString())
            }
        }

    }
}