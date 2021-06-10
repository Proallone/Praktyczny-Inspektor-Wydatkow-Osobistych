package com.example.projekt_zespolowy_ezi.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projekt_zespolowy_ezi.APIRequest
import com.example.projekt_zespolowy_ezi.R
import com.example.projekt_zespolowy_ezi.animations.BackgroundAnimation
import com.example.projekt_zespolowy_ezi.constants.LoggedUser
import com.example.projekt_zespolowy_ezi.constants.URL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit


class LogInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        val layout: RelativeLayout = findViewById(R.id.login_layout)
        //setStatusBarGradiant(this)
        BackgroundAnimation.animateUI(layout)
    }
    fun Login(view: View){

        val email = findViewById<EditText>(R.id.enter_email)
        val pass = findViewById<EditText>(R.id.enter_password)
        val intent = Intent(this, MainActivity::class.java)

        /* Naprawia błąd związany z timeoutem podczas logowania. Rozwiązaniem problemu jest wydłużenie czasu oczekiwania na odpowiedź serwera
        * https://stackoverflow.com/questions/29380844/how-to-set-timeout-in-retrofit-library*/
        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(400, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        //https://johncodeos.com/how-to-make-post-get-put-and-delete-requests-with-retrofit-using-kotlin/
        val retrofit = Retrofit.Builder().
        baseUrl(URL.BASE_URL).
        client(okHttpClient).
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
                        val usrEmail = responseJSON.getString("email")
                        val usrRegDate = responseJSON.getString("registration_date")
                        val usrName = responseJSON.getString("name")
                        LoggedUser.userId = usrID.toInt()
                        LoggedUser.userName = usrName.toString()
                        LoggedUser.userEmail = usrEmail.toString()
                        LoggedUser.userRegDate=usrRegDate.toString()
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