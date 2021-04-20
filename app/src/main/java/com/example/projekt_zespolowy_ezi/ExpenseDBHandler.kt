package com.example.projekt_zespolowy_ezi

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.res.Resources
import android.widget.Toast

/**
 * Data Base Handler - klasa odpowiadająca za implementacje funkcji typu CRUD (Create, Read, Update, Delete) bazy danych
 * Wykorzystana baza danych to standard SQLite. Poniższe funkcje definiują sposób komunikacji z zapisaną na urząrzeniu bazą
 * oferując możliwości jej stworzenia, dodawania elementów oraz wyświetlania
 */

class ExpenseDBHandler(context: Context, name: String?,factory: SQLiteDatabase.CursorFactory?,version: Int):SQLiteOpenHelper(context,DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_EXPENSES_TABLE = ("CREATE TABLE "+
                TABLE_EXPENSES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"+
                COLUMN_EXPENSEVAL + " TEXT,"+
                COLUMN_EXPENSECAT + " TEXT," +
                COLUMN_EXPENSEDATE + " TEXT"+ ")")

        val CREATE_CATEGORY_TABLE = ("CREATE TABLE "+
                TABLE_CATEGORY + "("
                + COLUMN_CAT_ID + " INTEGER PRIMARY KEY,"+
                COLUMN_CAT_TYPE + " TEXT"+")")

        db.execSQL(CREATE_EXPENSES_TABLE)
        db.execSQL(CREATE_CATEGORY_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS" +  TABLE_EXPENSES)
        db.execSQL("DROP TABLE IF EXISTS" +  TABLE_CATEGORY)
        onCreate(db)
    }

    fun initialCategories(initialCat: List<UserCategory>){
        val db = this.writableDatabase
        val values = ContentValues()
        //val cats = UserCategory(initialCat)

        for(ic in initialCat) {
            values.put(COLUMN_EXPENSECAT, ic.category)
            db.insert(TABLE_CATEGORY, null, values)
        }
        db.close()
    }

    fun addExpense(expense: UserExpense){
        val values = ContentValues()
        values.put(COLUMN_EXPENSEVAL, expense.value)
        values.put(COLUMN_EXPENSECAT, expense.category)
        values.put(COLUMN_EXPENSEDATE, expense.date)

        val db = this.writableDatabase

        db.insert(TABLE_EXPENSES, null, values)
        db.close()
    }

    fun addCategory(category: UserCategory){
        val values = ContentValues()
        values.put(COLUMN_EXPENSECAT, category.category)
        val db = this.writableDatabase
        db.insert(TABLE_CATEGORY, null, values)
        db.close()
    }

    fun removeExpense(exp: UserExpense):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_ID,exp.id)
        val success = db.delete(TABLE_EXPENSES,"_id="+exp.id,null)
        db.close()
        return success
    }

    fun removeCategory(cat: UserCategory):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_CAT_ID,cat.id)
        val success = db.delete(TABLE_CATEGORY,"_id="+cat.id,null)
        db.close()
        return success
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
                expenseId=cursor.getInt(0)
                expenseVal=cursor.getString(1)
                expenseCat=cursor.getString(2)
                expenseDate=cursor.getString(3)
                val exp = UserExpense(id =  expenseId, value = expenseVal, category = expenseCat, date = expenseDate )
                expList.add(exp)
            }while (cursor.moveToNext())
        }
        return expList
    }

    fun findCategory():List<UserCategory> {
        /* https://www.javatpoint.com/kotlin-android-sqlite-tutorial */
        val catList:ArrayList<UserCategory> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_CATEGORY"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)

        }catch (e: SQLException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var expenseId: Int
        var expenseCat: String
        if(cursor.moveToFirst()){
            do {
                expenseId=cursor.getInt(0)
                expenseCat=cursor.getString(1)
                val cat = UserCategory(id =  expenseId, category = expenseCat)
                catList.add(cat)
            }while (cursor.moveToNext())
        }
        return catList
    }

    companion object {

        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "expensesDB.sqlite"

        val TABLE_EXPENSES = "expenses"
        val COLUMN_ID = "_id"
        val COLUMN_EXPENSEVAL = "value"
        val COLUMN_EXPENSECAT = "category"
        val COLUMN_EXPENSEDATE = "date"

        val TABLE_CATEGORY = "categories"
        val COLUMN_CAT_ID = "_id"
        val COLUMN_CAT_TYPE = "category"

    }
}