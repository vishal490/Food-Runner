package com.vishal.foodrunner.activity

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.vishal.foodrunner.R

class OrderPlacedActivity : AppCompatActivity() {
    lateinit var btok:Button
    lateinit var song:MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_placed)
        btok=findViewById(R.id.btok)
        song= MediaPlayer.create(this,R.raw.orderplaced)
        song.start()
        btok.setOnClickListener{
            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onPause() {
        super.onPause()
        song.release()
        finish()
    }
}