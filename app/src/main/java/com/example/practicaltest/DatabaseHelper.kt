package com.example.practicaltest

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.practicaltest.Models.Employee

class DatabaseHelper(context: Context):SQLiteOpenHelper(context,DATABASE_NAME,FACTORY,DATABASE_VERSION) {
    companion object{
        internal val FACTORY  = null
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "UserManager"
        private val TABLE_NAME = "UserTable"
        private val KEY_ID = "id"
        private val KEY_NAME = "name"
        private val KEY_EMAIL = "email"
        private val KEY_PASSWORD = "password"
        private val KEY_LOGIN = "login"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_PASSWORD + " TEXT,"
                + KEY_LOGIN + " INTEGER DEFAULT 0" + ")")
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }


    fun getAllUser(): List<Employee> {

        // array of columns to fetch
        val columns = arrayOf(KEY_ID, KEY_NAME, KEY_EMAIL, KEY_PASSWORD,KEY_LOGIN)

        // sorting orders
        val sortOrder = "$KEY_NAME ASC"
        val userList = ArrayList<Employee>()

        val db = this.readableDatabase

        // query the user table
        val cursor = db.query(TABLE_NAME, //Table to query
            columns,            //columns to return
            null,     //columns for the WHERE clause
            null,  //The values for the WHERE clause
            null,      //group the rows
            null,       //filter by row groups
            sortOrder)         //The sort order
        if (cursor.moveToFirst()) {
            do {
                val user = Employee(id = cursor.getString(cursor.getColumnIndex(KEY_ID)).toInt(),
                    name = cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                    email = cursor.getString(cursor.getColumnIndex(KEY_EMAIL)),
                    password = cursor.getString(cursor.getColumnIndex(KEY_PASSWORD)),
                    login = cursor.getInt(cursor.getColumnIndex(KEY_LOGIN))
                )

                userList.add(user)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return userList
    }

    fun addUser(employee: Employee) {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(KEY_NAME, employee.name)
        values.put(KEY_EMAIL, employee.email)
        values.put(KEY_PASSWORD, employee.password)
        values.put(KEY_LOGIN, employee.login)
        // Inserting Row
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun updateUser(employee: Employee) {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(KEY_NAME, employee.name)
        values.put(KEY_EMAIL, employee.email)
        values.put(KEY_PASSWORD, employee.password)
        values.put(KEY_LOGIN, employee.login)

        // updating row
        db.update(TABLE_NAME, values, "$KEY_EMAIL = ?",
            arrayOf(employee.email.toString()))
        db.close()
    }

    fun checkUser(email: String): Boolean {

        // array of columns to fetch
        val columns = arrayOf(KEY_ID)
        val db = this.readableDatabase

        // selection criteria
        val selection = "$KEY_EMAIL = ?"

        // selection argument
        val selectionArgs = arrayOf(email)


        val cursor = db.query(TABLE_NAME, //Table to query
            columns,        //columns to return
            selection,      //columns for the WHERE clause
            selectionArgs,  //The values for the WHERE clause
            null,  //group the rows
            null,   //filter by row groups
            null)  //The sort order


        val cursorCount = cursor.count
        cursor.close()
        db.close()

        if (cursorCount > 0) {
            return true
        }

        return false
    }

    fun checkUser(email: String, password: String): Boolean {

        // array of columns to fetch
        val columns = arrayOf(KEY_ID)

        val db = this.readableDatabase

        // selection criteria
        val selection = "$KEY_EMAIL = ? AND $KEY_PASSWORD = ?"

        // selection arguments
        val selectionArgs = arrayOf(email, password)


        val cursor = db.query(
            TABLE_NAME, //Table to query
            columns, //columns to return
            selection, //columns for the WHERE clause
            selectionArgs, //The values for the WHERE clause
            null,  //group the rows
            null, //filter by row groups
            null) //The sort order

        val cursorCount = cursor.count
        cursor.close()
        db.close()

        if (cursorCount > 0)
            return true

        return false

    }

}