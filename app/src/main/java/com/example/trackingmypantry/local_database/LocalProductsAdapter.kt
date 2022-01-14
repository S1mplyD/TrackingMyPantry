package com.example.trackingmypantry.local_database

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.R
import com.example.trackingmypantry.room_database.Product
import com.example.trackingmypantry.room_database.ProductViewModel
import okhttp3.internal.indexOf

class LocalProductsAdapter(private val mviewmodel: ProductViewModel) :
    RecyclerView.Adapter<CustomViewHolderLocal>() {

    private var productList = emptyList<Product>()

    //ritorna il numero di prodotti
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
        //Pongo i campi della view ai valori dei prodotti
        val currentItem = productList[position]
        holder.productName.text = currentItem.nome
        holder.productDescription.text = currentItem.descrizione
        //Controllo se sono state inserite una data di acquisto o scadenza. Se sono inserite le visualizzo, altrimenti le elimino dalla visualizzazione
        if(currentItem.dataDiAcquisto !== null){
            holder.productBuyDate.text =
                "${holder.itemView.findViewById<TextView>(R.id.buyDateCard).resources.getString(R.string.buy_date)} " + currentItem.dataDiAcquisto.toString()
        }
        else {
            holder.productBuyDate.visibility = View.GONE
        }
        if (currentItem.dataDiScadenza != null) {
            holder.productExpirationDate.text =
                "${
                    holder.itemView.findViewById<TextView>(R.id.expirationDateCard).resources.getString(
                        R.string.expiration_date
                    )
                } " + currentItem.dataDiScadenza.toString()
        }
        else{
            holder.productExpirationDate.visibility = View.GONE
        }
        //Categoria del prodotto
        holder.category.text =
            holder.itemView.resources.getString(R.string.category) + " " + currentItem.categoria
        //Se clicco su un prodotto estendo la sua scheda mostrando il bottone per eliminare il prodotto
        holder.itemView.setOnClickListener {
            if (holder.itemView.findViewById<ConstraintLayout>(R.id.expandableLocal).isVisible) {
                holder.itemView.findViewById<ConstraintLayout>(R.id.expandableLocal).visibility =
                    View.GONE
            } else {
                holder.itemView.findViewById<ConstraintLayout>(R.id.expandableLocal).visibility =
                    View.VISIBLE
            }
        }
        //Bottone di eliminazione prodotto, elimina il prodotto dal database e notifica la recycler view che aggiorna la visualizzazione
        holder.itemView.findViewById<Button>(R.id.deleteButton).setOnClickListener {
            mviewmodel.delete(productList[position])
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, productList.size)
        }
    }

    //Funzione che viene chiamata per settare i prodotti
    @SuppressLint("NotifyDataSetChanged")
    fun setData(product: List<Product>) {
        this.productList = product
        notifyDataSetChanged()
    }
}

//Viewholder
class CustomViewHolderLocal(view: View) : RecyclerView.ViewHolder(view) {
    val productName: TextView = view.findViewById(R.id.productName)
    val productDescription: TextView = view.findViewById(R.id.productDescription)
    val productBuyDate: TextView = view.findViewById(R.id.buyDateCard)
    val productExpirationDate: TextView = view.findViewById(R.id.expirationDateCard)
    val category: TextView = view.findViewById(R.id.productCategory)
}
