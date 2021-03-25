package com.example.projekt_zespolowy_ezi

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RelativeLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /* ANIMACJA TLA */
        val layout: RelativeLayout = findViewById(R.id.main_layout)
        val animationDrawable = layout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(4000)
        animationDrawable.setExitFadeDuration(4000)
        animationDrawable.start()

        val enterExpenseButton = findViewById<Button>(R.id.enter_expense)
        val predictExpenseButton = findViewById<Button>(R.id.predict_expenses)

        enterExpenseButton.setOnClickListener {
            val enterExpenseIntent = Intent(this, EnterExpense::class.java)
            startActivity(enterExpenseIntent)
        }
        predictExpenseButton.setOnClickListener {
            val predictExpenseIntent = Intent(this, PredictExpense::class.java)
            startActivity(predictExpenseIntent)
        }

    }
}