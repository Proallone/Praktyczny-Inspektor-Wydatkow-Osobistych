
package com.example.projekt_zespolowy_ezi

import com.example.projekt_zespolowy_ezi.api.UserExpenseJSON
import com.example.projekt_zespolowy_ezi.api.UserExpenseJSONItem
import retrofit2.Call
import retrofit2.http.GET

interface APIRequest {
    @GET("/expenses")
    fun getAllExpenses(): Call<List<UserExpenseJSONItem>>

}