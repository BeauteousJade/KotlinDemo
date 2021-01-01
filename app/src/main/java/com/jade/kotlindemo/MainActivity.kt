package com.jade.kotlindemo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jade.kotlindemo.page.FlowActivity
import com.jade.kotlindemo.paging3.Paging3Activity
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initClick(R.id.flow, FlowActivity::class.java)
        initClick(R.id.paging3, Paging3Activity::class.java)
    }


    private fun initClick(id: Int, clazz: Class<*>) {
        findViewById<View>(id).setOnClickListener {
            val intent = Intent(this, clazz)
            startActivity(intent)
        }
    }
}