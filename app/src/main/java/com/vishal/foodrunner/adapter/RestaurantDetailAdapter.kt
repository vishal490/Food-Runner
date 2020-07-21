package com.vishal.foodrunner.adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Insets.add
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior.getTag
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.vishal.foodrunner.modul.ResDetail
import com.vishal.foodrunner.R
import com.vishal.foodrunner.activity.CartActivity
import com.vishal.foodrunner.database.DishDatabase
import com.vishal.foodrunner.database.DishEntity

class RestaurantDetailAdapter(val context:Context, val list: ArrayList<ResDetail>,var btproceedtocart:Button,var resName:String?,var resId:String?):RecyclerView.Adapter<RestaurantDetailAdapter.RestaurentDetailViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurentDetailViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.restaurant_detail_one_view, parent, false)

        return RestaurentDetailViewHolder(view)
    }

    var itemSelected: Int = 0
    var totalCost:Int=0
    var itemsSelectedId= arrayListOf<String>()
    override fun onBindViewHolder(holder: RestaurentDetailViewHolder, position: Int) {
        val text = list[position]
        holder.txtdishName.text = text.dishname
        holder.txtdishPrice.text = text.dishcoast
        val id = text.dishid.toInt()
        holder.txtdishNumber.text=text.dishnumber


        btproceedtocart.setOnClickListener {
            val intent = Intent(context, CartActivity::class.java)
            intent.putExtra("resName", resName)
            intent.putExtra("resId",resId)
            intent.putExtra("totalCoast",totalCost.toString())
            intent.putExtra("selectedItemsId",itemsSelectedId)
            context.startActivity(intent)
        }

        holder.btAdd.setOnClickListener(View.OnClickListener {

            val dishEntity = DishEntity(text.dishid.toInt(), text.dishname, text.dishcoast)
            val async = DbAsync(context, dishEntity, 1).execute().get()
            if (async) {
                itemsSelectedId.remove(text.dishid.toString())
                val asy = DbAsync(context, dishEntity, 3).execute().get()
                if (asy) {
                    totalCost-=text.dishcoast.toInt()
                    itemSelected--
                    holder.btAdd.text = "ADD"
                    val favColor = ContextCompat.getColor(context, R.color.colorAccent)
                    holder.btAdd.setBackgroundColor(favColor)
                } else {
                    Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show()
                }
            } else {
                itemsSelectedId.add(text.dishid.toString())
                val asy = DbAsync(context, dishEntity, 2).execute().get()
                if (asy) {
                    totalCost+=text.dishcoast.toInt()
                    itemSelected++
                    holder.btAdd.text = "REMOVE"
                    val favColor = ContextCompat.getColor(context, R.color.colorPrimary)
                    holder.btAdd.setBackgroundColor(favColor)
                } else {
                    Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show()
                }
            }
            if (itemSelected > 0) {
                btproceedtocart.visibility = View.VISIBLE
            } else {
                btproceedtocart.visibility = View.GONE
            }
        })
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class RestaurentDetailViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtdishName: TextView = view.findViewById(R.id.txtName)
        val txtdishPrice: TextView = view.findViewById(R.id.txtPrice)
        val txtdishNumber: TextView = view.findViewById(R.id.txtSrNumber)
        val btAdd: Button = view.findViewById(R.id.btAdd)
    }

    class DbAsync(val context: Context, val DishEntity: DishEntity, val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg params: Void?): Boolean {
            var db = Room.databaseBuilder(context, DishDatabase::class.java, "Dishes-db").build()
            when (mode) {
                1 -> {
                    val dish: DishEntity? = db.dishDao().getDishById(DishEntity.dish_id.toString())
                    db.close()
                    return dish != null
                }
                2 -> {
                    db.dishDao().insertDish(DishEntity)
                    db.close()
                    return true
                }
                3 -> {
                    db.dishDao().deleteDish(DishEntity)
                    db.close()
                    return true
                }
            }
            return true
        }
    }
}