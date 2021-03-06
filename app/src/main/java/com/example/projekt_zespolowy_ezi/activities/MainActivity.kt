package com.example.projekt_zespolowy_ezi.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.projekt_zespolowy_ezi.APIRequest
import com.example.projekt_zespolowy_ezi.R
import com.example.projekt_zespolowy_ezi.adapters.ExpenseListAdapter
import com.example.projekt_zespolowy_ezi.animations.BackgroundAnimation
import com.example.projekt_zespolowy_ezi.api.UserExpenseJSONItem
import com.example.projekt_zespolowy_ezi.constants.URL
import com.example.projekt_zespolowy_ezi.constants.LoggedUser
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


/**
 * Plik źródłowy głównej aktywności aplikacji, odpowiada za prawidłowe przechodzenie pomiędzy dodatkowymi
 * aktywnościami.
 */

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        /*Funkcja wykonywana w momencie pierwszego wywołania activity */
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Witaj " + LoggedUser.userName + "!"
        val layout: RelativeLayout = findViewById(R.id.main_layout)
        val selectedItem: ListView = findViewById(R.id.expenses_overview_listview)
        //setStatusBarGradiant(this)
        BackgroundAnimation.animateUI(layout)
        getExpenses()

        val enterExpenseButton = findViewById<ImageButton>(R.id.enter_expense)
        val enterCategoryButton = findViewById<ImageButton>(R.id.enter_category)
        val enterProfileButton = findViewById<ImageButton>(R.id.user_profile)
        val enterSettingsButton = findViewById<ImageButton>(R.id.user_options)


        enterExpenseButton.setOnClickListener {
            enterActualExpense()
        }
        enterCategoryButton.setOnClickListener {
            enterNewCategory()
        }
        enterProfileButton.setOnClickListener {
            enterProfile()
        }
        enterSettingsButton.setOnClickListener {
            enterSettings()
        }


        selectedItem.setOnItemClickListener { _, _, position, _ ->
            val sel: String = selectedItem.getItemAtPosition(position) as String

            //Popup menu
            val popupMenu: PopupMenu = PopupMenu(this,selectedItem)
            popupMenu.menuInflater.inflate(R.menu.popup_menu,popupMenu.menu)

            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when(item.itemId) {
                    R.id.delete ->
                        Log.d("RETROFIT SUCCESS, EXPENSE ID" + sel + " REMOVED" ,"SUCCESS")
                }
                deleteExpense(sel.toInt())
                true
            })
            popupMenu.show()
        }
    }

//    @SuppressLint("UseCompatLoadingForDrawables")
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    fun setStatusBarGradiant(activity: Activity) {
//        val window: Window = activity.window
//        val background = activity.resources.getDrawable(R.drawable.background_gradient_puple_blue_horizontal)
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        window.statusBarColor = activity.resources.getColor(android.R.color.transparent)
//        window.navigationBarColor = activity.resources.getColor(android.R.color.transparent)
//        window.setBackgroundDrawable(background)
//    }


    override fun onResume() {
        super.onResume()
        getExpenses()
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
    private fun enterProfile(){
        /*Funkcja odpowiadająca za przejście do activity wprowadzenia wydatków*/
        val enterProfile= Intent(this, ViewExpenses::class.java)
        startActivity(enterProfile)
    }
    private fun enterSettings(){
        /*Funkcja odpowiadająca za przejście do activity wprowadzenia wydatków*/
        val enterSettings = Intent(this, Settings::class.java)
        startActivity(enterSettings)
    }

    //https://www.youtube.com/watch?v=-U8Hkec3RWQ&t=310s&ab_channel=CodePalace


    private fun getExpenses(){
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(URL.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIRequest::class.java)

        val expensesList = findViewById<ListView>(R.id.expenses_overview_listview)
        val expensesSummary = findViewById<TextView>(R.id.summary_value)
        val response = retrofitBuilder.userExpenses(LoggedUser.userId!!)

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
                    expArrayID[index] = e.id.toString()
                    expF=String.format("%.2f", e.value.toFloat())
                    expArrayVal[index] = expF.toString()
                    expArrayCat[index] = e.category
                    expArrayDate[index] = e.date
                    expArrayDel[index] = e.deleted
                    sumExp+=expArrayVal[index].toFloat()
                    index++
                }
                val sumExpS=String.format("%.2f", sumExp)
                expensesSummary.text = sumExpS + "zł"
                val expListAdapter = ExpenseListAdapter(this@MainActivity,expArrayID,expArrayVal,expArrayCat,expArrayDate,expArrayDel)
                expensesList.adapter = expListAdapter
            }

            override fun onFailure(call: Call<List<UserExpenseJSONItem>?>, t: Throwable) {
                Toast.makeText(this@MainActivity,"Coś poszło nie tak",Toast.LENGTH_LONG).show()
            }
        })

    }
    fun deleteExpense(expense_id: Int){
        /**
         * Funkcja odpowiedzialna za usunięcie wybranego wydatku z serwerowej bazy danych
         */
        //https://johncodeos.com/how-to-make-post-get-put-and-delete-requests-with-retrofit-using-kotlin/
        val retrofit = Retrofit.Builder().
        baseUrl(URL.BASE_URL).
        build()

        val service = retrofit.create(APIRequest::class.java)

            val jsonObject = JSONObject()
            jsonObject.put("id", expense_id)
            jsonObject.put("deleted", 1)

            val jsonObjectString = jsonObject.toString()
            Log.d("Object sent", jsonObjectString)

            val requestBody = jsonObjectString.toRequestBody("application/json;charset=UTF-8".toMediaTypeOrNull())

            CoroutineScope(Dispatchers.IO).launch {
                val response = service.deleteExpense(expense_id,requestBody)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@MainActivity, "Usunięto wydatek id:" + expense_id, Toast.LENGTH_SHORT).show()
                        getExpenses()
                        Log.d("RETROFIT SUCCESS, SENT REQUEST", jsonObjectString)
                    } else {
                        Toast.makeText(this@MainActivity, jsonObjectString, Toast.LENGTH_SHORT).show()
                        getExpenses()
                        Log.e("RETROFIT_ERROR", response.code().toString())
                    }
                }
            }
    }
}