package com.jade.kotlindemo.room.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.jade.kotlindemo.room.bean.User

interface UserDao {

    @Query("Select * from user")
    fun getAll(): List<User>

    @Query("select * from user where user_id = :userId")
    fun getUserById(userId: String): User

    @Delete
    fun delete(user: User)

    @Insert
    fun insertUser(vararg users: User)
}