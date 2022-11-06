package com.jade.kotlindemo

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.tencent.mmkv.MMKV


class App : Application() {

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "kotlinDemo")

    override fun onCreate() {
        super.onCreate()
        System.setProperty("kotlinx.coroutines.debug", "on")
        val rootDir = MMKV.initialize(this)
        println("mmkv root: $rootDir")
    }
}