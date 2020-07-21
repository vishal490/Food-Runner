package com.vishal.foodrunner.adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.squareup.picasso.Picasso
import com.vishal.foodrunner.modul.Rest
import com.vishal.foodrunner.R
import com.vishal.foodrunner.activity.RestaurantDetail
import com.vishal.foodrunner.database.RestaurantDatabase
import com.vishal.foodrunner.database.RestrauEntity
import java.util.ArrayList

class HomeAdapter(val context: Context, val list: ArrayList<Rest>):
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    override fun onBindViewHolder(holder: HomeAdapter.HomeViewHolder, position: Int) {
        val text =list[position]
        val resEntity=RestrauEntity(text.resid.toInt(),text.resname,text.resprice,text.resrating,text.resimage)
        holder.txtResName.text=text.resname
        holder.txtResPrice.text="Rs."+text.resprice+"/person"
        holder.txtResRating.text=text.resrating
        //holder.imgBookImage.setImageResource(text.bookimage)
        Picasso.get().load(text.resimage).error(R.drawable.ic_default_cover).into(holder.imgResImage)
        val checkFav=DbAsync(context,resEntity,1).execute()
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
            val intent=Intent(context, RestaurantDetail::class.java)
            intent.putExtra("res_id",text.resid)
            intent.putExtra("res_name",text.resname)
            context.startActivity(intent)
        }
        holder.txtbtfavorites.setOnClickListener{

         val async=DbAsync(context,resEntity,1).execute().get()
            if (async){
                val async1=DbAsync(context,resEntity,3).execute().get()
                if (async1){
                    Toast.makeText(context,"Removed from Favorites",Toast.LENGTH_LONG).show()
                    holder.txtbtfavorites.setTag("unliked")
                    holder.txtbtfavorites.background=context.resources.getDrawable(R.drawable.ic_heart)
                }else{
                    Toast.makeText(context,"Some Error Occurred",Toast.LENGTH_LONG).show()
                }
            }else{
                val async2=DbAsync(context,resEntity,2).execute().get()
                if (async2){
                    Toast.makeText(context,"Added to Favorites",Toast.LENGTH_LONG).show()
                    holder.txtbtfavorites.setTag("liked")
                    holder.txtbtfavorites.background=context.resources.getDrawable(R.drawable.ic_redheart)
                }else{
                    Toast.makeText(context,"some error occurred",Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.home_one_view,parent,false)
        return HomeViewHolder(view)
    }

    class HomeViewHolder(view: View): RecyclerView.ViewHolder(view){
        val txtResName: TextView =view.findViewById(R.id.txtName)
        val txtResPrice: TextView =view.findViewById(R.id.txtPrice)
        val txtResRating: TextView =view.findViewById(R.id.txtResRating)
        val imgResImage: ImageView =view.findViewById(R.id.imgResCover)
        val llContext: LinearLayout =view.findViewById(R.id.llContext)
        val txtbtfavorites:ImageButton=view.findViewById(R.id.txtbtfavorites)

    }
    class DbAsync(val context:Context, val restrauEntity:RestrauEntity, val mode:Int):AsyncTask<Void,Void,Boolean>(){
        override fun doInBackground(vararg params: Void?): Boolean {
           var db= Room.databaseBuilder(context, RestaurantDatabase::class.java,"Restaurant-db").build()
            when(mode){
                1->{val res:RestrauEntity?=db.resDao().getRestrauById(restrauEntity.res_id.toString())
                    db.close()
                    return res!=null}
                2->{db.resDao().insertRestrau(restrauEntity)
                    db.close()
                    return true}
                3->{db.resDao().deleteRestrau(restrauEntity)
                    db.close()
                    return true}
            }
            return true
        }
    }

}
