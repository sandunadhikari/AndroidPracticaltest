package com.example.practicaltest

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context):SQLiteOpenHelper(context,DATABASE_NAME,FACTORY,DATABASE_VERSION) {
    companion object{
        internal val FACTORY  = null
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "userDB"
        private val TABLE_NAME = "UserTable"
        private val KEY_ID = "id"
        private val KEY_NAME = "name"
        private val KEY_EMAIL = "email"
        private val KEY_PASSWORD = "password"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_PASSWORD + " TEXT" + ")")
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun insertUserData(name: String,email: String,password: String){
        val db: SQLiteDatabase = writableDatabase
        val values: ContentValues = ContentValues()
        values.put(KEY_NAME,name)
        values.put(KEY_EMAIL,email)
        values.put(KEY_PASSWORD,password)
        db.insert(TABLE_NAME,null,values)
        db.close()
    }

    fun userPresent(email: String,password: String): Boolean{
        val db = writableDatabase
        val query = ("SELECT * FROM "+ TABLE_NAME +" WHERE "+ KEY_EMAIL +" = '$email' AND "+ KEY_PASSWORD +" = '$password'")
        val cursor = db.rawQuery(query,null)
        if (cursor.count <= 0){
            cursor.close()
            return false
        }
        cursor.close()
        return true
    }

    fun userExsit(email: String): Boolean{
        val db = writableDatabase
        val query = ("SELECT * FROM "+ TABLE_NAME +" WHERE "+ KEY_EMAIL +" = '$email'")
        val cursor = db.rawQuery(query,null)
        if (cursor.count <= 0){
            cursor.close()
            return false
        }
        cursor.close()
        return true
    }
}