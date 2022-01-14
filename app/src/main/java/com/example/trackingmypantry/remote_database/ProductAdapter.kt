package com.example.trackingmypantry.remote_database

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingmypantry.ProductData
import com.example.trackingmypantry.R
import com.example.trackingmypantry.VoteFragment
import com.example.trackingmypantry.local_database.AddProductToLocalDB

class ProductAdapter(
    private val products: ArrayList<ProductData>,
    private val token: String?,
    private val supportFragmentManager: FragmentManager
) : RecyclerView.Adapter<CustomViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.product_card, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        //Pongo i campi della view ai valori dei prodotti
        holder.productName.text = products[position].name
        holder.productDescription.text = products[position].description
        //Se clicco su un prodotto estendo la sua scheda mostrando due bottoni
        holder.itemView.setOnClickListener {
            if (holder.itemView.findViewById<ConstraintLayout>(R.id.expandable).isVisible) {
                holder.itemView.findViewById<ConstraintLayout>(R.id.expandable).visibility =
                    View.GONE
            } else {
                holder.itemView.findViewById<ConstraintLayout>(R.id.expandable).visibility =
                    View.VISIBLE
            }
        }

        val addToDatabase: Button = holder.itemView.findViewById(R.id.addToLocalList)
        //Bottone che serve ad aprire un dialog per aggiungere un prodotto al database locale
        addToDatabase.setOnClickListener {
            val dialog = AddProductToLocalDB(
                products[position].barcode,
                products[position].name,
                products[position].description
            )
            dialog.show(supportFragmentManager, "PostProductDetails")
        }
        val vote: Button = holder.itemView.findViewById(R.id.voteButton)
        //Bottone che serve per dare un voto ad un prodotto
        vote.setOnClickListener {
            val dialog = VoteFragment(
                products[position].name,
                products[position].description,
                token,
                products[position].id
            )
            dialog.show(supportFragmentManager, "VoteDialog")
        }
    }

    //Ritorna il numero di prodotti
    override fun getItemCount(): Int {
        return products.size
    }

}

//Classe contenente gli oggetti della view
class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val productName: TextView = view.findViewById(R.id.productName)
    val productDescription: TextView = view.findViewById(R.id.productDescription)
}
