package com.example.projekt_zespolowy_ezi

/**
 * Klasa odpowiadająca za implementację wydatków klienta
 * Klasa określa jakie pola składają się na poprawny wydatek
 * Implementacja przewiduje wykorzystanie różych konstruktorów zależnie od
 * dostępnych danych użytkownika
 */

class UserExpense {
    var id: Int = 0
    var value: String? = null
    var category: String? = null
    var date: String? = null

    constructor(id:Int, value: String,category: String ,date: String) {
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