package com.vishal.foodrunner.activity

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.vishal.foodrunner.R

class Splash : AppCompatActivity() {
      lateinit var song:MediaPlayer
    lateinit var logo:ImageView
    lateinit var text1:TextView
    lateinit var leftanimation:Animation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        logo=findViewById(R.id.imgSplashLogo)
        text1=findViewById(R.id.txtFoodRunner)
        leftanimation=AnimationUtils.loadAnimation(this,R.anim.left_animation)
        logo.setAnimation(leftanimation)
        text1.setAnimation(leftanimation)
        song= MediaPlayer.create(this,R.raw.twinkle)
        song.start()
        Handler().postDelayed({
            var intent=Intent(this, LoginActivity::class.java)
            startActivity(intent)
        },2000)
    }

    override fun onPause() {
        super.onPause()
        song.release()
        finish()
    }
}
