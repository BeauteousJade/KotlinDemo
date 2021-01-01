package com.jade.kotlindemo.helper

import android.content.Context
import androidx.annotation.MainThread
import androidx.room.Room
import com.jade.kotlindemo.page.room.AppDataBase

class DataBaseHelper {
    companion object {

        var dataBase: AppDataBase? = null

        @MainThread
        fun initDataBase(context: Context) {
            if (dataBase != null) {
                return
            }
            dataBase = Room.databaseBuilder(context, AppDataBase::class.java, "dataBase")
                .build();
        }
    }
}