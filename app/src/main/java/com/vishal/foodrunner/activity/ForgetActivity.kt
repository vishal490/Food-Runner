package com.vishal.foodrunner.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.vishal.bookhub.util.ConnectionManager
import com.vishal.foodrunner.R
import org.json.JSONException
import org.json.JSONObject

class ForgetActivity : AppCompatActivity() {
    lateinit var etUEmail:EditText
    lateinit var etUNumber:EditText
    lateinit var btNext:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget)
        etUEmail=findViewById(R.id.etUEmail)
        etUNumber=findViewById(R.id.etUNumber)
        btNext=findViewById(R.id.btNext)
        val number=etUNumber.text
        val email=etUEmail.text
        btNext.setOnClickListener {

            var queue = Volley.newRequestQueue(this)
            val url = "http://13.235.250.119/v2/forgot_password/fetch_result"
            val jsonobject = JSONObject()
            jsonobject.put("mobile_number", number)
            jsonobject.put("email", email)
            if (ConnectionManager().checkconnectivity(this)) {
                var jsonobjectrequest = object :
                    JsonObjectRequest(Request.Method.POST, url, jsonobject, Response.Listener {
                        try {
                            val data = it.getJSONObject("data")
                            val success = data.getBoolean("success")
                            if (success) {
                               val first=data.getBoolean("first_try")

                                val intent=Intent(this,OTPActivity::class.java)
                                intent.putExtra("Number",number)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this, "Account not found,check your Mobile Number and Email", Toast.LENGTH_LONG).show()
                            }
                        } catch (e: JSONException) {
                            Toast.makeText(this, "Unexpected Error,please try again later", Toast.LENGTH_LONG).show()

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
                val dialogue = AlertDialog.Builder(this )
                dialogue.setTitle("Error")
                dialogue.setMessage("Internet Connection Not Found")
                dialogue.setPositiveButton("Open Setting") { text, listener ->
                    val setting = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(setting)
                    finish()

                }
                dialogue.setNegativeButton("Exit") { text, listener ->
                    ActivityCompat.finishAffinity(this )
                }
                dialogue.create()
                dialogue.show()
            }
        }

    }
    override fun onPause() {
        super.onPause()
        finish()
    }

}

