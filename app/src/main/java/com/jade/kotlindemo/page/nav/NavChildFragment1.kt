package com.jade.kotlindemo.page.nav

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.jade.kotlindemo.R
import kotlinx.android.synthetic.main.fragment_nav_child1.*

class NavChildFragment1 : NavBaseFragment() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button1 = view.findViewById<Button>(R.id.button1)
        val button2 = view.findViewById<Button>(R.id.button2)
        val button3 = view.findViewById<Button>(R.id.button3)
        val button4 = view.findViewById<Button>(R.id.button4)
        button1.setOnClickListener {
            val action =
                NavChildFragment1Directions.actionNavChildFragment1ToNavChildFragment2()
            findNavController().navigate(action)
        }
        button2.setOnClickListener {
            val action = NavChildFragment1Directions.actionNavSecondActivity()
            findNavController().navigate(action)
        }
        button3.setOnClickListener {
            val action = NavChildFragment1Directions.actionNavThirdActivity()
            findNavController().navigate(action.actionId, bundleOf("userId" to "pby123"))
        }
        button4.setOnClickListener {
            val action = NavChildFragment1Directions.actionNavChildFragment1ToNavigation();
            findNavController().navigate(action.actionId)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_nav_child1
    }
}