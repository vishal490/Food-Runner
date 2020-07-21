package com.vishal.foodrunner.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.room.Room
import com.google.android.material.navigation.NavigationView
import com.vishal.foodrunner.R
import com.vishal.foodrunner.database.RestaurantDatabase
import com.vishal.foodrunner.database.RestrauEntity
import com.vishal.foodrunner.fragments.*

class MainActivity : AppCompatActivity() {
     lateinit var frame:FrameLayout
    lateinit var navigationView:NavigationView
    lateinit var toolbar:androidx.appcompat.widget.Toolbar
    lateinit var drawerLayout:DrawerLayout
    lateinit var coordinator:CoordinatorLayout
    lateinit var sharedPreferences: SharedPreferences
    lateinit var txtUserName:TextView
    lateinit var txtUserEmail:TextView
    lateinit var header:View
    var previousMenuItem: MenuItem? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences=getSharedPreferences(getString(R.string.foodrunner_app), Context.MODE_PRIVATE)
        setContentView(R.layout.activity_main)
        frame=findViewById(R.id.frame)
        toolbar=findViewById(R.id.toolbar)
        navigationView=findViewById(R.id.navigationView)
        drawerLayout=findViewById(R.id.drawerLayout)
        coordinator=findViewById(R.id.coordinator)
        header=navigationView.getHeaderView(0)
        txtUserName = header.findViewById(R.id.txtUserName);
        txtUserEmail= header.findViewById(R.id.txtUserEmail);
        setUpToolbar()
        openHome()

        txtUserName.text=sharedPreferences.getString("Name","Food Runner")
        txtUserEmail.text="+91-"+sharedPreferences.getString("Number","1111888888")
        val actionbarToggle=ActionBarDrawerToggle(this,drawerLayout,
            R.string.open_home,
            R.string.close_home
        )
        drawerLayout.addDrawerListener(actionbarToggle)
        actionbarToggle.syncState()
        navigationView.setNavigationItemSelectedListener{
            if(previousMenuItem!=null){
                previousMenuItem?.isChecked=false
            }
            it.isCheckable=true
            it.isChecked=true
            previousMenuItem=it
        when(it.itemId) {
            R.id.home -> {
                openHome()
            }
            R.id.favorites -> {
                val fragment= Favorites()
            supportFragmentManager.beginTransaction().replace(R.id.frame,fragment).commit()
            supportActionBar?.title="Favorites Restaurant"
                drawerLayout.closeDrawers()
            }
            R.id.myprofile -> {
            val fragment= MyProfle()
                supportFragmentManager.beginTransaction().replace(R.id.frame,fragment).commit()
                supportActionBar?.title="My Profile"
                drawerLayout.closeDrawers()
            }
            R.id.faqs ->{
                val fragment= FAQs()
                supportFragmentManager.beginTransaction().replace(R.id.frame,fragment).commit()
                supportActionBar?.title="FAQs"
                drawerLayout.closeDrawers()
            }
            R.id.logout ->{

                drawerLayout.closeDrawers()
                val dialogue = AlertDialog.Builder(this )
                dialogue.setTitle("Warnnig")
                dialogue.setMessage("Are you want to logout")
                dialogue.setPositiveButton("Yes") { text, listener ->
                    sharedPreferences.edit().clear().apply()
                    RetreivRestrau(this).execute()
                    val setting = Intent(this,LoginActivity::class.java)
                    startActivity(setting)
                    finish()

                }
                dialogue.setNegativeButton("Cancel") { text, listener ->
                    val intent1=Intent(this,MainActivity::class.java)
                    startActivity(intent1)
                }
                dialogue.create()
                dialogue.show()
            }
            R.id.orderhistory ->{
                val fragment=OrderHistory()
                supportFragmentManager.beginTransaction().replace(R.id.frame,fragment).commit()
                supportActionBar?.title="Order History"
                drawerLayout.closeDrawers()
            }
        }
        return@setNavigationItemSelectedListener true
        }
        }
    fun openHome(){
        var fragment= Home()
        supportFragmentManager.beginTransaction().replace(R.id.frame,fragment).commit()
        supportActionBar?.title="All Restaurants"
        navigationView.setCheckedItem(R.id.home)
        drawerLayout.closeDrawers()
    }
    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title="Tool Barr"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id==android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frame)
        when(frag){
            !is Home ->openHome()

            else->super.onBackPressed()
        }
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
    class RetreivRestrau(val context:Context): AsyncTask<Void, Void, Boolean>(){
        override fun doInBackground(vararg params: Void?): Boolean {
            val db= Room.databaseBuilder(context, RestaurantDatabase::class.java,"Restaurant-db").build()
            db.resDao().deleteAll()
            return true
        }

    }
}
