package com.jade.kotlindemo.page.launchMode

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jade.kotlindemo.R

class LaunchModeSecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch_mode)
        findViewById<TextView>(R.id.textView).text = "我是LaunchModeSecondActivity"
        findViewById<Button>(R.id.button).apply {
            text = "点击启动到LaunchModeThirdActivity"
            setOnClickListener {
                val intent =
                    Intent(this@LaunchModeSecondActivity, LaunchModeThirdActivity::class.java)
                startActivity(intent)
            }
        }
    }
}