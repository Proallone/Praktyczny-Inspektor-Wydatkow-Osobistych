package com.example.projekt_zespolowy_ezi

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity

class EnterExpense : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.enter_expense)
        /* ANIMACJA TLA */
        val layout: RelativeLayout = findViewById(R.id.enter_expense_layout)
        val animationDrawable = layout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(4000)
        animationDrawable.setExitFadeDuration(4000)
        animationDrawable.start()
    }
    /*fun getExpense (view: View){
        val enterExpense = findViewById<EditText>(R.id.enter_expense)
        val saveExpenseButton = findViewById<Button>(R.id.save_expense)

        saveExpenseButton.setOnClickListener {
            val Expense = findViewById<EditText>(R.id.enter_expense)
            Toast.makeText(this,"Nie ma gdzie zapisać :(",Toast.LENGTH_LONG).show()
        }
    }*/
}
