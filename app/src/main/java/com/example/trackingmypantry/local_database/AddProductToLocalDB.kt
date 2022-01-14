package com.example.trackingmypantry.local_database

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
import com.example.trackingmypantry.R
import com.example.trackingmypantry.room_database.Product
import com.example.trackingmypantry.room_database.ProductViewModel
import java.util.*

//Classe che gestisce immettere un prodotto nel database locale
class AddProductToLocalDB(
    private val barcode: String,
    private val nome: String,
    private val descrizione: String
) :
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
        val nameDetails: EditText = rootView.findViewById(R.id.nameDetails)
        val descriptionDetails: EditText = rootView.findViewById(R.id.descriptionDetails)
        nameDetails.setText(nome)
        descriptionDetails.setText(descrizione)

        val submitButton: Button = rootView.findViewById(R.id.submitDetails)
        var itemCategory = ""
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
                TODO("Not yet implemented")
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
        //Funzione che inserisce il nuovo prodotto nel database
        submitButton.setOnClickListener {
            //Controllo se i parametri "nome", "descrizione" e "categoria" sono nulli
            if (nameDetails.text.isNullOrBlank() || descriptionDetails.text.isNullOrBlank() || itemCategory.isNullOrBlank()) {
                //Avviso l'utente che ci sono campi necessari vuoti
                Toast.makeText(context, "Empty field", Toast.LENGTH_SHORT).show()
            } else {
                //Creo una data di acquisto e di scadenza
                val cal = Calendar.getInstance()
                cal.clear()
                cal.set(startYear, startMonth, startDay, 0, 0, 0)
                var dataDiAcquisto = cal.time
                cal.clear()
                cal.set(endYear, endMonth, endDay, 0, 0, 0)
                var dataDiScadenza = cal.time
                if (dataDiAcquisto.toString() == "Wed Dec 31 00:00:00 GMT+01:00 2")
                    dataDiAcquisto = null
                if (dataDiScadenza.toString() == "Wed Dec 31 00:00:00 GMT+01:00 2")
                    dataDiScadenza = null


                //Creo un nuovo prodotto con i dati inseriti dall'utente
                val product = Product(
                    nameDetails.text.toString(),
                    descriptionDetails.text.toString(),
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