package com.example.trackingmypantry.room_database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//View model che permette la comunicazione tra repository e UI
class ProductViewModel(application: Application) : AndroidViewModel(application) {
    val allProducts: LiveData<List<Product>>
    private val repository: ProductRepository

    //Recupero l'istanzo del database e inizializzo la repository
    init {
        val productDao = ProductDatabase.getDatabase(application).ProductDao()
        repository = ProductRepository(productDao)
        allProducts = repository.allProducts
    }

    //Funzioni che mi permettono tramite la repositori di eseguire le query verso il database
    fun insert(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(product)
        }
    }

    fun delete(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(product)
        }
    }

    fun searchDatabase(searchQuery: String): LiveData<List<Product>>{
        return repository.searchDatabase(searchQuery)
    }

    fun searchByCategory(category: String): LiveData<List<Product>>{
        return repository.searchByCategory(category)
    }
}
