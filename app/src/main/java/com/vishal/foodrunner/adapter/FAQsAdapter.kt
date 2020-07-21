package com.vishal.foodrunner.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vishal.foodrunner.R
import com.vishal.foodrunner.modul.FAQS

class FAQsAdapter(val context: Context,val list: List<FAQS>):RecyclerView.Adapter<FAQsAdapter.FAQsViewHolder>() {
    class FAQsViewHolder(view: View):RecyclerView.ViewHolder(view){
       val txtQuestion:TextView=view.findViewById(R.id.txtQuestion)
        val txtAnswer:TextView=view.findViewById(R.id.txtAnswer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FAQsViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.faqs_one_view,parent,false)
        return FAQsViewHolder(view)
    }

    override fun getItemCount(): Int {
      return list.size
    }

    override fun onBindViewHolder(holder: FAQsViewHolder, position: Int) {
        val text=list[position]
        holder.txtQuestion.text=text.question
        holder.txtAnswer.text=text.answer
    }
}