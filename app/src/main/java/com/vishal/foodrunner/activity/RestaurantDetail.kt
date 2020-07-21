package com.vishal.foodrunner.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.vishal.bookhub.util.ConnectionManager

import com.vishal.foodrunner.R
import com.vishal.foodrunner.adapter.RestaurantDetailAdapter
import com.vishal.foodrunner.database.DishDatabase
import com.vishal.foodrunner.database.RestaurantDatabase
import com.vishal.foodrunner.fragments.Home
import com.vishal.foodrunner.modul.ResDetail
import org.json.JSONException

class RestaurantDetail:AppCompatActivity() {
    lateinit var rlprogressDetail: RelativeLayout
    lateinit var pbprogressDetail:ProgressBar
    lateinit var recyclerDetail: RecyclerView
    lateinit var layoutManager:RecyclerView.LayoutManager
    lateinit var recyclerAdapter:RestaurantDetailAdapter
    lateinit var toolbar:Toolbar
    var restruName:String?="Menu Card"

    lateinit var proceedtocart:Button
    val resList= arrayListOf<ResDetail>()
    var restaurant_id:String?="100"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retaurant_detail)
        RetreivDish(this).execute()
        recyclerDetail=findViewById(R.id.recyclerDetail)
        pbprogressDetail=findViewById(R.id.pbprogressDeatail)
        proceedtocart=findViewById(R.id.proceedtocart)
        rlprogressDetail=findViewById(R.id.rlprogressDetail)
        rlprogressDetail.visibility= View.VISIBLE
        proceedtocart.visibility= View.GONE
        toolbar=findViewById(R.id.toolbar)
        layoutManager=LinearLayoutManager(this)
        if(intent!=null){
            restaurant_id=intent.getStringExtra("res_id")
            restruName=intent.getStringExtra("res_name")
        }else{
            finish()
            Toast.makeText(this,"Some Error occur",Toast.LENGTH_SHORT).show()
        }

        if(restaurant_id=="100"|| restruName=="Menu Card"){
            finish()
            Toast.makeText(this,"Some Error occur",Toast.LENGTH_SHORT).show()
        }
        setUpToolbar()
        val queue=Volley.newRequestQueue(this as Context)
        val url="http://13.235.250.119/v2/restaurants/fetch_result/$restaurant_id"
        if(ConnectionManager().checkconnectivity(this as Context)){
            val jsonobject=object:JsonObjectRequest(Request.Method.GET,url,null,Response.Listener {
                try{
                      rlprogressDetail.visibility=View.GONE
                    val resdishjsonobject=it.getJSONObject("data")
                    val success = resdishjsonobject.getBoolean("success")
                    if (success) {
                        val data = resdishjsonobject.getJSONArray("data")
                        var j=0
                        for (i in 0 until data.length()) {
                            j=i
                         val resjsonobject = data.getJSONObject(i)
                            val resobject = ResDetail(
                                resjsonobject.getString("id"),
                                resjsonobject.getString("name"),
                                resjsonobject.getString("cost_for_one"),
                                ("${++j}")
                            )

                            resList.add(resobject)
                            recyclerAdapter = RestaurantDetailAdapter(this as Context, resList,proceedtocart,restruName,restaurant_id)
                            recyclerDetail.adapter = recyclerAdapter
                            recyclerDetail.layoutManager = layoutManager
                        }
                    } else {
                        Toast.makeText(this, "Some Error Ouccured!!", Toast.LENGTH_SHORT)
                            .show()
                    }


                }catch (e:JSONException){
                    Toast.makeText(this,"Error Ouccr",Toast.LENGTH_SHORT).show()
                }

            }, Response.ErrorListener { Toast.makeText(this,"Low Internet connection please check your connection and try again",Toast.LENGTH_SHORT).show() }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "bc91c4800261ef"
                    return headers
                }
            }
            queue.add(jsonobject)
        }else{
            val dialogue = AlertDialog.Builder(this as Context)
            dialogue.setTitle("Error")
            dialogue.setMessage("Internet Connection Not Found")
            dialogue.setPositiveButton("Open Setting") { text, listener ->
                val setting= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(setting)
                finish()

            }
            dialogue.setNegativeButton("Exit") { text, listener ->
                ActivityCompat.finishAffinity(this as Activity)
            }
            dialogue.create()
            dialogue.show()

        }
    }

    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title=restruName
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
    var flag:Boolean=false
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id==android.R.id.home) {
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
            flag=true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (restruName == "Menu Card") {
            Toast.makeText(this, "Unexpected", Toast.LENGTH_SHORT).show()
        } else {
            val intent=Intent(this,MainActivity::class.java)
            RetreivDish(this).execute()
            startActivity(intent)
        }
    }

    override fun onPause() {
        super.onPause()
            finish()
    }
    class RetreivDish(val context:Context): AsyncTask<Void, Void, Boolean>(){
        override fun doInBackground(vararg params: Void?): Boolean {
            val db= Room.databaseBuilder(context, DishDatabase::class.java,"Dishes-db").build()
            db.dishDao().deleteAll()
            return true
        }

    }
}