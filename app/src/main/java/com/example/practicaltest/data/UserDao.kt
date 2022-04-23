package com.example.practicaltest.data

import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(register: User)

    @Query("SELECT * FROM user_table ORDER BY userId DESC")
    fun getAllUsers(): LiveData<List<User>>

    @Query("SELECT EXISTS(SELECT 1 FROM user_table WHERE user_email = :userEmail)")
    suspend fun userExists(userEmail : String) : Boolean

    @Query("SELECT * FROM user_table WHERE user_name LIKE :userEmail")
    suspend fun getUsername(userEmail: String): User?

}