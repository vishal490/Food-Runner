package com.vishal.foodrunner.fragments


import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.vishal.bookhub.util.ConnectionManager
import com.vishal.foodrunner.modul.Rest
import com.vishal.foodrunner.R
import com.vishal.foodrunner.adapter.HomeAdapter
import org.json.JSONException
import java.util.*
import kotlin.collections.HashMap


class Home : Fragment() {
    lateinit var recyclerDashboard: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    val resList= arrayListOf<Rest>()
    lateinit var recyclerAdapter: HomeAdapter
    lateinit var rlProgress: RelativeLayout
    lateinit var pbProgress: ProgressBar
    var ratingcompartor= Comparator<Rest>(){book1,book2->
        if(book1.resrating.compareTo(book2.resrating,true)==0){
            book1.resname.compareTo(book2.resname,true)
        }else{
            book1.resrating.compareTo(book2.resrating,true)
        }
    }
    var pricecompartor1= Comparator<Rest>(){res1,res2->
        if(res1.resprice.compareTo(res2.resprice,true)==0){
            res1.resname.compareTo(res2.resname,true)
        }else{
            res1.resprice.compareTo(res2.resprice,true)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =inflater.inflate(R.layout.fragment_home, container, false)
        recyclerDashboard = view.findViewById(R.id.recyclerDashboard)
        rlProgress=view.findViewById(R.id.rlProgress)
        pbProgress=view.findViewById(R.id.pbProgress)
        rlProgress.visibility=View.VISIBLE
        layoutManager = LinearLayoutManager(activity)
        setHasOptionsMenu(true)
        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/"
        if(ConnectionManager().checkconnectivity(activity as Context)) {

            val jsonobjectrequest=
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    try {
                        rlProgress.visibility=View.GONE
                        val resdatajsonobject=it.getJSONObject("data")
                        val success = resdatajsonobject.getBoolean("success")
                        if (success) {
                            val data = resdatajsonobject.getJSONArray("data")
                            for (i in 0 until data.length()) {
                                val resjsonobject = data.getJSONObject(i)
                                val resobject = Rest(
                                    resjsonobject.getString("id"),
                                    resjsonobject.getString("name"),
                                    resjsonobject.getString("rating"),
                                    resjsonobject.getString("cost_for_one"),
                                    resjsonobject.getString("image_url")
                                )
                                resList.add(resobject)
                                recyclerAdapter = HomeAdapter(activity as Context, resList)
                                recyclerDashboard.adapter = recyclerAdapter
                                recyclerDashboard.layoutManager = layoutManager
                            }
                        } else {
                            Toast.makeText(context, "Some Error Ouccured!! try again later", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }catch (e:JSONException){
                        Toast.makeText(context,"Some Unexpected errror ouccer", Toast.LENGTH_SHORT).show()
                    }
                },Response.ErrorListener {
                    Toast.makeText(context,"Low Internet connection, please check your connection and try again", Toast.LENGTH_LONG).show()
                }){

                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "bc91c4800261ef"
                        return headers
                    }
                }
            queue.add(jsonobjectrequest)
        }else{
            val dialogue = AlertDialog.Builder(activity as Context)
            dialogue.setTitle("Error")
            dialogue.setMessage("Internet Connection Not Found")
            dialogue.setPositiveButton("Open Setting") { text, listener ->
                val setting= Intent(Settings.ACTION_WIRELESS_SETTINGS)
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
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_sort,menu)
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id=item?.itemId
        if(id==R.id.icSort){
            Collections.sort(resList,ratingcompartor)
            resList.reverse()
        }else if (id==R.id.icSortByPriceHighToLow){
            Collections.sort(resList,pricecompartor1)
            resList.reverse()
        }else if (id==R.id.icSortByPriceLowToHigh){
            Collections.sort(resList,pricecompartor1)
        }

        return super.onOptionsItemSelected(item)
    }
}
