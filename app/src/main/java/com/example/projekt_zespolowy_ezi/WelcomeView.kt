package com.example.projekt_zespolowy_ezi

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

//REF https://www.geeksforgeeks.org/how-to-create-an-animated-splash-screen-in-android/

@Suppress("DEPRECATION")
class WelcomeView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_view)

        // This is used to hide the status bar and make
        // the splash screen as a full screen activity.
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

       welcomeAnimation()

        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000) // 3000 is the delayed time in milliseconds.
    }
    private fun welcomeAnimation(){
        /*Funkcja odpowiadająca za animację ekranu powitania*/
        val backgroundImage: ImageView = findViewById(R.id.WelcomeViewImage)
        val name_p: TextView = findViewById(R.id.name_p)
        val name_i: TextView = findViewById(R.id.name_i)
        val name_w: TextView = findViewById(R.id.name_w)
        val name_o: TextView = findViewById(R.id.name_o)
        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.side_slide)
        backgroundImage.startAnimation(slideAnimation)
        name_p.startAnimation(slideAnimation)
        name_i.startAnimation(slideAnimation)
        name_w.startAnimation(slideAnimation)
        name_o.startAnimation(slideAnimation)
    }

}