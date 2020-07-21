package com.vishal.foodrunner.fragments


import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.vishal.bookhub.util.ConnectionManager
import com.vishal.foodrunner.modul.OrderDetail

import com.vishal.foodrunner.R
import com.vishal.foodrunner.adapter.OrderHistoryAdapter
import org.json.JSONException


class OrderHistory : Fragment() {
    lateinit var txtpreviousorder: TextView
    lateinit var recyclerCart: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    var listOfOrder = arrayListOf<OrderDetail>()
    lateinit var recyclerAdapter: OrderHistoryAdapter
    lateinit var sharedPreferences: SharedPreferences
    lateinit var rlProgress:RelativeLayout
    lateinit var rlNoOrderYet:RelativeLayout
    var user_id: String? = "100"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order_history, container, false)
        sharedPreferences = activity!!.getSharedPreferences(
            getString(R.string.foodrunner_app),
            Context.MODE_PRIVATE
        )
        user_id = sharedPreferences.getString("User_id", "1")
        if (user_id == "1" || user_id == "100") {
            Toast.makeText(activity, "Some error occur", Toast.LENGTH_LONG).show()
            activity?.finish()
        }
        rlNoOrderYet=view.findViewById(R.id.rlNoOrderYet)
        rlNoOrderYet.visibility=View.GONE
        rlProgress=view.findViewById(R.id.rlProgress)
        rlProgress.visibility=View.VISIBLE
        txtpreviousorder = view.findViewById(R.id.txtpreviousorder)
        recyclerCart = view.findViewById(R.id.recyclerCart)
        layoutManager = LinearLayoutManager(activity)
        recyclerCart.addItemDecoration(DividerItemDecoration(recyclerCart.context,(layoutManager as LinearLayoutManager).orientation))

        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/orders/fetch_result/$user_id"
        if (ConnectionManager().checkconnectivity(activity as Context)) {
            val jsonobjectrequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    try {
                        val resdatajsonobject = it.getJSONObject("data")
                        val success = resdatajsonobject.getBoolean("success")
                        if (success) {
                            val data = resdatajsonobject.getJSONArray("data")
                            if(data.length()==0){
                                rlNoOrderYet.visibility=View.VISIBLE
                                Toast.makeText(context, "No Orders Placed yet!!!",Toast.LENGTH_SHORT).show()
                            }else {
                                for (i in 0 until data.length()) {
                                    val resjsonobject = data.getJSONObject(i)
                                    val resobject = OrderDetail(
                                        resjsonobject.getString("order_id"),
                                        resjsonobject.getString("restaurant_name"),
                                        resjsonobject.getString("total_cost"),
                                        resjsonobject.getString("order_placed_at")
                                    )
                                    listOfOrder.add(resobject)
                                    recyclerAdapter =
                                        OrderHistoryAdapter(activity as Context, listOfOrder)
                                    recyclerCart.adapter = recyclerAdapter
                                    recyclerCart.layoutManager = layoutManager
                                }
                                rlProgress.visibility = View.GONE
                            }
                        } else {
                            Toast.makeText(context, "Some Error Ouccured!!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } catch (e: JSONException) {
                        Toast.makeText(context, "Some Unexpected errror ouccer", Toast.LENGTH_SHORT)
                            .show()
                    }
                }, Response.ErrorListener {
                    Toast.makeText(context, "${it} Error Ouccered", Toast.LENGTH_LONG).show()
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
            val dialogue = AlertDialog.Builder(activity as Context)
            dialogue.setTitle("Error")
            dialogue.setMessage("Internet Connection Not Found")
            dialogue.setPositiveButton("Open Setting") { text, listener ->
                val setting = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(setting)
                activity?.finish()

            }
            dialogue.setNegativeButton("Exit") { text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialogue.create()
            dialogue.show()
        }
        return view
    }
}
