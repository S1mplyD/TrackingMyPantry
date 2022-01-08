package com.example.trackingmypantry

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.trackingmypantry.room_database.Product
import com.example.trackingmypantry.room_database.ProductViewModel
import java.util.*

//Classe che gestisce immettere un prodotto nel database remoto
class PostProductDetails(private val token: String?, private val barcode: String) :
    DialogFragment(), DatePickerDialog.OnDateSetListener {
    private lateinit var mViewModel: ProductViewModel

    //Variabili per le date di acquisto e scadenza
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
        var itemCategory = ""
        val nameDetails: EditText = rootView.findViewById(R.id.nameDetails)
        val descriptionDetails: EditText = rootView.findViewById(R.id.descriptionDetails)
        val nameField = nameDetails.text
        val descriptionField = descriptionDetails.text
        val submitButton: Button = rootView.findViewById(R.id.submitDetails)
        val category: Spinner = rootView.findViewById(R.id.categoriesSelector)
        //Funzione che permette di scegliere una categoria dall'apposito spinner
        category.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                itemCategory = adapterView?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(rootView.context, "Nothing selected", Toast.LENGTH_SHORT).show()
            }
        }
        //Funzione che permette di selezionare una data di acquisto
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
        //Funzione che permette di selezionare una data di scadenza
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
        //Funzione che inserisce il nuovo prodotto nei database
        submitButton.setOnClickListener {
            //Controllo se i parametri "nome", "descrizione" e "categoria" sono nulli
            if (nameField.isNullOrBlank() || descriptionField.isNullOrBlank() || itemCategory.isNullOrBlank()) {
                Toast.makeText(context, "Empty field", Toast.LENGTH_SHORT).show()
            } else {
                //Inserisco il prodotto nel database remoto
                HTTPcalls().postProductsDetails(
                    token,
                    nameField.toString(),
                    descriptionField.toString(),
                    barcode,
                    rootView.context
                )
                //Creo una data di acquisto e di scadenza
                val cal = Calendar.getInstance()
                cal.clear()
                cal.set(startYear, startMonth, startDay)
                val dataDiAcquisto = cal.time
                cal.clear()
                cal.set(endYear, endMonth, endDay)
                val dataDiScadenza = cal.time
                //Creo un nuovo prodotto con i dati inseriti dall'utente
                val product = Product(
                    nameField.toString(),
                    descriptionField.toString(),
                    barcode,
                    dataDiScadenza,
                    dataDiAcquisto,
                    itemCategory
                )
                //Inizializzo il viewModel del database ed inserisco il nuovo prodotto
                mViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
                mViewModel.insert(product)
                dismiss()
            }

        }

        return rootView
    }
    //Funzione che ritorna il giorno attuale
    private fun getToday() {
        val cal: Calendar = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {}

}