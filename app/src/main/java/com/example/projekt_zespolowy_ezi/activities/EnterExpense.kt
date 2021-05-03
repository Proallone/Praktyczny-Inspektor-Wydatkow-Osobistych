package com.example.projekt_zespolowy_ezi.activities


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.projekt_zespolowy_ezi.APIRequest
import com.example.projekt_zespolowy_ezi.animations.BackgroundAnimation
import com.example.projekt_zespolowy_ezi.R
import com.example.projekt_zespolowy_ezi.classes.UserCategory
import com.example.projekt_zespolowy_ezi.classes.UserExpense
import com.example.projekt_zespolowy_ezi.constants.url
import com.example.projekt_zespolowy_ezi.database.ExpenseDBHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import retrofit2.Retrofit




class EnterExpense : AppCompatActivity() {

    lateinit var  selectedCat: String
    //lateinit var selected: UserCategory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_expense)

        val layout: RelativeLayout = findViewById(R.id.enter_expense_layout)
        BackgroundAnimation.animateUI(layout)
        categorySpinnerPop()
    }
    fun newExpense(view: View){
        /**
         * Funkcja newExpense realzuje zapis podanego przez użytkownika wydatku do bazy danych.
         * Korzysta z handlera bazy danych ExpenseDBHandler
         */

        val enterExpense = findViewById<EditText>(R.id.enter_expense)

        //https://johncodeos.com/how-to-make-post-get-put-and-delete-requests-with-retrofit-using-kotlin/
        val retrofit = Retrofit.Builder().
            baseUrl(url.BASE_URL).
            build()

        val service = retrofit.create(APIRequest::class.java)

        if(enterExpense.text.isNotEmpty() && enterExpense.text.toString().toFloat() > 0) {

            val expenseVal = enterExpense.text.toString()
            /*TIME https://grokonez.com/kotlin/kotlin-get-current-datetime */
            val currentDateTime = LocalDateTime.now()
            val date = currentDateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")).toString()
            //val utl = URL(url.BASE_URL)

            val jsonObject = JSONObject()
            jsonObject.put("value", expenseVal)
            jsonObject.put("category",selectedCat)
            jsonObject.put("date",date)

            val jsonObjectString = jsonObject.toString()
            Log.d("Object", jsonObjectString)

            val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
            Toast.makeText(this,jsonObjectString, Toast.LENGTH_LONG).show()

            CoroutineScope(Dispatchers.IO).launch {
                val response = service.addExpense2(requestBody)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {

                    } else {
                        Log.e("RETROFIT_ERROR", response.code().toString())
                    }
                }
            }
            enterExpense.text.clear()
        }else{
            Toast.makeText(this, "Wprowadź poprawną wartość", Toast.LENGTH_SHORT).show()
            enterExpense.text.clear()
        }
        //viewExpenses(view)
    }
    /*
    fun newExpense(view: View){
        /**
         * Funkcja newExpense realzuje zapis podanego przez użytkownika wydatku do bazy danych.
         * Korzysta z handlera bazy danych ExpenseDBHandler
         */
        val dbHandler = ExpenseDBHandler(this, null, null, 1)

        val enterExpense = findViewById<EditText>(R.id.enter_expense)

        if(enterExpense.text.isNotEmpty() && enterExpense.text.toString().toFloat() > 0) {

            val expenseVal = enterExpense.text.toString()
            /*TIME https://grokonez.com/kotlin/kotlin-get-current-datetime */
            val currentDateTime = LocalDateTime.now()
            //val date = currentDateTime.format(DateTimeFormatter.ISO_DATE).toString()
            val date = currentDateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")).toString()
            val expense = UserExpense(expenseVal, selectedCat, date)
            dbHandler.addExpense(expense)
            enterExpense.text.clear()
            //val expenseRounded = String.format("%0.2f", expenseVal)
            Toast.makeText(this, "Zapisano wydatek " + expenseVal + "zł", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "Wprowadź poprawną wartość", Toast.LENGTH_SHORT).show()
            enterExpense.text.clear()
        }
        //viewExpenses(view)
    }
*/
    /*
    fun viewExpenses(view: View) {

        val expensesList = findViewById<ListView>(R.id.expenses_list)

        val dbHandler = ExpenseDBHandler(this, null, null, 1)
        val expenses: List<UserExpense> = dbHandler.readAllExpenses()

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

        val expSumVal = expArrayVal.map { it.toFloat() }
        var sumExp = 0F

        expSumVal.forEach{ it->
            sumExp+=it
        }

        SummaryExpenses.summary = sumExp
        //Toast.makeText(this, sumExp.toString(), Toast.LENGTH_SHORT).show()
        //Toast.makeText(this,expensesList.toString(),Toast.LENGTH_LONG).show()

    }
*/
    fun categorySpinnerPop(){
        /**
         * Funkcja realizująca wypełnienie listy dostepnych kategorii przy użyciu zdefiniowanych przez użytkownika
         * kategorii wydatków
         */
        val categorySpinner = findViewById<Spinner>(R.id.expense_category_spinner)
        val dbHandler = ExpenseDBHandler(this, null, null, 1)
        val categories: List<UserCategory> = dbHandler.readAllCategory()
        val ArrayID = Array(categories.size){0}
        val ArrayCat = Array(categories.size){"null"}
        var index = 0

        for(c in categories){
            ArrayID[index] = c.id
            ArrayCat[index] = c.category.toString()
            index++
        }

        if (categorySpinner != null){
            val adapter = ArrayAdapter(this, R.layout.spinner_selected_layout, ArrayCat)
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout)
            categorySpinner.adapter = adapter
        }

        categorySpinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedCat = ArrayCat[position]
                val selected = ArrayID[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
    }
    fun summarizeExpenses(): Float {
        val dbHandler = ExpenseDBHandler(this, null, null, 1)
        val expenses: List<UserExpense> = dbHandler.readAllExpenses()

        val expArrayID = Array<String>(expenses.size){"0"}
        val expArrayVal = Array<String>(expenses.size){"null"}
        val expArrayCat = Array<String>(expenses.size){"null"}
        val expArrayDate = Array<String>(expenses.size){"null"}

        var index = 0
        var sumExp = 0F

        for(e in expenses){
            expArrayID[index] = e.id.toString()
            expArrayVal[index] = e.value.toString()
            expArrayCat[index] = e.category.toString()
            expArrayDate[index] = e.date.toString()
            sumExp+=expArrayVal[index].toFloat()
            index++
        }
        return sumExp
    }
}

