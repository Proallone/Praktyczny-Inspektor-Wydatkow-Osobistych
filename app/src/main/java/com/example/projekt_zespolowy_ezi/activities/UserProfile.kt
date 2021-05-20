package com.example.projekt_zespolowy_ezi.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.projekt_zespolowy_ezi.APIRequest
import com.example.projekt_zespolowy_ezi.R
import com.example.projekt_zespolowy_ezi.constants.URL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit

class UserProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
    }
    fun newUser2(view: View){
        /**
         * Funkcja newExpense realizuje zapis podanego przez użytkownika wydatku do bazy danych.
         * Korzysta z handlera bazy danych ExpenseDBHandler
         */

        val name = findViewById<EditText>(R.id.enter_name)
        val email = findViewById<EditText>(R.id.enter_email)
        val pass = findViewById<EditText>(R.id.enter_password)

        //https://johncodeos.com/how-to-make-post-get-put-and-delete-requests-with-retrofit-using-kotlin/
        val retrofit = Retrofit.Builder().
        baseUrl(URL.BASE_URL).
        build()

        val service = retrofit.create(APIRequest::class.java)

        if(name.text.isNotEmpty() && email.text.isNotBlank() && pass.text.isNotEmpty()) {

            val newName = name.text.toString()
            val newEmail = email.text.toString()
            val newPass = pass.text.toString()

            /*TIME https://grokonez.com/kotlin/kotlin-get-current-datetime */
            //val currentDateTime = LocalDateTime.now()
            //val date = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString()

            val jsonObject = JSONObject()
            jsonObject.put("name", newName)
            jsonObject.put("email",newEmail)
            jsonObject.put("password",newPass)

            val jsonObjectString = jsonObject.toString()
            Log.d("Object sent", jsonObjectString)

            val requestBody = jsonObjectString.toRequestBody("application/json;charset=UTF-8".toMediaTypeOrNull())

            CoroutineScope(Dispatchers.IO).launch {
                val response = service.newUser(requestBody)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@UserProfile, "Witaj " + newName + "!", Toast.LENGTH_LONG).show()
                        Log.d("RETROFIT SUCCESS, SENT REQUEST", jsonObjectString)
                    } else {
                        Toast.makeText(this@UserProfile, jsonObjectString, Toast.LENGTH_SHORT).show()
                        Log.e("RETROFIT_ERROR", response.code().toString())
                    }
                }
            }
            name.text.clear()
            email.text.clear()
            pass.text.clear()
        }else{
            Toast.makeText(this, "Wprowadź wszystkie dane!", Toast.LENGTH_SHORT).show()
        }
    }

}