package com.jade.kotlindemo.page.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jade.kotlindemo.page.paging3.dataBase.Message
import com.jade.kotlindemo.page.paging3.dataBase.MessageDao
import com.jade.kotlindemo.page.room.bean.User
import com.jade.kotlindemo.page.room.dao.UserDao

@Database(entities = [User::class, Message::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun messageDao(): MessageDao
}