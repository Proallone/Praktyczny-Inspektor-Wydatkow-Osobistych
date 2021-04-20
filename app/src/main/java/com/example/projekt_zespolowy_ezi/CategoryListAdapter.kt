package com.example.projekt_zespolowy_ezi

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

/**
 * Moduł odpowiający za poprawne zapełnienie pola ListView z wydatkami
 * Funkcja getView odpowiada za przypisanie do odpowiednich pól wartości z podanej bazy danych
 * Uzupełniając odpowiednie pola właściwymi wartościami
 */

class CategoryListAdapter(private val context: Activity, private val id: Array<String>, private val category: Array<String>)
    : ArrayAdapter<String>(context, R.layout.expense_layout,id) {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.category_layout, null, true)

        val idText = rowView.findViewById(R.id.categoryViewId) as TextView
        val categoryText = rowView.findViewById(R.id.categoryViewCat) as TextView

        idText.text = "Id: ${id[position]}"
        categoryText.text = "Category: ${category[position]}"
        return rowView
    }
}