package com.example.trackingmypantry

import android.app.DatePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment

class PostProductDetails(private val token: String, private val barcode: String) :
    DialogFragment(), DatePickerDialog.OnDateSetListener {
    @RequiresApi(Build.VERSION_CODES.N)
    var day = 0
    var month = 0
    var year = 0

    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView: View =
            inflater.inflate(R.layout.activity_post_product_details, container, false)
        val nameDetails: EditText = rootView.findViewById(R.id.nameDetails)
        val descriptionDetails: EditText = rootView.findViewById(R.id.descriptionDetails)
        val submitButton: Button = rootView.findViewById(R.id.submitDetails)
        val name = nameDetails.text
        val description = descriptionDetails.text

        rootView.findViewById<Button>(R.id.setBuyDate).setOnClickListener {
            DatePickerDialog(rootView.context,this,year,month,day).show()
        }

        submitButton.setOnClickListener {
            if (name.isNullOrBlank() || description.isNullOrBlank()) {
                Toast.makeText(context, "Empty field", Toast.LENGTH_SHORT).show()
            } else {
                HTTPcalls().postProductsDetails(
                    token,
                    name,
                    description,
                    barcode,
                    context
                )
            }
        }
        return rootView
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month
        savedYear = year
    }
}