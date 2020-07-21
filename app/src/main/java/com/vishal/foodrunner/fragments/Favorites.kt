package com.vishal.foodrunner.fragments


import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.vishal.foodrunner.R
import com.vishal.foodrunner.adapter.FavoritesAdapter
import com.vishal.foodrunner.adapter.HomeAdapter
import com.vishal.foodrunner.database.RestaurantDatabase
import com.vishal.foodrunner.database.RestrauEntity
import com.vishal.foodrunner.modul.Rest


class Favorites : Fragment() {
    lateinit var recyclerDashboard: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    var restList= listOf<RestrauEntity>()
    lateinit var recyclerAdapter: FavoritesAdapter
    lateinit var rlProgress: RelativeLayout
    lateinit var pbProgress: ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view=inflater.inflate(R.layout.fragment_favorites, container, false)
        recyclerDashboard=view.findViewById(R.id.recyclerFavorites)
        layoutManager=LinearLayoutManager(activity)
        rlProgress=view.findViewById(R.id.rlProgress)
        pbProgress=view.findViewById(R.id.pbProgress)
        rlProgress.visibility=View.VISIBLE

        restList=RetreivRestrau(activity as Context).execute().get()
        if(activity!=null){
            if (restList.size!=0){
            rlProgress.visibility=View.GONE}
            recyclerAdapter= FavoritesAdapter(activity as Context,restList)
            recyclerDashboard.adapter= recyclerAdapter
            recyclerDashboard.layoutManager=layoutManager

        }

        return view
    }
  class RetreivRestrau(val context:Context):AsyncTask<Void,Void,List<RestrauEntity>>(){
      override fun doInBackground(vararg params: Void?): List<RestrauEntity> {
          val db= Room.databaseBuilder(context,RestaurantDatabase::class.java,"Restaurant-db").build()
          return db.resDao().getAllData()
      }

  }

}
