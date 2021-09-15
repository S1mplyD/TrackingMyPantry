package com.example.trackingmypantry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.database.ProductViewModel

class LocalProducts : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var mviewmodel: ProductViewModel
    private lateinit var adapter: LocalProductsAdapter

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
            val dialog = FilterFragment()
            dialog.show(supportFragmentManager, "filter_fragment")
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

}