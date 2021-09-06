package com.example.trackingmypantry.database

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class ProductRepository(private val productDao : ProductDao) {
    val allProducts: LiveData<List<Product>> = productDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(product : Product) {
        productDao.insert(product)
    }

    fun delete(product: Product){
        productDao.delete(product)
    }

    fun searchDatabase(searchQuery: String) : LiveData<List<Product>>{
        return productDao.searchDatabase(searchQuery)
    }
}