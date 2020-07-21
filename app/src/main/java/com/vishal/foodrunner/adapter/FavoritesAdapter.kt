package com.vishal.foodrunner.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vishal.foodrunner.R
import com.vishal.foodrunner.activity.RestaurantDetail
import com.vishal.foodrunner.database.RestrauEntity

class FavoritesAdapter(val context: Context,val list:List<RestrauEntity>):RecyclerView.Adapter<FavoritesAdapter.FavoritesAdapterViewHolder>() {

  class FavoritesAdapterViewHolder(view:View):RecyclerView.ViewHolder(view){
      val txtResName: TextView =view.findViewById(R.id.txtName)
      val txtResPrice: TextView =view.findViewById(R.id.txtPrice)
      val txtResRating: TextView =view.findViewById(R.id.txtResRating)
      val imgResImage: ImageView =view.findViewById(R.id.imgResCover)
      val llContext: LinearLayout =view.findViewById(R.id.llContext)
      val txtbtfavorites: ImageButton =view.findViewById(R.id.txtbtfavorites)

  }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesAdapterViewHolder {
       val view=LayoutInflater.from(parent.context).inflate(R.layout.home_one_view,parent,false)
        return FavoritesAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: FavoritesAdapterViewHolder, position: Int) {
        val text =list[position]
        val resEntity=RestrauEntity(text.res_id.toInt(),text.resName,text.resPrice,text.resRating,text.resImage)
        holder.txtResName.text=text.resName
        holder.txtResPrice.text=text.resPrice
        holder.txtResRating.text=text.resRating
        //holder.imgBookImage.setImageResource(text.bookimage)
        Picasso.get().load(text.resImage).error(R.drawable.ic_default_cover).into(holder.imgResImage)
        val checkFav=HomeAdapter.DbAsync(context,resEntity,1).execute()
        val isFav=checkFav.get()

        if(isFav){
            holder.txtbtfavorites.setTag("liked")
            holder.txtbtfavorites.background =
                context.resources.getDrawable(R.drawable.ic_redheart)

        }else{
            holder.txtbtfavorites.setTag("unliked")
            holder.txtbtfavorites.background =
                context.resources.getDrawable(R.drawable.ic_heart)
        }

        holder.llContext.setOnClickListener {
            val intent= Intent(context, RestaurantDetail::class.java)
            intent.putExtra("res_id",text.res_id.toString())
            intent.putExtra("res_name",text.resName)
            context.startActivity(intent)
        }
        holder.txtbtfavorites.setOnClickListener{

            val async= HomeAdapter.DbAsync(context, resEntity, 1).execute().get()
            if (async){
                val async1= HomeAdapter.DbAsync(context, resEntity, 3).execute().get()
                if (async1){
                    Toast.makeText(context,"Removed from Favorites", Toast.LENGTH_LONG).show()
                    holder.txtbtfavorites.setTag("unliked")
                    holder.txtbtfavorites.background=context.resources.getDrawable(R.drawable.ic_heart)
                }else{
                    Toast.makeText(context,"Some Error Occurred", Toast.LENGTH_LONG).show()
                }
            }else{
                val async2= HomeAdapter.DbAsync(context, resEntity, 2).execute().get()
                if (async2){
                    Toast.makeText(context,"Added to Favorites", Toast.LENGTH_LONG).show()
                    holder.txtbtfavorites.setTag("liked")
                    holder.txtbtfavorites.background=context.resources.getDrawable(R.drawable.ic_redheart)
                }else{
                    Toast.makeText(context,"some error occurred", Toast.LENGTH_LONG).show()
                }
            }
        }

    }
}