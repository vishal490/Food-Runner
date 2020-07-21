package com.vishal.foodrunner.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.SpannableStringBuilder
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

class OTPActivity : AppCompatActivity() {
    lateinit var etOTP:EditText
    lateinit var etNewPassword:EditText
    lateinit var etConfirmNewPassword:EditText
    lateinit var btSubmit:Button
     lateinit var sharedPreferences:SharedPreferences
    lateinit var number:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences=getSharedPreferences(getString(R.string.foodrunner_app), Context.MODE_PRIVATE)
        setContentView(R.layout.activity_otp)
        etOTP=findViewById(R.id.etOTP)
        etNewPassword=findViewById(R.id.etNewPassword)
        etConfirmNewPassword=findViewById(R.id.etConfirmNewPassword)
        btSubmit=findViewById(R.id.btSubmit)
        number=findViewById(R.id.etMobileNumber)
        val num=number.text
        val password=etNewPassword.text
        val otp=etOTP.text
        btSubmit.setOnClickListener {
            val queue = Volley.newRequestQueue(this )
            val url = "http://13.235.250.119/v2/reset_password/fetch_result"
            val jsonobject = JSONObject()
            jsonobject.put("mobile_number", num)
            jsonobject.put("password", password)
            jsonobject.put("otp",otp)
            if (ConnectionManager().checkconnectivity(this)) {
                val jsonobjectrequest = object :
                    JsonObjectRequest(Request.Method.POST, url, jsonobject, Response.Listener {
                        try {
                            val data = it.getJSONObject("data")
                            val success = data.getBoolean("success")
                            if (success) {
                              val message=data.getString("successMessage")
                                Toast.makeText(this,message,Toast.LENGTH_LONG).show()
                                val intent=Intent(this,LoginActivity::class.java)
                                startActivity(intent)
                            } else {
                                val message=data.getString("successMessage")
                                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                            }
                        } catch (e: JSONException) {
                            Toast.makeText(this, "Unexpected Error try again", Toast.LENGTH_LONG).show()

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
                    ActivityCompat.finishAffinity(this)
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
