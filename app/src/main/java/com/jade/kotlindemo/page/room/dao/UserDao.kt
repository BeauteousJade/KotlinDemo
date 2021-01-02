package com.jade.kotlindemo.page.room.dao

import androidx.room.*
import com.jade.kotlindemo.page.room.bean.User
import com.jade.kotlindemo.page.room.bean.UserAndLibrary

@Dao
interface UserDao {

    @Query("Select * from user")
    fun getAll(): List<User>

    @Query("select * from user where user_id = :userId")
    fun getUserById(userId: String): User

    @Delete
    fun delete(user: User)

    @Query("delete from user")
    fun clearUser();

    @Insert
    fun insertUser(vararg users: User)
}