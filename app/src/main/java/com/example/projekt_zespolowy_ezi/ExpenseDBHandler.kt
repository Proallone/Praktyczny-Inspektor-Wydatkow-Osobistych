package com.example.projekt_zespolowy_ezi

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ExpenseDBHandler(context: Context, name: String?,factory: SQLiteDatabase.CursorFactory?,version: Int):SQLiteOpenHelper(context,DATABASE_NAME, factory, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_EXPENSES_TABLE = ("CREATE TABLE "+
                TABLE_EXPENSES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"+
                COLUMN_EXPENSEVAL + " TEXT,"+
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

    fun findExpense():List<UserExpense> {
        /* https://www.javatpoint.com/kotlin-android-sqlite-tutorial */
        val expList:ArrayList<UserExpense> = ArrayList<UserExpense>()
        val selectQuery = "SELECT * FROM $TABLE_EXPENSES"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)

        }catch (e: SQLException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var expenseId: Int
        var expenseVal: String
        var expenseCat: String
        var expenseDate: String
        if(cursor.moveToFirst()){
            do {
                expenseId=cursor.getInt(cursor.getColumnIndex("COLUMN_ID"))
                expenseVal=cursor.getString(cursor.getColumnIndex("COLUMN_EXPENSEVAL"))
                expenseCat=cursor.getString(cursor.getColumnIndex("COLUMN_EXPENSECAT"))
                expenseDate=cursor.getString(cursor.getColumnIndex("COLUMN_EXPENSEDATE"))
                val exp = UserExpense(id =  expenseId, value = expenseVal, category = expenseCat, date = expenseDate )
                expList.add(exp)
            }while (cursor.moveToNext())
        }
        return expList
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