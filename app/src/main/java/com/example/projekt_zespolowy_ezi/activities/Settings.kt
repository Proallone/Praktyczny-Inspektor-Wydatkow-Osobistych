package com.example.projekt_zespolowy_ezi.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
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
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit


class Settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val layout: RelativeLayout = findViewById(R.id.settings_layout)
        BackgroundAnimation.animateUI(layout)

        val userNameTV = findViewById<TextView>(R.id.textview_user_name)
        val userEmailTV = findViewById<TextView>(R.id.textview_user_email)
        val userRegDateTV = findViewById<TextView>(R.id.textview_user_registration)
        val deleteAccountButton = findViewById<ImageButton>(R.id.delete_account_button)
        val updatePasswordButton = findViewById<ImageButton>(R.id.change_password_button)

        userNameTV.text = LoggedUser.userName
        userEmailTV.text = LoggedUser.userEmail
        userRegDateTV.text = LoggedUser.userRegDate

        deleteAccountButton.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setMessage("Usunąć konto?")
                .setTitle("Potwierdź wybór")
                .setPositiveButton("Tak", DialogInterface.OnClickListener { dialog, id ->
                    deleteUser(LoggedUser.userId!!)
                    logout()
                })
                .setNegativeButton("Nie", DialogInterface.OnClickListener { dialog, id ->

                })
                .show()
        }


        updatePasswordButton.setOnClickListener {
            val input = EditText(this)
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setMessage("Zmienić hasło?")
                input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                builder.setView(input)
                .setTitle("Potwierdź wybór")
                .setPositiveButton("Tak", DialogInterface.OnClickListener { dialog, id ->
                    changePassword(LoggedUser.userId!!, input.text.toString())
                    logout()
                })
                .setNegativeButton("Nie", DialogInterface.OnClickListener { dialog, id ->
                    // CANCEL
                })
                .show()
        }
    }

    private fun logout(){
        /*Funkcja odpowiadająca za przejście do activity wprowadzenia wydatków*/
        val logout = Intent(this, WelcomeView::class.java)
        LoggedUser.userId = null
        LoggedUser.userName = null
        LoggedUser.userEmail = null
        LoggedUser.userRegDate = null
        startActivity(logout)
    }

    fun deleteUser(user_id: Int){
        /**
         * Funkcja odpowiedzialna za usunięcie wybranego wydatku z serwerowej bazy danych
         */
        //https://johncodeos.com/how-to-make-post-get-put-and-delete-requests-with-retrofit-using-kotlin/
        val retrofit = Retrofit.Builder().
        baseUrl(URL.BASE_URL).
        build()

        val service = retrofit.create(APIRequest::class.java)

        val jsonObject = JSONObject()
        jsonObject.put("id", user_id)

        val jsonObjectString = jsonObject.toString()
        Log.d("Object sent", jsonObjectString)

        val requestBody = jsonObjectString.toRequestBody("application/json;charset=UTF-8".toMediaTypeOrNull())

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.deleteUser(requestBody)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Settings, "Usunięto Twoje konto:" +user_id, Toast.LENGTH_SHORT).show()
                    Log.d("RETROFIT SUCCESS, SENT REQUEST", jsonObjectString)
                } else {
                    Toast.makeText(this@Settings, "Coś poszło nie tak", Toast.LENGTH_SHORT).show()
                    Log.e("RETROFIT_ERROR", response.code().toString())
                }
            }
        }
    }
    fun changePassword(user_id: Int, new_pass :String){
        /**
         * Funkcja odpowiedzialna za usunięcie wybranego wydatku z serwerowej bazy danych
         */
        //https://johncodeos.com/how-to-make-post-get-put-and-delete-requests-with-retrofit-using-kotlin/
        val retrofit = Retrofit.Builder().
        baseUrl(URL.BASE_URL).
        build()

        val service = retrofit.create(APIRequest::class.java)

        val jsonObject = JSONObject()
        jsonObject.put("password", new_pass)

        val jsonObjectString = jsonObject.toString()
        Log.d("Object sent", jsonObjectString)

        val requestBody = jsonObjectString.toRequestBody("application/json;charset=UTF-8".toMediaTypeOrNull())

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.changePassword(user_id,requestBody)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Settings, "Zmieniono Twoje hasło", Toast.LENGTH_SHORT).show()
                    Log.d("RETROFIT SUCCESS, SENT REQUEST", jsonObjectString)
                } else {
                    Toast.makeText(this@Settings, "Coś poszło nie tak", Toast.LENGTH_SHORT).show()
                    Log.e("RETROFIT_ERROR", response.code().toString())
                }
            }
        }
    }
}