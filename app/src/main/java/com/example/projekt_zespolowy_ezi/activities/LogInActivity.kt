package com.example.projekt_zespolowy_ezi.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import com.example.projekt_zespolowy_ezi.APIRequest
import com.example.projekt_zespolowy_ezi.R
import com.example.projekt_zespolowy_ezi.animations.BackgroundAnimation
import com.example.projekt_zespolowy_ezi.constants.URL
import com.example.projekt_zespolowy_ezi.constants.UserID
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit

class LogInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
    }
    fun Login(view: View){

        val email = findViewById<EditText>(R.id.enter_email)
        val pass = findViewById<EditText>(R.id.enter_password)
        val intent = Intent(this, MainActivity::class.java)

        //https://johncodeos.com/how-to-make-post-get-put-and-delete-requests-with-retrofit-using-kotlin/
        val retrofit = Retrofit.Builder().
        baseUrl(URL.BASE_URL).
        build()

        val service = retrofit.create(APIRequest::class.java)

        if(email.text.isNotBlank() && pass.text.isNotEmpty()) {

            val newEmail = email.text.toString()
            val newPass = pass.text.toString()

            val jsonObject = JSONObject()
            jsonObject.put("email",newEmail)
            jsonObject.put("password",newPass)

            val jsonObjectString = jsonObject.toString()
            Log.d("Object sent", jsonObjectString)

            val requestBody = jsonObjectString.toRequestBody("application/json;charset=UTF-8".toMediaTypeOrNull())

            CoroutineScope(Dispatchers.IO).launch {
                val response = service.userLogin(requestBody)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        //val responseBody = response.body()!!
                        val responseBody = response.body()?.string()
                        val responseJSON = JSONObject(responseBody)
                        val usrID = responseJSON.getString("id")
                        UserID.userId = usrID.toInt()
                        //Toast.makeText(this@LogInActivity, usrID, Toast.LENGTH_LONG).show()
                        Toast.makeText(this@LogInActivity, "Zalogowano!", Toast.LENGTH_LONG).show()
                        Log.d("RETROFIT SUCCESS, SENT REQUEST", jsonObjectString)
                        Log.d("RESPONSE", response.toString())
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LogInActivity, "Niepoprawne dane", Toast.LENGTH_SHORT).show()
                        Log.e("RETROFIT_ERROR", response.code().toString())
                    }
                }
            }
            email.text.clear()
            pass.text.clear()
        }else{
            Toast.makeText(this, "Wprowadź wszystkie dane!", Toast.LENGTH_SHORT).show()
        }
    }
}