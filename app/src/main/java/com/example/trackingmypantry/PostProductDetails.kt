package com.example.trackingmypantry

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.trackingmypantry.database.Product
import com.example.trackingmypantry.database.ProductViewModel
import org.w3c.dom.Text
import java.util.*

class PostProductDetails(private val token: String, private val barcode: String) :
    DialogFragment(), DatePickerDialog.OnDateSetListener {
    private lateinit var mViewModel: ProductViewModel


    @RequiresApi(Build.VERSION_CODES.N)
    var day = 0
    var month = 0
    var year = 0

    var startDay = 0
    var startMonth = 0
    var startYear = 0

    var endDay = 0
    var endMonth = 0
    var endYear = 0

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
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
            getToday()
            DatePickerDialog(
                rootView.context,
                DatePickerDialog.OnDateSetListener { view, year, month, day ->
                    startYear = year
                    startMonth = month
                    startDay = day
                    rootView.findViewById<TextView>(R.id.buyDate).text = "$day/${month + 1}/$year"
                },
                year,
                month,
                day
            ).show()

        }

        rootView.findViewById<Button>(R.id.setExpirationDate).setOnClickListener {
            getToday()
            DatePickerDialog(
                rootView.context,
                DatePickerDialog.OnDateSetListener { view, year, month, day ->
                    endYear = year
                    endMonth = month
                    endDay = day
                    rootView.findViewById<TextView>(R.id.expirationDate).text =
                        "$day/${month + 1}/$year"
                },
                year,
                month,
                day
            ).show()
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
                val cal = Calendar.getInstance()
                cal.clear()
                cal.set(startYear,startMonth,startDay)
                val dataDiScadenza = cal.time
                cal.clear()
                cal.set(endYear,endMonth,endDay)
                val dataDiAcquisto = cal.time
                val product = Product(name.toString(),description.toString(),barcode,dataDiScadenza,dataDiAcquisto,null,null)
                mViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
                mViewModel.insert(product)
            }
        }
        return rootView
    }


    private fun getToday() {
        val cal: Calendar = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {}

}