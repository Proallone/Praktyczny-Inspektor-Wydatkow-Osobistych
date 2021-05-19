package com.example.projekt_zespolowy_ezi.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.projekt_zespolowy_ezi.R

/**
 * Moduł odpowiający za poprawne zapełnienie pola ListView z wydatkami
 * Funkcja getView odpowiada za przypisanie do odpowiednich pól wartości z podanej bazy danych
 * Uzupełniając odpowiednie pola właściwymi wartościami
 */

class CategoryListAdapter(private val context: Activity, private val id: Array<String>, private val category: Array<String>,private val date: Array<String>,private val deleted: Array<Int>)
    : ArrayAdapter<String>(context, R.layout.category_layout,id) {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.category_layout, null, true)

        val idText = rowView.findViewById(R.id.categoryViewId) as TextView
        val categoryText = rowView.findViewById(R.id.categoryViewCat) as TextView
        val dateText = rowView.findViewById(R.id.categoryViewDate) as TextView
        val deletedText = rowView.findViewById(R.id.categoryViewDel) as TextView

        idText.text = "Id: ${id[position]}"
        categoryText.text = "Category: ${category[position]}"
        dateText.text = "Date: ${date[position]}"
        deletedText.text = "Deleted: ${deleted[position]}"
        return rowView
    }
}