package com.jade.kotlindemo.page.nav

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.jade.kotlindemo.R

class NavActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav)
        // 使用这种方式，Fragment不能返回了，以后看看原因
        val navHostFragment = NavHostFragment.create(R.navigation.nav_graph, bundleOf("pby" to "1"))
        val view = findViewById<View>(R.id.nav_host_fragment)
        supportFragmentManager.beginTransaction()
//            .add(view.id, navHostFragment, null)
//            .setReorderingAllowed(true)
//            .commitNowAllowingStateLoss()
    }
}