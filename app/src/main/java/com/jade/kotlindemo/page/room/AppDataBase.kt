package com.jade.kotlindemo.page.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jade.kotlindemo.page.room.bean.User
import com.jade.kotlindemo.page.room.dao.UserDao

@Database(entities = arrayOf(User::class), version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun userDao(): UserDao
}