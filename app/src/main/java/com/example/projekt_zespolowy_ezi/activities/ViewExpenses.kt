package com.example.projekt_zespolowy_ezi.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.projekt_zespolowy_ezi.APIRequest
import com.example.projekt_zespolowy_ezi.R
import com.example.projekt_zespolowy_ezi.adapters.ExpenseListAdapter
import com.example.projekt_zespolowy_ezi.animations.BackgroundAnimation
import com.example.projekt_zespolowy_ezi.api.UserCategoryJSONItem
import com.example.projekt_zespolowy_ezi.api.UserExpenseJSONItem
import com.example.projekt_zespolowy_ezi.constants.LoggedUser
import com.example.projekt_zespolowy_ezi.constants.URL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ViewExpenses : AppCompatActivity() {
    lateinit var  selectedCat: String
    override fun onCreate(savedInstanceState: Bundle?) {
        /*Funkcja wykonywana w momencie pierwszego wywołania activity */
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_expense)
        title = "Witaj " + LoggedUser.userName + "!"
        val layout: RelativeLayout = findViewById(R.id.expenses_by_category_layout)
        val selectedItem: ListView = findViewById(R.id.expenses_overview_listview)
        val enterGraphsButton = findViewById<Button>(R.id.button_graphs)


        enterGraphsButton.setOnClickListener {
            enterGraphsExpense()
        }

        BackgroundAnimation.animateUI(layout)
        getCategoriesSpinner()

        selectedItem.setOnItemClickListener { _, _, position, _ ->
            val sel: String = selectedItem.getItemAtPosition(position) as String
        }
    }
    private fun enterGraphsExpense(){
        /*Funkcja odpowiadająca za przejście do activity wprowadzenia wydatków*/
        val enterExpenseGraphsIntent = Intent(this, ExpensesGraphs::class.java)
        startActivity(enterExpenseGraphsIntent)
    }

    override fun onResume() {
        super.onResume()
    }

    private fun getExpensesByCat(cat : String){
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(URL.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIRequest::class.java)

        val expensesList = findViewById<ListView>(R.id.expenses_overview_listview)
        val expensesSummary = findViewById<TextView>(R.id.summary_value)
        val response = retrofitBuilder.userAllExpenses(LoggedUser.userId!!, cat)

        response.enqueue(object : Callback<List<UserExpenseJSONItem>?> {
            @SuppressLint("SetTextI18n")
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
                val expArrayDel = Array<Int>(responseBody.size){0}
                var sumExp = 0.0F
                var index = 0
                var expF = ""
                for(e in responseBody){
                    if (e.category == cat){
                    expArrayID[index] = e.id.toString()
                    expF=String.format("%.2f", e.value.toFloat())
                    expArrayVal[index] = expF.toString()
                    expArrayCat[index] = e.category
                    expArrayDate[index] = e.date
                    expArrayDel[index] = e.deleted
                        sumExp+=expArrayVal[index].toFloat()
                    }
                    index++
                }
                val sumExpS=String.format("%.2f", sumExp)
                expensesSummary.text = sumExpS + "zł"
                val expListAdapter = ExpenseListAdapter(this@ViewExpenses,expArrayID,expArrayVal,expArrayCat,expArrayDate,expArrayDel)
                expensesList.adapter = expListAdapter
            }

            override fun onFailure(call: Call<List<UserExpenseJSONItem>?>, t: Throwable) {
                Toast.makeText(this@ViewExpenses,"Coś poszło nie tak",Toast.LENGTH_LONG).show()
            }
        })

    }
    fun getCategoriesSpinner(){
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(URL.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIRequest::class.java)

        val response = retrofitBuilder.userCategories(LoggedUser.userId!!)
        val categorySpinner = findViewById<Spinner>(R.id.expense_category_spinner)

        response.enqueue(object : Callback<List<UserCategoryJSONItem>?> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<List<UserCategoryJSONItem>?>,
                response: Response<List<UserCategoryJSONItem>?>
            ) {
                val responseBody = response.body()!!
                /*
                Pola poniżej służą do populacji adaptera
                 */
                val ArrayID = Array<String>(responseBody.size){"0"}
                val ArrayCat = Array<String>(responseBody.size){"null"}
                val ArrayDate = Array<String>(responseBody.size){"null"}
                val ArrayDel = Array<Int>(responseBody.size){0}
                var index = 0

                for(e in responseBody){
                    ArrayID[index] = e.id.toString()
                    ArrayCat[index] = e.category.toString()
                    ArrayDate[index] = e.date
                    ArrayDel[index] = e.deleted
                    index++
                }
                if (categorySpinner != null){
                    val adapter = ArrayAdapter(this@ViewExpenses, R.layout.spinner_selected_layout, ArrayCat)
                    adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout)
                    categorySpinner.adapter = adapter
                }
                categorySpinner.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                        selectedCat = ArrayCat[position]
                        //val selected = ArrayID[position]
                        getExpensesByCat(selectedCat)
                    }
                    override fun onNothingSelected(parent: AdapterView<*>) {
                        // write code to perform some action
                    }
                }
            }

            override fun onFailure(call: Call<List<UserCategoryJSONItem>?>, t: Throwable) {
                Toast.makeText(this@ViewExpenses,"Coś poszło nie tak",Toast.LENGTH_LONG).show()
            }
        })

    }
}