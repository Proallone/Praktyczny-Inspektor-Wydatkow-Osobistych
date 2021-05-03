package com.example.projekt_zespolowy_ezi.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.projekt_zespolowy_ezi.APIRequest
import com.example.projekt_zespolowy_ezi.animations.BackgroundAnimation
import com.example.projekt_zespolowy_ezi.R
import com.example.projekt_zespolowy_ezi.adapters.ExpenseListAdapter
import com.example.projekt_zespolowy_ezi.api.UserExpenseJSONItem
import com.example.projekt_zespolowy_ezi.constants.url
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Plik źródłowy głównej aktywności aplikacji, odpowiada za prawidłowe przechodzenie pomiędzy dodatkowymi
 * aktywnościami.
 */

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        /*Funkcja wykonywana w momencie pierwszego wywołania activity */
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Projekt PIWO"
        val layout: RelativeLayout = findViewById(R.id.main_layout)
        val summaryView = findViewById<TextView>(R.id.summary_value)
        val expensesList = findViewById<ListView>(R.id.expenses_overview_listview)
        //SummaryExpenses.summary = summaryLoad.summarizeExpenses()
        //summaryView.text = SummaryExpenses.summary.toString()

        BackgroundAnimation.animateUI(layout)

        getAPIRequest()

        val enterExpenseButton = findViewById<Button>(R.id.enter_expense)
        val enterCategoryButton = findViewById<Button>(R.id.enter_category)

        enterExpenseButton.setOnClickListener {
            enterActualExpense()
        }
        enterCategoryButton.setOnClickListener {
            enterNewCategory()
        }
    }

    override fun onResume() {
        super.onResume()
        getAPIRequest()
    }

    private fun enterActualExpense(){
        /*Funkcja odpowiadająca za przejście do activity wprowadzenia wydatków*/
        val enterExpenseIntent = Intent(this, EnterExpense::class.java)
        startActivity(enterExpenseIntent)
    }
    private fun enterNewCategory(){
        /*Funkcja odpowiadająca za przejście do activity przewidywania wydatków*/
        val enterCategoryIntent = Intent(this, EnterCategory::class.java)
        startActivity(enterCategoryIntent)
    }

    //https://www.youtube.com/watch?v=-U8Hkec3RWQ&t=310s&ab_channel=CodePalace

    private fun getAPIRequest(){
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(url.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIRequest::class.java)



                val expensesList = findViewById<ListView>(R.id.expenses_overview_listview)
                val expensesSummary = findViewById<TextView>(R.id.summary_value)
                val response = retrofitBuilder.getAllExpenses()

                response.enqueue(object : Callback<List<UserExpenseJSONItem>?> {
                    override fun onResponse(
                        call: Call<List<UserExpenseJSONItem>?>,
                        response: Response<List<UserExpenseJSONItem>?>
                    ) {
                        val responseBody = response.body()!!
                        /*
                        Pola poniżej służą do populacji adaptera
                         */
                        val expArrayID = Array<String>(responseBody.size){"0"}
                        val expArrayVal = Array<String>(responseBody.size){"null"}
                        val expArrayCat = Array<String>(responseBody.size){"null"}
                        val expArrayDate = Array<String>(responseBody.size){"null"}
                        var sumExp = 0.0F
                        var index = 0

                        for(e in responseBody){
                            expArrayID[index] = e.id.toString()
                            expArrayVal[index] = e.value
                            expArrayCat[index] = e.category
                            expArrayDate[index]=e.date
                            sumExp+=expArrayVal[index].toFloat()
                            index++
                        }
                        expensesSummary.text = sumExp.toString() + "zł"
                        val expListAdapter = ExpenseListAdapter(this@MainActivity,expArrayID,expArrayVal,expArrayCat,expArrayDate)
                        expensesList.adapter = expListAdapter
                    }

                    override fun onFailure(call: Call<List<UserExpenseJSONItem>?>, t: Throwable) {
                        Toast.makeText(this@MainActivity,"Coś poszło nie tak",Toast.LENGTH_LONG).show()
                    }
                })

    }

}