package com.example.projekt_zespolowy_ezi.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.projekt_zespolowy_ezi.APIRequest
import com.example.projekt_zespolowy_ezi.R
import com.example.projekt_zespolowy_ezi.adapters.CategoryListAdapter
import com.example.projekt_zespolowy_ezi.adapters.ExpenseListAdapter
import com.example.projekt_zespolowy_ezi.animations.BackgroundAnimation
import com.example.projekt_zespolowy_ezi.api.UserCategoryJSON
import com.example.projekt_zespolowy_ezi.api.UserCategoryJSONItem
import com.example.projekt_zespolowy_ezi.api.UserExpenseJSONItem
import com.example.projekt_zespolowy_ezi.classes.UserCategory
import com.example.projekt_zespolowy_ezi.constants.URL
import com.example.projekt_zespolowy_ezi.database.ExpenseDBHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.get
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class EnterCategory : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_category)
        val layout: RelativeLayout = findViewById(R.id.enter_category_layout)
        val selectedItem: ListView = findViewById(R.id.category_list)

        BackgroundAnimation.animateUI(layout)
        getAllCategories()


        selectedItem.setOnItemClickListener { parent, view, position, id ->
            val sel: String = selectedItem.getItemAtPosition(position) as String

            //Popup menu
            val popupMenu: PopupMenu = PopupMenu(this,selectedItem)
            popupMenu.menuInflater.inflate(R.menu.popup_menu,popupMenu.menu)

            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when(item.itemId) {
                    R.id.delete ->
                        Log.d("RETROFIT SUCCESS, CATEGORY ID" + sel + " REMOVED" ,"SUCCESS")
                }
                deleteCategory(sel.toInt())
                getAllCategories()
                true
            })
            popupMenu.show()
        }
    }
    override fun onResume() {
        super.onResume()
        getAllCategories()
    }
    fun newCategory(view: View){
        /**
         * Funkcja newCategory realizuje zapis podanego przez użytkownika wydatku do bazy danych.
         */

        val enterCategory = findViewById<EditText>(R.id.enter_category)

        //https://johncodeos.com/how-to-make-post-get-put-and-delete-requests-with-retrofit-using-kotlin/
        val retrofit = Retrofit.Builder().
        baseUrl(URL.BASE_URL).
        build()

        val service = retrofit.create(APIRequest::class.java)

        if(enterCategory.text.isNotEmpty()) {

            val newCat= enterCategory.text.toString()

            /*TIME https://grokonez.com/kotlin/kotlin-get-current-datetime */
            //al currentDateTime = LocalDateTime.now()
            //val date = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString()

            val jsonObject = JSONObject()
            jsonObject.put("category", newCat)
            //jsonObject.put("date",date)

            val jsonObjectString = jsonObject.toString()
            Log.d("Object sent", jsonObjectString)

            val requestBody = jsonObjectString.toRequestBody("application/json;charset=UTF-8".toMediaTypeOrNull())

            CoroutineScope(Dispatchers.IO).launch {
                val response = service.addCategory(requestBody)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        getAllCategories()
                        Toast.makeText(this@EnterCategory, "Zapisano kategorię " + newCat, Toast.LENGTH_SHORT).show()
                        Log.d("RETROFIT SUCCESS, SENT REQUEST", jsonObjectString)
                    } else {
                        Toast.makeText(this@EnterCategory,"Nie udało się!", Toast.LENGTH_SHORT).show()
                        Log.e("RETROFIT_ERROR", response.code().toString())
                    }
                }
            }
            enterCategory.text.clear()
        }else{
            Toast.makeText(this, "Wprowadź poprawną wartość", Toast.LENGTH_SHORT).show()
            enterCategory.text.clear()
        }
    }

    private fun deleteCategory(category_id: Int){
        /**
         * Funkcja odpowiedzialna za usunięcie wybranego wydatku z serwerowej bazy danych
         */
        //https://johncodeos.com/how-to-make-post-get-put-and-delete-requests-with-retrofit-using-kotlin/
        val retrofit = Retrofit.Builder().
        baseUrl(URL.BASE_URL).
        build()

        val service = retrofit.create(APIRequest::class.java)

        val jsonObject = JSONObject()
        jsonObject.put("id", category_id)
        jsonObject.put("deleted", 1)

        val jsonObjectString = jsonObject.toString()
        Log.d("Object sent", jsonObjectString)

        val requestBody = jsonObjectString.toRequestBody("application/json;charset=UTF-8".toMediaTypeOrNull())

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.deleteCategory(category_id,requestBody)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Toast.makeText(this@EnterCategory, "Usunięto kategorię id:" + category_id, Toast.LENGTH_SHORT).show()
                    getAllCategories()
                    Log.d("RETROFIT SUCCESS, SENT REQUEST", jsonObjectString)
                } else {
                    Toast.makeText(this@EnterCategory, jsonObjectString, Toast.LENGTH_SHORT).show()
                    getAllCategories()
                    Log.e("RETROFIT_ERROR", response.code().toString())
                }
            }
        }
    }

    /*fun newCategory(view: View){
        *//**
         * Funkcja umożliwiająca dodanie nowej kategorii wydatków przez użytkownika,
         * Korzysta z handlera bazy danych w pliku ExpenseDBHandler
         *//*
        val dbHandler = ExpenseDBHandler(this, null, null, 1)
        val enterCategory = findViewById<EditText>(R.id.enter_category)

        if(enterCategory.text.isNotEmpty()) {
            val category = enterCategory.text.toString()
            val newCategory = UserCategory(category)
            dbHandler.addCategory(newCategory)
            enterCategory.text.clear()
            Toast.makeText(this, "Zapisano kategorię $category", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "Wprowadź poprawną wartość", Toast.LENGTH_SHORT).show()
            enterCategory.text.clear()
        }
        viewCategories(view)
    }*/

   /* fun removeCat(id: Int){
        *//**
         * Funkcja umożliwiająca usuwanie wskazanej z listview kategorii wydatków przez użytkownika,
         * Korzysta z handlera bazy danych w pliku ExpenseDBHandler
         *//*
        val dbHandler = ExpenseDBHandler(this,null,null,1)
        val success = dbHandler.removeCategory(UserCategory(id,""))
    }*/

    fun getAllCategories(){
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(URL.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIRequest::class.java)

        val categoryList = findViewById<ListView>(R.id.category_list)
        val response = retrofitBuilder.getAllCategories()

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
                val catArrayID = Array<String>(responseBody.size){"0"}
                val catArrayCat = Array<String>(responseBody.size){"null"}
                val catArrayDate = Array<String>(responseBody.size){"null"}
                val catArrayDel = Array<Int>(responseBody.size){0}
                var index = 0

                for(e in responseBody){
                    catArrayID[index] = e.id.toString()
                    catArrayCat[index] = e.category
                    catArrayDate[index] = e.date
                    catArrayDel[index] = e.deleted
                    index++
                }
                val catListAdapter = CategoryListAdapter(this@EnterCategory,catArrayID,catArrayCat,catArrayDate,catArrayDel)
                categoryList.adapter = catListAdapter
            }

            override fun onFailure(call: Call<List<UserCategoryJSONItem>?>, t: Throwable) {
                Toast.makeText(this@EnterCategory,"Coś poszło nie tak",Toast.LENGTH_LONG).show()
            }
        })

    }

    /*fun viewCategories(view: View) {

        val expensesList = findViewById<ListView>(R.id.category_list)

        val dbHandler = ExpenseDBHandler(this, null, null, 1)
        val categories: List<UserCategory> = dbHandler.readAllCategory()

        val ArrayID = Array<String>(categories.size){"0"}
        val ArrayCat = Array<String>(categories.size){"null"}

        var index = 0

        for(e in categories){
            ArrayID[index] = e.id.toString()
            ArrayCat[index] = e.category.toString()
            index++
        }
        val expListAdapter = CategoryListAdapter(this,ArrayID,ArrayCat,Arr)
        expensesList.adapter = expListAdapter
    }*/
}