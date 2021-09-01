package com.example.trackingmypantry.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {
    val allProducts: LiveData<List<Product>>
    private val repository: ProductRepository

    init {
        val productDao = ProductDatabase.getDatabase(application).ProductDao()
        repository = ProductRepository(productDao)
        allProducts = repository.allProducts
    }

    fun insert(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(product)
        }

    }
}
