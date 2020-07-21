package com.vishal.foodrunner.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vishal.foodrunner.R
import com.vishal.foodrunner.database.DishEntity
import com.vishal.foodrunner.modul.OrderDetail


class CartAdapter(val context: Context, val list:List<DishEntity>) :RecyclerView.Adapter<CartAdapter.CartAdapterViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapterViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.cart_one_view,parent,false)
        return CartAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
     return list.size
    }

    override fun onBindViewHolder(holder: CartAdapterViewHolder, position: Int) {
        val text=list[position]
        holder.txtdishName.text=text.dishName
        holder.txttdishPrice.text="Rs. "+text.dishPrice
    }

    class CartAdapterViewHolder(view: View):RecyclerView.ViewHolder(view){
        val txtdishName:TextView=view.findViewById(R.id.txtdishName)
        val txttdishPrice:TextView=view.findViewById(R.id.txtdishPrice)
    }
}