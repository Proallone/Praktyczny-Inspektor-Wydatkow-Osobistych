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

        //val viewExpenseButton = findViewById<Button>(R.id.viewdata)




        animateUI(layout)

        if (categorySpinner != null){
            val adapter = ArrayAdapter(this, R.layout.spinner_selected_layout, expenseCategories)
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout)
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

            val expenseVal = enterExpense.text.toString()
            /*TIME https://grokonez.com/kotlin/kotlin-get-current-datetime */
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

    fun viewExpenses(view: View) {
        val expensesList = findViewById<ListView>(R.id.expenses_list)

        val dbHandler = ExpenseDBHandler(this, null, null, 1)
        val expenses: List<UserExpense> = dbHandler.findExpense()

        val expArrayID = Array<String>(expenses.size){"0"}
        val expArrayVal = Array<String>(expenses.size){"null"}
        val expArrayCat = Array<String>(expenses.size){"null"}
        val expArrayDate = Array<String>(expenses.size){"null"}

        var index = 0

        for(e in expenses){
            expArrayID[index] = e.id.toString()
            expArrayVal[index] = e.value.toString()
            expArrayCat[index] = e.category.toString()
            expArrayDate[index] = e.date.toString()
            index++
        }
        val expListAdapter = ExpenseListAdapter(this,expArrayID,expArrayVal,expArrayCat,expArrayDate)
        expensesList.adapter = expListAdapter
    }
    private fun animateUI(layout: RelativeLayout, EnterDuration: Int = 4000, ExitDuration: Int = 4000){
        /*Funkcja odpowiadająca za animację tła*/
        val animationDrawable = layout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(EnterDuration)
        animationDrawable.setExitFadeDuration(ExitDuration)
        animationDrawable.start()
    }
}

