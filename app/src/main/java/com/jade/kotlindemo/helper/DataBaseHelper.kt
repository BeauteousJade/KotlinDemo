package com.jade.kotlindemo.helper

import android.content.Context
import androidx.annotation.MainThread
import androidx.room.Room
import com.jade.kotlindemo.page.room.AppDataBase

class DataBaseHelper {
    companion object {

        lateinit var dataBase: AppDataBase

        @MainThread
        fun initDataBase(context: Context) {
            if (this::dataBase.isInitialized) {
                return
            }
            dataBase = Room.databaseBuilder(context, AppDataBase::class.java, "dataBase")
                .build();
        }
    }
}