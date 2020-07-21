package com.vishal.foodrunner.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.*
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.vishal.bookhub.util.ConnectionManager
import com.vishal.foodrunner.R
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
     lateinit var imgLogo:ImageView
    lateinit var etNumber:EditText
    lateinit var etPassword:EditText
    lateinit var btLogin:Button
    lateinit var txtForgetPassword:TextView
    lateinit var txtRegister:TextView
    lateinit var sharedPreference:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference=getSharedPreferences(getString(R.string.foodrunner_app), Context.MODE_PRIVATE)
        val isLoggedIn=sharedPreference.getBoolean("isLogin",false)
        setContentView(R.layout.activity_login)
        if(isLoggedIn){
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        imgLogo=findViewById(R.id.imgLogo)
        etNumber=findViewById(R.id.etNumber)
        etPassword=findViewById(R.id.etPassword)
        btLogin=findViewById(R.id.btLogin)
        txtForgetPassword=findViewById(R.id.txtForgetPassword)
        txtRegister=findViewById(R.id.txtRegister)
        val number=etNumber.text
        val password=etPassword.text
        btLogin.setOnClickListener {
            val queue = Volley.newRequestQueue(this)
            val url = "http://13.235.250.119/v2/login/fetch_result"
            val jsonobject = JSONObject()
            jsonobject.put("mobile_number", number)
            jsonobject.put("password", password)
            if (ConnectionManager().checkconnectivity(this)) {
                val jsonobjectrequest =
                    object :JsonObjectRequest(Request.Method.POST, url, jsonobject, Response.Listener {
                        try {
                           val dataobject=it.getJSONObject("data")
                            val success = dataobject.getBoolean("success")
                            if (success) {
                                val data = dataobject.getJSONObject("data")
                                val name = data.getString("name")
                                val userid = data.getString("user_id")
                                val email = data.getString("email")
                                val mobilenumber = data.getString("mobile_number")
                                val address = data.getString("address")
                                savePreference(name, mobilenumber, email, address, userid)
                                val intent=Intent(this,MainActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this, "Wrong Mobile Number or Password", Toast.LENGTH_LONG).show()
                            }
                        } catch (e: JSONException) {
                            Toast.makeText(this, "Unexpected Error", Toast.LENGTH_LONG).show()

                        }
                    }, Response.ErrorListener {
                        Toast.makeText(this, "Low Internet Connection please check your connection and try again", Toast.LENGTH_LONG).show()
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
        txtRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        txtForgetPassword.setOnClickListener {
            val intent = Intent(this, ForgetActivity::class.java)
            startActivity(intent)
        }
    }
    fun savePreference(name:String,number:String,email:String,address:String,userid:String){
        sharedPreference.edit().putBoolean("isLogin",true).apply()
        sharedPreference.edit().putString("Name",name).apply()
        sharedPreference.edit().putString("Number",number).apply()
        sharedPreference.edit().putString("Email",email).apply()
        sharedPreference.edit().putString("Address",address).apply()
        sharedPreference.edit().putString("User_id",userid).apply()

    }
    override fun onPause() {
        super.onPause()
        finish()
    }
}
