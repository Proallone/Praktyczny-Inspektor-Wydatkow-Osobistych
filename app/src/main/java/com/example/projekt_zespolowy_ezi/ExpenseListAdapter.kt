package com.example.projekt_zespolowy_ezi

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ExpenseListAdapter(private val context: Activity, private val id: Array<String>, private val value: Array<String>, private val category: Array<String>,private val date: Array<String>)
    : ArrayAdapter<String>(context, R.layout.expense_layout,id) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.expense_layout, null, true)

        val idText = rowView.findViewById(R.id.expenseViewId) as TextView
        val valueText = rowView.findViewById(R.id.expenseViewVal) as TextView
        val categoryText = rowView.findViewById(R.id.expenseViewCat) as TextView
        val dateText = rowView.findViewById(R.id.expenseViewDate) as TextView

        idText.text = "Id: ${id[position]}"
        valueText.text = "Name: ${value[position]}"
        categoryText.text = "Name: ${category[position]}"
        dateText.text = "Email: ${date[position]}"
        return rowView
    }
}