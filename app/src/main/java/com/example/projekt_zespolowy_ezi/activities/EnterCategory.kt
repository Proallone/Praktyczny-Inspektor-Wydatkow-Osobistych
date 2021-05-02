package com.example.projekt_zespolowy_ezi.activities

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ListView

import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projekt_zespolowy_ezi.animations.BackgroundAnimation
import com.example.projekt_zespolowy_ezi.adapters.CategoryListAdapter
import com.example.projekt_zespolowy_ezi.R
import com.example.projekt_zespolowy_ezi.classes.UserCategory
import com.example.projekt_zespolowy_ezi.database.ExpenseDBHandler


class EnterCategory : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_category)
        val layout: RelativeLayout = findViewById(R.id.enter_category_layout)
        val selectedItem: ListView = findViewById(R.id.category_list)

        BackgroundAnimation.animateUI(layout)
        viewCategories(layout)

        selectedItem.setOnItemClickListener { parent, view, position, id ->
            val element : String = selectedItem.getItemAtPosition(position) as String // The item that was clicked
            removeCat(element.toInt())
        }
    }

    fun newCategory(view: View){
        /**
         * Funkcja umożliwiająca dodanie nowej kategorii wydatków przez użytkownika,
         * Korzysta z handlera bazy danych w pliku ExpenseDBHandler
         */
        val dbHandler = ExpenseDBHandler(this, null, null, 1)
        val enterCategory = findViewById<EditText>(R.id.enter_category)

        if(enterCategory.text.isNotEmpty()) {
            val category = enterCategory.text.toString()
            val newCategory = UserCategory(category)
            dbHandler.addCategory(newCategory)
            enterCategory.text.clear()
            Toast.makeText(this, "Zapisano kategorię $category", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "Wprowadź poprawną wartość", Toast.LENGTH_SHORT).show()
            enterCategory.text.clear()
        }
        viewCategories(view)
    }

    fun removeCat(id: Int){
        /**
         * Funkcja umożliwiająca usuwanie wskazanej z listview kategorii wydatków przez użytkownika,
         * Korzysta z handlera bazy danych w pliku ExpenseDBHandler
         */
        val dbHandler = ExpenseDBHandler(this,null,null,1)
        val success = dbHandler.removeCategory(UserCategory(id,""))
        //Toast.makeText(this, "Usunięto kategorię ID: $id", Toast.LENGTH_SHORT).show()
    }

    fun viewCategories(view: View) {
        /**
         * Funkcja wyświetlająca wszystkie wprowadzone do bazy danych kategorie.
         * Korzysta z adaptera listy kategorii do wyświetlenia otrzymanych wyników w listview
         */
        val expensesList = findViewById<ListView>(R.id.category_list)

        val dbHandler = ExpenseDBHandler(this, null, null, 1)
        val categories: List<UserCategory> = dbHandler.readAllCategory()

        val ArrayID = Array<String>(categories.size){"0"}
        val ArrayCat = Array<String>(categories.size){"null"}

        var index = 0

        for(e in categories){
            ArrayID[index] = e.id.toString()
            ArrayCat[index] = e.category.toString()
            index++
        }
        val expListAdapter = CategoryListAdapter(this,ArrayID,ArrayCat)
        expensesList.adapter = expListAdapter
    }

}