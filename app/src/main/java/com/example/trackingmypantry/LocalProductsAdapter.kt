package com.example.trackingmypantry

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.database.Product

class LocalProductsAdapter : RecyclerView.Adapter<CustomViewHolderLocal>() {

    private var productList = emptyList<Product>()

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolderLocal {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.product_card, parent, false)
        return CustomViewHolderLocal(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolderLocal, position: Int) {
        val currentItem = productList[position]
        holder.productName.text = currentItem.nome
        holder.productDescription.text = currentItem.descrizione
    }

    fun setData(product: List<Product>) {
        this.productList = product
        notifyDataSetChanged()
    }


}

class CustomViewHolderLocal(view: View) : RecyclerView.ViewHolder(view) {
    val productName: TextView = view.findViewById(R.id.productName)
    val productDescription: TextView = view.findViewById(R.id.productDescription)
}
