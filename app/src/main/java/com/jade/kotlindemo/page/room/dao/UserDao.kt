package com.jade.kotlindemo.page.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.jade.kotlindemo.page.room.bean.User

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