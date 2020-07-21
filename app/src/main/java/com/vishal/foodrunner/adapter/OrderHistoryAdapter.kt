package com.vishal.foodrunner.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.vishal.bookhub.util.ConnectionManager
import com.vishal.foodrunner.modul.OrderDetail
import com.vishal.foodrunner.R
import com.vishal.foodrunner.database.DishEntity
import com.vishal.foodrunner.modul.ResDetail
import org.json.JSONException

class OrderHistoryAdapter(var context:Context,var list:ArrayList<OrderDetail>) :RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.order_history_one_view,parent,false)
        return OrderHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
     val text=list[position]
        holder.txtrestrauName.text=text.resName
        holder.txtdate.text=text.orderplacedat

        var layoutManager = LinearLayoutManager(context)
        var orderedItemAdapter: CartAdapter

        if (ConnectionManager().checkconnectivity(context)) {

            try {

                val orderItemsPerRestaurant=ArrayList<DishEntity>()

                val sharedPreferencess=context.getSharedPreferences(context.getString(R.string.foodrunner_app),Context.MODE_PRIVATE)

                val user_id=sharedPreferencess.getString("User_id","0")

                val queue = Volley.newRequestQueue(context)

                val url="http://13.235.250.119/v2/orders/fetch_result/$user_id"

                val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {

                        val responseJsonObjectData = it.getJSONObject("data")

                        val success = responseJsonObjectData.getBoolean("success")

                        if (success) {

                            val data = responseJsonObjectData.getJSONArray("data")

                            val fetechedRestaurantJsonObject = data.getJSONObject(position)
                            orderItemsPerRestaurant.clear()
                            val foodOrderedJsonArray=fetechedRestaurantJsonObject.getJSONArray("food_items")
                            for(j in 0 until foodOrderedJsonArray.length())
                            {
                                val eachFoodItem = foodOrderedJsonArray.getJSONObject(j)
                                val itemObject = DishEntity(
                                    eachFoodItem.getString("food_item_id").toInt(),
                                    eachFoodItem.getString("name"),
                                    eachFoodItem.getString("cost")
                                )
                                orderItemsPerRestaurant.add(itemObject)
                            }
                            orderedItemAdapter = CartAdapter(context,orderItemsPerRestaurant)
                            holder.recyclerView.adapter = orderedItemAdapter
                            holder.recyclerView.layoutManager = layoutManager
                        }
                    },
                    Response.ErrorListener {
                        Toast.makeText(context, "Some Error occurred!!!", Toast.LENGTH_SHORT).show()
                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "bc91c4800261ef"
                        return headers
                    }
                }
                queue.add(jsonObjectRequest)
            } catch (e: JSONException) {
                Toast.makeText(context, "Some Unexpected error occured!!!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun getItemCount(): Int {
        return  list.size
    }

    class OrderHistoryViewHolder(view: View):RecyclerView.ViewHolder(view){
        val txtrestrauName:TextView=view.findViewById(R.id.txtrestrauName)
        val recyclerView:RecyclerView=view.findViewById(R.id.recyclerHistroy)
        val txtdate:TextView=view.findViewById(R.id.txtdate)
    }
}