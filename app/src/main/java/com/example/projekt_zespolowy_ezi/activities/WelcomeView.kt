package com.example.projekt_zespolowy_ezi.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.projekt_zespolowy_ezi.R
import com.example.projekt_zespolowy_ezi.animations.BackgroundAnimation


//REF https://www.geeksforgeeks.org/how-to-create-an-animated-splash-screen-in-android/

/**
 * Plik źródłowy ekranu powitania użtykownika powiajaący się w momencie uruchomienia aplikacji.
 * Funkcja onCreate odpowiada za ustawienie właściwego layoutu oraz rozpoczęcie animacji tła.
 * Funkcja welcomeAnimation odpowiada za wyświetlenie oraz wykonanie animacji logo programu wraz z napisem.
 */

@Suppress("DEPRECATION")
class WelcomeView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_view)
        val layout : RelativeLayout = findViewById(R.id.activity_welcome_layout)
        val loginButton = findViewById<Button>(R.id.enter_login)
        val signupButton = findViewById<Button>(R.id.enter_signup)

        loginButton.setVisibility(View.GONE)
        signupButton.setVisibility(View.GONE)

        BackgroundAnimation.animateUI(layout)

        welcomeAnimation()

        Handler().postDelayed({
            loginButton.setVisibility(View.VISIBLE)
            signupButton.setVisibility(View.VISIBLE)
        }, 2000) // 2000 opoźnienie wyświetlenia przycisków.
        loginButton.setOnClickListener {
            enterLogin()
        }
        signupButton.setOnClickListener {
            enterSignup()
        }
    }
    fun enterLogin(){
        /*Funkcja odpowiadająca za przejście do activity logowania*/
        val startLogin = Intent(this, LogInActivity::class.java)
        startActivity(startLogin)
    }
    fun enterSignup(){
        /*Funkcja odpowiadająca za przejście do activity rejestracji*/
        val startSignup = Intent(this, SignUpActivity::class.java)
        startActivity(startSignup)
    }
    private fun welcomeAnimation(){
        /*Funkcja odpowiadająca za animację ekranu powitania*/

        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val backgroundImage: ImageView = findViewById(R.id.WelcomeViewImage)
        val name_p: TextView = findViewById(R.id.name_p)
        val name_i: TextView = findViewById(R.id.name_i)
        val name_w: TextView = findViewById(R.id.name_w)
        val name_o: TextView = findViewById(R.id.name_o)
        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.animation_slide_from_left)
        backgroundImage.startAnimation(slideAnimation)
        name_p.startAnimation(slideAnimation)
        name_i.startAnimation(slideAnimation)
        name_w.startAnimation(slideAnimation)
        name_o.startAnimation(slideAnimation)
    }

}