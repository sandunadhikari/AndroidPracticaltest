package com.example.practicaltest.data

import androidx.lifecycle.LiveData

class UserRepository(private val userDao: UserDao) {

    val users = userDao.getAllUsers()

    suspend fun insert(user: User) {
        return userDao.insert(user)
    }

    suspend fun getUserName(userEmail: String):User?{
        return userDao.getUsername(userEmail)
    }

     suspend fun isExists(userEmail: String):Boolean{
        return userDao.userExists(userEmail)
    }
}