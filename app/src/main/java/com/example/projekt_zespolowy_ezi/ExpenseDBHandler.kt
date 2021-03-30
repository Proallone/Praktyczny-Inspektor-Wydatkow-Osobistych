package com.example.projekt_zespolowy_ezi

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.view.View

class ExpenseDBHandler(context: Context, name: String?,factory: SQLiteDatabase.CursorFactory?,version: Int):SQLiteOpenHelper(context,DATABASE_NAME, factory, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_EXPENSES_TABLE = ("CREATE TABLE "+
                TABLE_EXPENSES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"+
                COLUMN_EXPENSEVAL + " REAL,"+
                COLUM_EXPENSECAT + " TEXT," +
                COLUMN_EXPENSEDATE + " TEXT"+ ")")
        db.execSQL(CREATE_EXPENSES_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS" +  TABLE_EXPENSES)
        onCreate(db)

    }

    fun addExpense(expense: UserExpense){
        val values = ContentValues()
        values.put(COLUMN_EXPENSEVAL, expense.value)
        values.put(COLUM_EXPENSECAT, expense.category)
        values.put(COLUMN_EXPENSEDATE, expense.date)

        val db = this.writableDatabase

        db.insert(TABLE_EXPENSES, null, values)
        db.close()
    }

    fun findExpense(expenseID: Int ): UserExpense? {
        val query =
            "SELECT * FROM $TABLE_EXPENSES WHERE $COLUMN_ID =  \"$expenseID\""

        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)

        var expense: UserExpense? = null

        if (cursor.moveToFirst()) {
            cursor.moveToFirst()

            val id = Integer.parseInt(cursor.getString(0))
            val value = cursor.getFloat(1)
            val date = cursor.getString(2)
            expense = UserExpense(id, value, null, date)
            cursor.close()
        }

        db.close()
        return expense
    }

    companion object {

        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "expensesDB.db"
        val TABLE_EXPENSES = "expenses"

        val COLUMN_ID = "_id"
        val COLUMN_EXPENSEVAL = "value"
        val COLUM_EXPENSECAT = "category"
        val COLUMN_EXPENSEDATE = "date"

    }
}