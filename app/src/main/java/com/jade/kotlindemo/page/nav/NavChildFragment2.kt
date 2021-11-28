package com.jade.kotlindemo.page.nav

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class NavChildFragment2 : NavBaseFragment() {


    override fun getInflateView(inflater: LayoutInflater): View {
        return TextView(inflater.context).apply {
            text = "我是Fragment2"
            textSize = 100F
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            gravity = Gravity.CENTER
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        log(arguments.toString())
    }

}