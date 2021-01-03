package com.jade.kotlindemo

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        System.setProperty("kotlinx.coroutines.debug", "on")
    }
}