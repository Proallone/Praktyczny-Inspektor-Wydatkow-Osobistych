package com.example.projekt_zespolowy_ezi

class UserExpense {
    var id: Int = 0
    var value: Float = 0f
    var date: String? = null

    constructor(id:Int, value: Float, date: String) {
        this.id = id
        this.value = value
        this.date = date
    }
    constructor(value: Float, date: String)
    {
        this.value = value
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