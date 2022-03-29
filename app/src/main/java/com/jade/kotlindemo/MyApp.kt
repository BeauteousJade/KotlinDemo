package com.jade.kotlindemo

import android.app.Application
import android.util.Log

class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        Log.i("pby123", "myApp onCreate")
    }
}