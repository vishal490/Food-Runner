package com.vishal.foodrunner.fragments


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.vishal.foodrunner.R


class MyProfle : Fragment() {
lateinit var txtname:TextView
    lateinit var txtemail:TextView
    lateinit var txtnumber:TextView
    lateinit var txtaddress:TextView
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_my_profle, container, false)
        sharedPreferences= activity!!.getSharedPreferences(getString(R.string.foodrunner_app), Context.MODE_PRIVATE)
        val address=sharedPreferences.getString("Address","Address")
        txtaddress=view.findViewById(R.id.txtaddress)
        txtaddress.text=(address)
        val email=sharedPreferences.getString("Email","Email").toString()
        txtemail=view.findViewById<TextView>(R.id.txtemail).apply { text=email }
        val name=sharedPreferences.getString("Name","Name").toString()
        txtname=view.findViewById<TextView>(R.id.txtname).apply { text= name}
        val number=sharedPreferences.getString("Number","Number").toString()
        txtnumber=view.findViewById<TextView>(R.id.txtnumber).apply { text= number}
        print("$(addres)")

        return view
    }

}
