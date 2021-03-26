package com.example.projekt_zespolowy_ezi

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class EnterExpense : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.enter_expense)
        /* ANIMACJA TLA */
        val layout: RelativeLayout = findViewById(R.id.enter_expense_layout)
        val animationDrawable = layout.background as AnimationDrawable

        animateUI(layout)

    }
    fun getExpense(view: View){
        val enterExpense = findViewById<EditText>(R.id.enter_expense)
        val saveExpenseButton = findViewById<Button>(R.id.save_expense)
        //val summary = findViewById<View>(R.id.summary) as TextView

        saveExpenseButton.setOnClickListener {
            /*val actualExpense = findViewById<EditText>(R.id.enter_expense)
            var actual: Float = actualExpense.text.toString().toFloat()
            summary.append(actual.toString())
            Expense.actualExpense = enterExpense.text.toString().toFloat()*/
            Toast.makeText(this, "Nie ma gdzie zapisać :(", Toast.LENGTH_LONG).show()
        }
    }
    private fun animateUI(layout: RelativeLayout, EnterDuration: Int = 4000, ExitDuration: Int = 4000 ){
        /*Funkcja odpowiadająca za animację tła*/
        val animationDrawable = layout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(EnterDuration)
        animationDrawable.setExitFadeDuration(ExitDuration)
        animationDrawable.start()
    }
}
