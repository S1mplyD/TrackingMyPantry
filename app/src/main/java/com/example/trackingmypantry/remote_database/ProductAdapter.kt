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
        holder.productName.text = products[position].name
        holder.productDescription.text = products[position].description
        holder.itemView.setOnClickListener {
            if (holder.itemView.findViewById<ConstraintLayout>(R.id.expandable).isVisible) {
                //TransitionManager.beginDelayedTransition(holder.itemView as ViewGroup)
                holder.itemView.findViewById<ConstraintLayout>(R.id.expandable).visibility = View.GONE
            } else {
               // TransitionManager.beginDelayedTransition(holder.itemView as ViewGroup)
                holder.itemView.findViewById<ConstraintLayout>(R.id.expandable).visibility =
                    View.VISIBLE
            }

        }

        //val delete : Button = holder.itemView.findViewById(R.id.delete)

//        delete.setOnClickListener {
//            HTTPcalls().delete(products[position].id)
//            products.removeAt(position)
//            notifyItemRemoved(position)
//            notifyItemRangeChanged(position,products.size)
//
//        }
        val addToDatabase : Button = holder.itemView.findViewById(R.id.addToLocalList)
        addToDatabase.setOnClickListener {
            val dialog = AddProductToLocalDB(products[position].barcode,products[position].name,products[position].description)
            dialog.show(supportFragmentManager, "PostProductDetails")
        }
        val vote : Button = holder.itemView.findViewById(R.id.voteButton)
        vote.setOnClickListener {
            val dialog = VoteFragment(products[position].name,products[position].description,token,products[position].id)

            dialog.show(supportFragmentManager, "VoteDialog")
//            val i = Intent(holder.itemView.context,VotePage::class.java)
//            i.putExtra("token", token)
//            i.putExtra("productName",products[position].name)
//            i.putExtra("productDescription", products[position].description)
//            i.putExtra("productID", products[position].id)
//            startActivity(holder.itemView.context,i,ActivityOptions.makeBasic().toBundle())
        }

    }

    override fun getItemCount(): Int {
        return products.size
    }


}

class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val productName: TextView = view.findViewById(R.id.productName)
    val productDescription: TextView = view.findViewById(R.id.productDescription)
}
