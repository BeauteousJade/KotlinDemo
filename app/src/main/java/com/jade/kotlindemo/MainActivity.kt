package com.jade.kotlindemo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jade.kotlindemo.helper.DataBaseHelper
import com.jade.kotlindemo.page.flow.FlowActivity
import com.jade.kotlindemo.page.paging3.Paging3RouterActivity
import com.jade.kotlindemo.page.paging3.page.Paging3WithNetWorkActivity
import com.jade.kotlindemo.page.room.RoomActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        initClick(R.id.flow, FlowActivity::class.java)
        initClick(R.id.paging3, Paging3RouterActivity::class.java)
        initClick(R.id.room, RoomActivity::class.java)
    }

    private fun init() {
        DataBaseHelper.initDataBase(this)
    }

    private fun initClick(id: Int, clazz: Class<*>) {
        findViewById<View>(id).setOnClickListener {
            val intent = Intent(this, clazz)
            startActivity(intent)
        }
    }
}