package com.example.projekt_zespolowy_ezi.classes

/**
 * Klasa odpowiadająca za implementację kategorii wydatków klienta
 * Klasa określa jakie pola składają się na poprawną kategorię
 * Implementacja przewiduje wykorzystanie różych konstruktorów zależnie od
 * dostępnych danych użytkownika
 */

class UserCategory {
    var id: Int = 0
    var category: String? = null

    constructor(id:Int,category: String) {
        this.id = id
        this.category = category
    }

    constructor(category: String?)
    {
        this.category = category
    }
}