package com.jade.kotlindemo.page.nav

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class NavCommonFragment: NavBaseFragment() {

    override fun getInflateView(inflater: LayoutInflater): View? {
        return TextView(inflater.context).apply {
            text = "我是NavCommonFragment"
            textSize = 100F
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            gravity = Gravity.CENTER
        }
    }
}