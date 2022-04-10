package com.jade.kotlindemo.page.finish

import android.os.Bundle
import com.jade.kotlindemo.BaseActivity
import com.jade.kotlindemo.R

class LifecycleActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lifecycle)
    }

    override fun isPrintLog() = true

}