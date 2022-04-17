package com.jade.kotlindemo

import android.app.Application
import com.tencent.mmkv.MMKV


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        System.setProperty("kotlinx.coroutines.debug", "on")
        val rootDir = MMKV.initialize(this)
        println("mmkv root: $rootDir")
    }
}