package com.jade.kotlindemo.page.nav

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController

class NavChildFragmentA : NavBaseFragment() {


    override fun getInflateView(inflater: LayoutInflater): View? {
        return Button(inflater.context).apply {
            text = "我是NavChildFragmentA"
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            gravity = Gravity.CENTER
            setOnClickListener {
                findNavController().navigate(NavChildFragmentADirections.actionAToB())
            }
        }
    }
}