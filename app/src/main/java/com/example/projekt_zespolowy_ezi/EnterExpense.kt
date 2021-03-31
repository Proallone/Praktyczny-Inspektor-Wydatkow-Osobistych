package com.example.projekt_zespolowy_ezi

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class EnterExpense : AppCompatActivity() {

    lateinit var  selectedCat: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.enter_expense)

        val layout: RelativeLayout = findViewById(R.id.enter_expense_layout)
        val categorySpinner = findViewById<Spinner>(R.id.expense_category_spinner)
        val expenseCategories = resources.getStringArray(R.array.expense_category_array)

        animateUI(layout)

        if (categorySpinner != null){
            val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item, expenseCategories)
            categorySpinner.adapter = adapter
        }

       categorySpinner.onItemSelectedListener = object :
               AdapterView.OnItemSelectedListener {
           override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
               selectedCat = expenseCategories[position]
               Toast.makeText(this@EnterExpense,
                       getString(R.string.selected_category) + " " +
                               "" + expenseCategories[position], Toast.LENGTH_SHORT).show()
           }

           override fun onNothingSelected(parent: AdapterView<*>) {
               // write code to perform some action
           }
       }
    }
    fun newExpense(view: View){
        val dbHandler = ExpenseDBHandler(this, null, null, 1)

        val enterExpense = findViewById<EditText>(R.id.enter_expense)

        if(enterExpense.text.isNotEmpty() && enterExpense.text.toString().toFloat() > 0) {

            val expenseVal = enterExpense.text.toString().toFloat()
            val currentDateTime = LocalDateTime.now()
            val date = currentDateTime.format(DateTimeFormatter.ISO_DATE).toString()
            val expense = UserExpense(expenseVal, selectedCat, date)
            dbHandler.addExpense(expense)
            enterExpense.text.clear()
            //val expenseRounded = String.format("%0.2f", expenseVal)
            Toast.makeText(this, "Zapisano wydatek " + expenseVal + "zł", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "Wprowadź poprawną wartość", Toast.LENGTH_SHORT).show()
            enterExpense.text.clear()
        }
    }

    /*fun lookupProduct(view: View) {
        val dbHandler = ExpenseDBHandler(this, null, null, 1)

        val product = dbHandler.findExpense(
            expenseDate.text.toString())

        if (product != null) {
            productID.text = product.id.toString()

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

