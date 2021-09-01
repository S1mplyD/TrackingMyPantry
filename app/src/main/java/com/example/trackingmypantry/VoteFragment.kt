package com.example.trackingmypantry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment

class VoteFragment(private val name: String, private val description: String, private val token: String?, private val id: String) : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView : View = inflater.inflate(R.layout.fragment_vote, container, false)

        rootView.findViewById<TextView>(R.id.productNameVote).text = name
        rootView.findViewById<TextView>(R.id.productDescriptionVote).text = description

        rootView.findViewById<Button>(R.id.submitVoteButton).setOnClickListener {
            if(!rootView.findViewById<EditText>(R.id.vote).text.isNullOrBlank()){
                HTTPcalls().postProductPreference(token,rootView.findViewById<EditText>(R.id.vote).text.toString().toInt(),id,context)
            } else {
                Toast.makeText(context,"Empty vote field", Toast.LENGTH_SHORT).show()
            }
        }


        return rootView
    }
}