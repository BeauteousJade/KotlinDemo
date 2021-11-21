package com.jade.kotlindemo.page.nav

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.fragment.NavHostFragment
import com.jade.kotlindemo.R

class NavActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav)
        val navHostFragment = NavHostFragment.create(R.navigation.nav_graph, bundleOf("pby" to "1"))
        val view = findViewById<View>(R.id.nav_host_fragment)
        supportFragmentManager.beginTransaction().add(view.id, navHostFragment).commit()
    }
}