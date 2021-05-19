package com.example.projekt_zespolowy_ezi.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.projekt_zespolowy_ezi.R
import java.time.format.DateTimeFormatter

/**
 * Moduł odpowiający za poprawne zapełnienie pola ListView z wydatkami
 * Funkcja getView odpowiada za przypisanie do odpowiednich pól wartości z podanej bazy danych
 * Uzupełniając odpowiednie pola właściwymi wartościami
 */

class ExpenseListAdapter(private val context: Activity, private val id: Array<String>, private val value: Array<String>, private val category: Array<String>,private val date: Array<String>,private val deleted: Array<Int>)
    : ArrayAdapter<String>(context, R.layout.expense_layout,id) {

    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.expense_layout, null, true)


        val valueText = rowView.findViewById(R.id.expenseViewVal) as TextView
        val categoryText = rowView.findViewById(R.id.expenseViewCat) as TextView
        val dateText = rowView.findViewById(R.id.expenseViewDate) as TextView


        valueText.text = "Wartość: ${value[position]}zł"
        categoryText.text = "Kategoria: ${category[position]}"
        dateText.text = "Data: ${date[position]}"
        return rowView
    }
}