package com.example.projekt_zespolowy_ezi.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.projekt_zespolowy_ezi.APIRequest
import com.example.projekt_zespolowy_ezi.animations.BackgroundAnimation
import com.example.projekt_zespolowy_ezi.R
import com.example.projekt_zespolowy_ezi.api.UserCategoryJSONItem
import com.example.projekt_zespolowy_ezi.classes.UserCategory
import com.example.projekt_zespolowy_ezi.constants.URL
import com.example.projekt_zespolowy_ezi.constants.LoggedUser
import com.example.projekt_zespolowy_ezi.database.ExpenseDBHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class EnterExpense : AppCompatActivity() {

    lateinit var  selectedCat: String
    //lateinit var selected: UserCategory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_expense)

        val layout: RelativeLayout = findViewById(R.id.enter_expense_layout)
        BackgroundAnimation.animateUI(layout)
        //categorySpinnerPop()
        getCategoriesSpinner()

    }
    fun newExpense(view: View){
        /**
         * Funkcja newExpense realizuje zapis podanego przez użytkownika wydatku do bazy danych.
         * Korzysta z handlera bazy danych ExpenseDBHandler
         */

        val enterExpense = findViewById<EditText>(R.id.enter_expense)

        //https://johncodeos.com/how-to-make-post-get-put-and-delete-requests-with-retrofit-using-kotlin/
        val retrofit = Retrofit.Builder().
            baseUrl(URL.BASE_URL).
            build()

        val service = retrofit.create(APIRequest::class.java)

        if(enterExpense.text.isNotEmpty() && enterExpense.text.toString().toFloat() > 0) {

            val expenseVal = enterExpense.text.toString()

            /*TIME https://grokonez.com/kotlin/kotlin-get-current-datetime */
            //val currentDateTime = LocalDateTime.now()
            //val date = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString()

            val jsonObject = JSONObject()
            jsonObject.put("user_id",LoggedUser.userId)
            jsonObject.put("value", expenseVal)
            jsonObject.put("category",selectedCat)


            val jsonObjectString = jsonObject.toString()
            Log.d("Object sent", jsonObjectString)

            val requestBody = jsonObjectString.toRequestBody("application/json;charset=UTF-8".toMediaTypeOrNull())

            CoroutineScope(Dispatchers.IO).launch {
                val response = service.addExpense2(requestBody)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@EnterExpense, "Zapisano wydatek " + expenseVal + "zł", Toast.LENGTH_SHORT).show()
                        Log.d("RETROFIT SUCCESS, SENT REQUEST", jsonObjectString)
                    } else {
                        Toast.makeText(this@EnterExpense, jsonObjectString, Toast.LENGTH_SHORT).show()
                        Log.e("RETROFIT_ERROR", response.code().toString())
                    }
                }
            }
            enterExpense.text.clear()
        }else{
            Toast.makeText(this, "Wprowadź poprawną wartość", Toast.LENGTH_SHORT).show()
            enterExpense.text.clear()
        }
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
                    val adapter = ArrayAdapter(this@EnterExpense, R.layout.spinner_selected_layout, ArrayCat)
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

            override fun onFailure(call: Call<List<UserCategoryJSONItem>?>, t: Throwable) {
                Toast.makeText(this@EnterExpense,"Coś poszło nie tak",Toast.LENGTH_LONG).show()
            }
        })

    }

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
}

