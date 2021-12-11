package com.example.trackingmypantry

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.database.Product
import com.example.trackingmypantry.database.ProductViewModel

class LocalProductsAdapter(private val mviewmodel: ProductViewModel) :
    RecyclerView.Adapter<CustomViewHolderLocal>() {

    private var productList = emptyList<Product>()

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolderLocal {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.product_card_local, parent, false)
        return CustomViewHolderLocal(cellForRow)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CustomViewHolderLocal, position: Int) {
        val currentItem = productList[position]
        holder.productName.text = currentItem.nome
        holder.productDescription.text = currentItem.descrizione
        if (!(currentItem.dataDiAcquisto.toString().length < 28 || currentItem.dataDiScadenza.toString().length < 28)) {
            holder.productBuyDate.text =
                "${holder.itemView.findViewById<TextView>(R.id.buyDateCard).resources.getString(R.string.buy_date)} " + currentItem.dataDiAcquisto.toString()
                    .substring(0, 10) + " " + currentItem.dataDiAcquisto.toString()
                    .substring(24, 28)
            holder.productExpirationDate.text =
                "${holder.itemView.findViewById<TextView>(R.id.expirationDateCard).resources.getString(R.string.expiration_date)} " + currentItem.dataDiScadenza.toString()
                    .substring(0, 10) + " " + currentItem.dataDiScadenza.toString()
                    .substring(24, 28)
        } else {
            holder.productBuyDate.visibility = View.GONE
            holder.productExpirationDate.visibility = View.GONE
        }
        if(!currentItem.categoria.isNullOrBlank()) holder.category.text = currentItem.categoria
        else holder.category.visibility = View.GONE

        holder.itemView.setOnClickListener {
            if (holder.itemView.findViewById<ConstraintLayout>(R.id.expandableLocal).isVisible) {
                holder.itemView.findViewById<ConstraintLayout>(R.id.expandableLocal).visibility =
                    View.GONE
            } else {
                holder.itemView.findViewById<ConstraintLayout>(R.id.expandableLocal).visibility =
                    View.VISIBLE
            }
        }
        holder.itemView.findViewById<Button>(R.id.deleteButton).setOnClickListener {
            mviewmodel.delete(productList[position])
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, productList.size)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(product: List<Product>) {
        this.productList = product
        notifyDataSetChanged()
    }


}

class CustomViewHolderLocal(view: View) : RecyclerView.ViewHolder(view) {
    val productName: TextView = view.findViewById(R.id.productName)
    val productDescription: TextView = view.findViewById(R.id.productDescription)
    val productBuyDate: TextView = view.findViewById(R.id.buyDateCard)
    val productExpirationDate: TextView = view.findViewById(R.id.expirationDateCard)
    val category : TextView = view.findViewById(R.id.productCategory)
}
