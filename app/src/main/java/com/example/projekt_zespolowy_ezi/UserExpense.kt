package com.example.projekt_zespolowy_ezi

class UserExpense {
    var id: Int = 0
    var value: String? = null
    var category: String? = null
    var date: String? = null

    constructor(id:Int, value: String?,category: String? ,date: String) {
        this.id = id
        this.value = value
        this.category = category
        this.date = date
    }
    constructor(value: String?, category: String?, date: String)
    {
        this.value = value
        this.category = category
        this.date = date
    }

}

/*class UserExpense {
    var id: Int = 0
    var value: Float = 0f
    var date: String = "";

    constructor(value: Float, date: String) {
        this.value = value
        this.date = date
    }
} */