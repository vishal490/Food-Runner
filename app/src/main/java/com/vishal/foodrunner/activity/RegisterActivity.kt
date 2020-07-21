package com.vishal.foodrunner.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.vishal.bookhub.util.ConnectionManager
import com.vishal.foodrunner.R
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.HashMap

class RegisterActivity : AppCompatActivity() {
     lateinit var etName:EditText
    lateinit var etEmail:EditText
    lateinit var etNumber:EditText
    lateinit var etAddress:EditText
    lateinit var etPassword:EditText
    lateinit var etConfirmPassword:EditText
    lateinit var btRegister:Button
    lateinit var toolbar: Toolbar
    lateinit var sharedPreference:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference = this.getSharedPreferences(getString(R.string.foodrunner_app), Context.MODE_PRIVATE)
        setContentView(R.layout.activity_register)
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etAddress = findViewById(R.id.etAddress)
        etNumber = findViewById(R.id.etNumber)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btRegister = findViewById(R.id.btRegister)
        toolbar=findViewById(R.id.toolbar)
        setUpToolbar()
        var name=etName.text
        var email=etEmail.text
        var number=etNumber.text
        var address=etAddress.text
        var password=etPassword.text
        btRegister.setOnClickListener {

            val queue = Volley.newRequestQueue(this )
            val url = "http://13.235.250.119/v2/register/fetch_result"
            val jsonobject = JSONObject()
            jsonobject.put("name",name)
            jsonobject.put("mobile_number", number)
            jsonobject.put("password", password)
            jsonobject.put("address",address)
            jsonobject.put("email",email)
            if (ConnectionManager().checkconnectivity(this )) {
                val jsonobjectrequest = object :
                    JsonObjectRequest(Request.Method.POST, url, jsonobject, Response.Listener {
                        try {
                            val dataobject=it.getJSONObject("data")
                            val success = dataobject.getBoolean("success")
                            if (success) {
                                val data = dataobject.getJSONObject("data")
                                val  name = data.getString("name")
                                val userid = data.getString("user_id")
                                val email = data.getString("email")
                                val mobilenumber = data.getString("mobile_number")
                                val address = data.getString("address")
                                savePreference(name, mobilenumber, email, address, userid)
                                val intent=Intent(this,MainActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this, "Please fill unique Email, Mobile Number and Password", Toast.LENGTH_LONG).show()
                            }
                        } catch (e: JSONException) {
                            Toast.makeText(this, "Unexpected Error please try again", Toast.LENGTH_LONG).show()

                        }
                    }, Response.ErrorListener {
                        Toast.makeText(this, "Low Internet connection please check the connection and try again", Toast.LENGTH_LONG).show()
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
    fun savePreference(name:String,number:String,email:String,address:String,userid:String){
        sharedPreference.edit().putBoolean("isLogin",true).apply()
        sharedPreference.edit().putString("Name",name).apply()
        sharedPreference.edit().putString("Number",number).apply()
        sharedPreference.edit().putString("Email",email).apply()
        sharedPreference.edit().putString("Address",address).apply()
        sharedPreference.edit().putString("User_id",userid).apply()

    }
    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title="Registration"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

}
