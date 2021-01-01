package com.jade.kotlindemo.page.room

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.jade.kotlindemo.R
import com.jade.kotlindemo.helper.DataBaseHelper
import com.jade.kotlindemo.page.room.bean.User
import kotlinx.coroutines.*

class RoomActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room);
        init()
    }

    private fun init() {
        findViewById<View>(R.id.add_user).setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                DataBaseHelper.dataBase?.run {
                    val user = User(name = "pby", age = 24, sex = 1)
                    userDao().insertUser(user)
                }
            }
        }
        findViewById<View>(R.id.clear_user).setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                DataBaseHelper.dataBase?.run {
                    userDao().clearUser()
                }
            }
        }
    }
}