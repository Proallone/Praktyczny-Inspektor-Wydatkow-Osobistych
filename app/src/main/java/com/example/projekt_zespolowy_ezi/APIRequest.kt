
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
    @Headers("Accept:application/json","Content-Type:application/json;charset=UTF-8")
    fun getAllExpenses(): Call<List<UserExpenseJSONItem>>

    //@POST("/expenses")
    //fun addExpense(@Body newExpense: UserExpense):Call<UserExpense>

    @GET("/users/expenses/{user_id}")
    @Headers("Accept:application/json","Content-Type:application/json;charset=UTF-8")
    fun userExpenses(@Path("user_id") id: Int): Call<List<UserExpenseJSONItem>>

    @GET("/users/categories/{user_id}")
    @Headers("Accept:application/json","Content-Type:application/json;charset=UTF-8")
    fun userCategories(@Path("user_id") id: Int): Call<List<UserCategoryJSONItem>>

    @GET("/users/expenses/all")
    @Headers("Accept:application/json","Content-Type:application/json;charset=UTF-8")
    fun userAllExpenses(@Query("user_id") id :Int, @Query("category") cat : String): Call<List<UserExpenseJSONItem>>

    @POST("/expenses")
    @Headers("Accept:application/json","Content-Type:application/json;charset=UTF-8")
    suspend fun addExpense2(@Body requestbody: RequestBody): Response<ResponseBody>

    @PATCH("/expenses/id/{id}")
    @Headers("Accept:application/json","Content-Type:application/json;charset=UTF-8")
    suspend fun deleteExpense(@Path("id") id: Int, @Body requestbody: RequestBody):Response<ResponseBody>

    @GET("/categories")
    fun getAllCategories(): Call<List<UserCategoryJSONItem>>

    @POST("/categories")
    @Headers("Accept:application/json","Content-Type:application/json;charset=UTF-8")
    suspend fun addCategory(@Body requestbody: RequestBody): Response<ResponseBody>

    @PATCH("/categories/id/{id}")
    @Headers("Accept:application/json","Content-Type:application/json;charset=UTF-8")
    suspend fun deleteCategory(@Path("id") id: Int, @Body requestbody: RequestBody):Response<ResponseBody>

    @POST("/users")
    @Headers("Accept:application/json","Content-Type:application/json;charset=UTF-8")
    suspend fun newUser(@Body requestbody: RequestBody): Response<ResponseBody>

    @PATCH("/users")
    @Headers("Accept:application/json","Content-Type:application/json;charset=UTF-8")
    suspend fun deleteUser(@Body requestbody: RequestBody):Response<ResponseBody>

    @PATCH("/users/{id}")
    @Headers("Accept:application/json","Content-Type:application/json;charset=UTF-8")
    suspend fun changePassword(@Path("id") id: Int, @Body requestbody: RequestBody):Response<ResponseBody>

    @POST("/users/login")
    @Headers("Accept:application/json","Content-Type:application/json;charset=UTF-8")
    suspend fun userLogin(@Body requestbody: RequestBody): Response<ResponseBody>

}