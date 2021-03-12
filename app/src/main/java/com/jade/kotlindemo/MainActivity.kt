package com.jade.kotlindemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jade.kotlindemo.helper.DataBaseHelper
import com.jade.kotlindemo.page.animation.AnimationActivity
import com.jade.kotlindemo.page.flow.FlowActivity
import com.jade.kotlindemo.page.paging3.Paging3RouterActivity
import com.jade.kotlindemo.page.paging3.page.Paging3WithNetWorkActivity
import com.jade.kotlindemo.page.room.RoomActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        initClick<FlowActivity>(R.id.flow)
        initClick<Paging3RouterActivity>(R.id.paging3)
        initClick<RoomActivity>(R.id.room)
        initClick<AnimationActivity>(R.id.animation)
    }

    private fun init() {
        DataBaseHelper.initDataBase(this)
    }

    private inline fun <reified T : Activity> initClick(id: Int) {
        findViewById<View>(id).setOnClickListener {
            val intent = Intent(this, T::class.java)
            startActivity(intent)
        }
    }
}