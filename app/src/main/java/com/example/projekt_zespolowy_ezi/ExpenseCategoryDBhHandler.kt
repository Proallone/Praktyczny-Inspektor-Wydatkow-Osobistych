package com.example.projekt_zespolowy_ezi

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ExpenseCategoryDBHandler(context: Context, name: String?,factory: SQLiteDatabase.CursorFactory?,version: Int):SQLiteOpenHelper(context,DATABASE_NAME, factory, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_CATEGORY_TABLE = ("CREATE TABLE "+
                TABLE_CATEGORY + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"+
                COLUM_EXPENSECAT + " TEXT," + ")")
        db.execSQL(CREATE_CATEGORY_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS" +  TABLE_CATEGORY)
        onCreate(db)

    }

    fun addCategory(expense: UserExpense){
        val values = ContentValues()
        values.put(COLUM_EXPENSECAT, expense.category)

        val db = this.writableDatabase

        db.insert(TABLE_CATEGORY, null, values)
        db.close()
    }

    companion object {

        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "expensesCategoryDB.db"
        val TABLE_CATEGORY = "category"

        val COLUMN_ID = "_id"
        val COLUM_EXPENSECAT = "category"
    }
}