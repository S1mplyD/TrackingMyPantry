package com.example.trackingmypantry.local_database

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.R
import com.example.trackingmypantry.room_database.ProductViewModel

class LocalProducts : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var mviewmodel: ProductViewModel
    private lateinit var adapter: LocalProductsAdapter
    private var result: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_products)
        //Inizializzo il viewModel del database locale
        mviewmodel = ViewModelProvider(this).get(ProductViewModel::class.java)
        //Creo la Recycler View
        val recview: RecyclerView = findViewById(R.id.localProductsRecView)
        //Inizializzo l'adapter della recycler View
        adapter = LocalProductsAdapter(mviewmodel)
        recview.layoutManager = LinearLayoutManager(this)
        recview.adapter = adapter

        //Leggo i prodotti salvati localmente e li visualizzo nella recycler view
        mviewmodel.allProducts.observe(this, Observer { product ->
            adapter.setData(product)
        })
    }

    //Funzione che crea il menù opzioni
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        //Menù che permette di cercare un prodotto
        val search = menu?.findItem(R.id.search_menu)
        val searchView = search?.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)

        return true
    }

    //Funzione che permette di visualizzare i prodotti in una determinata categoria
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.filter_menu) {
            //Creo un Alert Dialog per mostrare le categorie
            val builder = AlertDialog.Builder(this)
            val res: Resources = resources
            val array = res.getStringArray(R.array.categories)
            //mostro i prodotti appartenenti alla categoria selezionata
            builder.setSingleChoiceItems(array, -1) { dialogInterface, i ->
                dialogInterface.dismiss()
                result = array[i]
                searchByCategory(result)
            }
            val dialog = builder.create()
            dialog.show()
        }
        else if (item.itemId == R.id.resetCategories){
            mviewmodel.allProducts.observe(this, Observer { product ->
                adapter.setData(product)
            })
        }

        return super.onOptionsItemSelected(item)
    }

    //Cerca i prodotti attinenti al completamento della stringa
    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchDatabase(query)
        }
        return true
    }

    //Cerca i prodotti attinenti nel momento della scrittura della stringa
    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchDatabase(query)
        }
        return true
    }

    //Cerca la query inserita nel database locale
    private fun searchDatabase(query: String) {
        val searchQuery = "%$query%"
        mviewmodel.searchDatabase(searchQuery).observe(this, { list ->
            list.let {
                adapter.setData(it)
            }
        })
    }

    //Cerca la categoria inserita nel database locale
    private fun searchByCategory(category: String) {
        val searchQuery = "%$category%"
        mviewmodel.searchByCategory(searchQuery).observe(this, { list ->
            list.let {
                adapter.setData(it)
            }
        })
    }

}

