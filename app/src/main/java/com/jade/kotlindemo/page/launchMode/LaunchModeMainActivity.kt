package com.jade.kotlindemo.page.launchMode

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jade.kotlindemo.R

class LaunchModeMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch_mode)
        findViewById<TextView>(R.id.textView).text = "我是LaunchModeMainActivity"
        findViewById<Button>(R.id.button).apply {
            text = "点击启动到LaunchModeSecondActivity"
            setOnClickListener {
                val intent =
                    Intent(this@LaunchModeMainActivity, LaunchModeSecondActivity::class.java)
                startActivity(intent)
            }
        }
    }
}