package com.example.projekt_zespolowy_ezi

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class EnterExpense : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.enter_expense)
        /* ANIMACJA TLA */
        val layout: RelativeLayout = findViewById(R.id.enter_expense_layout)

        animateUI(layout)

    }
    fun newExpense(view: View){
        val dbHandler = ExpenseDBHandler(this, null, null, 1)

        val enterExpense = findViewById<EditText>(R.id.enter_expense)

        val expenseVal = enterExpense.text.toString().toFloat()

        val currentDateTime = LocalDateTime.now()
        val date = currentDateTime.format(DateTimeFormatter.ISO_DATE).toString()

        val expense = UserExpense(expenseVal,date)

        dbHandler.addExpense(expense)

    }

    /*fun lookupProduct(view: View) {
        val dbHandler = ExpenseDBHandler(this, null, null, 1)

        val product = dbHandler.findExpense(
            expenseI.text.toString())

        if (product != null) {
            expense.text = product.id.toString()

            productQuantity.setText(
                product.quantity.toString())
        } else {
            productID.text = "No Match Found"
        }
    } */
    private fun animateUI(layout: RelativeLayout, EnterDuration: Int = 4000, ExitDuration: Int = 4000 ){
        /*Funkcja odpowiadająca za animację tła*/
        val animationDrawable = layout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(EnterDuration)
        animationDrawable.setExitFadeDuration(ExitDuration)
        animationDrawable.start()
    }
}

