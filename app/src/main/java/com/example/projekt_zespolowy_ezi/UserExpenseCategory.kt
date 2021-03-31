package com.example.projekt_zespolowy_ezi

class UserExpenseCategory {
    var id: Int = 0
    var category: String? = null

    constructor(id:Int,category: String?) {
        this.id = id
        this.category = category
    }
    constructor(category: String?)
    {
        this.category = category
    }

}
