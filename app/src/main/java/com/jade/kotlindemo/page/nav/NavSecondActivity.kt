package com.jade.kotlindemo.page.nav

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ActivityNavigator

class NavSecondActivity : AppCompatActivity() {

    override fun finish() {
        super.finish()
        ActivityNavigator.applyPopAnimationsToPendingTransition(this)
    }
}