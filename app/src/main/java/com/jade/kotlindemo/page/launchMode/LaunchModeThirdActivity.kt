package com.jade.kotlindemo.page.launchMode

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jade.kotlindemo.R

class LaunchModeThirdActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch_mode)
        findViewById<TextView>(R.id.textView).text = "我是LaunchModeSecondActivity"
    }
}