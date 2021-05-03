
package com.example.projekt_zespolowy_ezi

import com.example.projekt_zespolowy_ezi.api.UserExpenseJSON
import com.example.projekt_zespolowy_ezi.api.UserExpenseJSONItem
import com.example.projekt_zespolowy_ezi.classes.UserExpense
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface APIRequest {
    @GET("/expenses")
    fun getAllExpenses(): Call<List<UserExpenseJSONItem>>

    @POST("/expenses")
    fun addExpense(@Body newExpense: UserExpense):Call<UserExpense>

    @POST("/expenses")
    suspend fun addExpense2(@Body requestbody: RequestBody): Response<ResponseBody>

}