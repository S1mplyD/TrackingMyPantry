package com.example.trackingmypantry

import android.content.DialogInterface
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.database.ProductViewModel

class LocalProducts : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var mviewmodel: ProductViewModel
    private lateinit var adapter: LocalProductsAdapter
    private var result: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_products)

        mviewmodel = ViewModelProvider(this).get(ProductViewModel::class.java)
        val recview: RecyclerView = findViewById(R.id.localProductsRecView)
        adapter = LocalProductsAdapter(mviewmodel)
        recview.layoutManager = LinearLayoutManager(this)
        recview.adapter = adapter

        mviewmodel.allProducts.observe(this, Observer { product ->
            adapter.setData(product)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)

        val search = menu?.findItem(R.id.search_menu)
        val searchView = search?.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.filter_menu) {
            val builder = AlertDialog.Builder(this)
            val res: Resources = resources
            val array = res.getStringArray(R.array.categories)
            //result = -1
            builder.setSingleChoiceItems(array, -1) { dialogInterface, i ->
                dialogInterface.dismiss()
                result = array[i]
                searchByCategory(result)
            }
            val dialog = builder.create()
            dialog.show()

        }

        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchDatabase(query)
        }
        return true
    }

    private fun searchDatabase(query: String) {
        val searchQuery = "%$query%"
        mviewmodel.searchDatabase(searchQuery).observe(this, { list ->
            list.let {
                adapter.setData(it)
            }
        })
    }

    private fun searchByCategory(category: String) {
        val res: Resources = resources
        val noCat : String = res.getString(R.string.noCategory)
        if(category != noCat){
            val searchQuery = "%$category%"
            mviewmodel.searchByCategory(searchQuery).observe(this, { list ->
                list.let {
                    adapter.setData(it)
                }
            })
        }
        else{
            println("here")
            mviewmodel.allProducts.observe(this, Observer { product ->
                adapter.setData(product)
            })
        }
    }
}
