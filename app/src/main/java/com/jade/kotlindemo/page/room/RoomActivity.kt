package com.jade.kotlindemo.page.room

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
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
        findViewById<View>(R.id.update_user).setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val userLiveData = DataBaseHelper.dataBase.userDao().getUserByIdV2("1")
                lifecycleScope.launch(Dispatchers.Main) {
                    userLiveData.observe(this@RoomActivity) {
                        Log.i("pby123", "observe user = $it")
                    }
                }
                // liveData 赋值时时通过postValue实现的，这里直接获取value是为空，
                // 所以先delay 100ms,保证先更新完值，才去获取正确的值
                delay(100)
                val oldUser = userLiveData.value
                Log.i("pby123", "user = $oldUser")
                oldUser?.let {
                    val newUser = User(
                        userId = oldUser.userId,
                        name = "new pby",
                        age = oldUser.age,
                        sex = oldUser.sex
                    )
                    DataBaseHelper.dataBase.userDao().updateUser(newUser)
                }
            }
        }
    }
}