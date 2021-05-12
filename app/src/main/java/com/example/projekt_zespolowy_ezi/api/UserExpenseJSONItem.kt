package com.example.projekt_zespolowy_ezi.api
import com.google.gson.annotations.SerializedName

data class UserExpenseJSONItem(
    val category: String,
    val date: String,
    val id: Int,
    val value: String,
    @SerializedName("deleted")
    var deleted: Int
)