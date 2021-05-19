
package com.example.projekt_zespolowy_ezi

import com.example.projekt_zespolowy_ezi.api.UserCategoryJSONItem
import com.example.projekt_zespolowy_ezi.api.UserExpenseJSON
import com.example.projekt_zespolowy_ezi.api.UserExpenseJSONItem
import com.example.projekt_zespolowy_ezi.classes.UserCategory
import com.example.projekt_zespolowy_ezi.classes.UserExpense
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface APIRequest {
    @GET("/expenses")
    fun getAllExpenses(): Call<List<UserExpenseJSONItem>>

    @POST("/expenses")
    fun addExpense(@Body newExpense: UserExpense):Call<UserExpense>

    @POST("/expenses")
    @Headers("Accept:application/json","Content-Type:application/json")
    suspend fun addExpense2(@Body requestbody: RequestBody): Response<ResponseBody>

    @PATCH("/expenses/id/{id}")
    @Headers("Accept:application/json","Content-Type:application/json")
    suspend fun deleteExpense(@Path("id") id: Int, @Body requestbody: RequestBody):Response<ResponseBody>

    @GET("/categories")
    fun getAllCategories(): Call<List<UserCategoryJSONItem>>

    @POST("/categories")
    @Headers("Accept:application/json","Content-Type:application/json")
    suspend fun addCategory(@Body requestbody: RequestBody): Response<ResponseBody>

}