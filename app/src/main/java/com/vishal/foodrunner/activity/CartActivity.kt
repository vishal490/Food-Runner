package com.vishal.foodrunner.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import com.vishal.foodrunner.adapter.CartAdapter
import com.vishal.foodrunner.database.DishDatabase
import com.vishal.foodrunner.database.DishEntity
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class CartActivity:AppCompatActivity() {
    lateinit var recyclerCart:RecyclerView
    lateinit var tool:Toolbar
    lateinit var txtrestrauName:TextView
    lateinit var btplaceorder:Button
    var dish= listOf<DishEntity>()
    var res_id:String?="5"
    var user_id:String?="105"
    lateinit var sharedPreferences: SharedPreferences
    lateinit var layoutManager:RecyclerView.LayoutManager
    lateinit var recyclerAdapter: CartAdapter
    lateinit var selectedItemsId:ArrayList<String>
    lateinit var txtprocessingorder:TextView
    var totalCost:String?="0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences=getSharedPreferences(getString(R.string.foodrunner_app),Context.MODE_PRIVATE)
        setContentView(R.layout.activity_cart)
        txtprocessingorder=findViewById(R.id.txtprocessingorder)
        txtprocessingorder.visibility=View.GONE
        user_id=sharedPreferences.getString("User_id","100")
        if (user_id=="100"){
            Toast.makeText(this,"User Id not found",Toast.LENGTH_SHORT).show()
        }
        recyclerCart=findViewById(R.id.recyclerCart)
        tool=findViewById(R.id.tool)

        btplaceorder=findViewById(R.id.btplaceorder)
        txtrestrauName=findViewById(R.id.txtrestrauName)
        txtrestrauName.text=intent.getStringExtra("resName")
         totalCost=intent.getStringExtra("totalCoast")
        if (totalCost=="0"){
            Toast.makeText(this,"totalCost is not correct",Toast.LENGTH_SHORT).show()
        }
        res_id=intent.getStringExtra("resId")
        if (res_id=="5"){
            Toast.makeText(this,"Restaurant Id not found",Toast.LENGTH_SHORT).show()
        }
        selectedItemsId= intent.getStringArrayListExtra("selectedItemsId")
        btplaceorder.text= "Place Order($totalCost)"
        layoutManager=LinearLayoutManager(this)
        setUpToolbar()

        dish= RetreivDish(this).execute().get()
        recyclerAdapter= CartAdapter(this,dish)
        recyclerCart.adapter= recyclerAdapter
        recyclerCart.layoutManager=layoutManager
        val jsonobject = JSONObject()
        try{
            val foodJsonArray = JSONArray()
            for (foodItem in selectedItemsId) {
                val singleItemObject = JSONObject()
                singleItemObject.put("food_item_id", foodItem)
                foodJsonArray.put(singleItemObject)
            }

            jsonobject.put("user_id", user_id)
            jsonobject.put("restaurant_id", res_id)
            jsonobject.put("total_cost", totalCost)
            jsonobject.put("food", foodJsonArray)
        }
        catch (e:JSONException){
            Toast.makeText(this,"Sorry please try again",Toast.LENGTH_LONG).show()
        }

        btplaceorder.setOnClickListener {
            txtprocessingorder.visibility=View.VISIBLE
            val queue = Volley.newRequestQueue(this)
            val url = "http://13.235.250.119/v2/place_order/fetch_result/"
            if (ConnectionManager().checkconnectivity(this)) {
                val jsonobjectrequest =
                    object :
                        JsonObjectRequest(Request.Method.POST, url, jsonobject, Response.Listener {
                            try {
                                txtprocessingorder.visibility=View.GONE
                                val dataobject = it.getJSONObject("data")
                                val success = dataobject.getBoolean("success")
                                if (success) {
                                    val intent = Intent(this, OrderPlacedActivity::class.java)
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(this, "Sorry!! your order not placed, please try again", Toast.LENGTH_LONG)
                                        .show()
                                }
                            } catch (e: JSONException) {
                                Toast.makeText(this, "Unexpected Error, please try again", Toast.LENGTH_LONG).show()

                            }
                        }, Response.ErrorListener {
                            Toast.makeText(this, "Low Internet connection please check your connection and try again", Toast.LENGTH_LONG).show()
                        }) {
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String, String>()
                            headers["Content-type"] = "application/json"
                            headers["token"] = "bc91c4800261ef"
                            return headers
                        }
                    }
                queue.add(jsonobjectrequest)
            } else {
                val dialogue = AlertDialog.Builder(this)
                dialogue.setTitle("Error")
                dialogue.setMessage("Internet Connection Not Found")
                dialogue.setPositiveButton("Open Setting") { text, listener ->
                    val setting = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(setting)
                    finish()

                }
                dialogue.setNegativeButton("Exit") { text, listener ->
                    ActivityCompat.finishAffinity(this)
                }
                dialogue.create()
                dialogue.show()
            }

        }

    }
    fun setUpToolbar(){
        setSupportActionBar(tool)
        supportActionBar?.title="My Cart"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
    override fun onBackPressed() {
        if (true){val dialogue = AlertDialog.Builder(this)
            dialogue.setTitle("Warning")
            dialogue.setMessage("If you press back then all cart item will delete")
            dialogue.setPositiveButton("Ok") { text, listener ->
                val intent=Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()

            }
            dialogue.setNegativeButton("Cancle") { text, listener ->
            }
            dialogue.create()
            dialogue.show()}
        }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id==android.R.id.home) {
            val dialogue = AlertDialog.Builder(this)
            dialogue.setTitle("Warning")
            dialogue.setMessage("If you press back then all cart item will delete")
            dialogue.setPositiveButton("Ok") { text, listener ->
                val intent=Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()

            }
            dialogue.setNegativeButton("Cancle") { text, listener ->
            }
            dialogue.create()
            dialogue.show()

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
    class RetreivDish(val context:Context):AsyncTask<Void,Void,List<DishEntity>>() {
        override fun doInBackground(vararg params: Void?): List<DishEntity> {
            val db = Room.databaseBuilder(context, DishDatabase::class.java, "Dishes-db")
                .build()
            return db.dishDao().getAllData()
        }
    }
}