package com.jade.kotlindemo.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jade.kotlindemo.room.bean.User
import com.jade.kotlindemo.room.dao.UserDao

@Database(entities = arrayOf(User::class), version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun userDao(): UserDao
}