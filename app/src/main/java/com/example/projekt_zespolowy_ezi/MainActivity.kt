package com.example.projekt_zespolowy_ezi

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Projekt PIWO"
        val layout: RelativeLayout = findViewById(R.id.main_layout)

        animateUI(layout)

        val enterExpenseButton = findViewById<Button>(R.id.enter_expense)
        val predictExpenseButton = findViewById<Button>(R.id.predict_expenses)


        enterExpenseButton.setOnClickListener {
            enterActualExpense()
        }
        predictExpenseButton.setOnClickListener {
            saveActualExpense()
        }
    }
    fun enterActualExpense(){
        /*Funkcja odpowiadająca za przejście do activity wprowadzenia wydatków*/
        val enterExpenseIntent = Intent(this, EnterExpense::class.java)
        startActivity(enterExpenseIntent)
    }
    fun saveActualExpense(){
        /*Funkcja odpowiadająca za przejście do activity przewidywania wydatków*/
        val predictExpenseIntent = Intent(this, PredictExpense::class.java)
        startActivity(predictExpenseIntent)
    }
    private fun animateUI(layout: RelativeLayout, EnterDuration: Int = 4000, ExitDuration: Int = 4000 ){
        /*Funkcja odpowiadająca za animację tła*/
        val animationDrawable = layout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(EnterDuration)
        animationDrawable.setExitFadeDuration(ExitDuration)
        animationDrawable.start()
    }
  /*  inner class uiUpdate() : AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg params: Void?): String? {
            // ...
            return null
        }

        override fun onPreExecute() {
            super.onPreExecute()
            // ...
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

        }
    }*/
}