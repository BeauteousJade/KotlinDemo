package com.jade.kotlindemo.page.constraint

import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jade.kotlindemo.R
import com.jade.kotlindemo.helper.recyclerview.MyAdapter

class ConstraintDemoActivity : AppCompatActivity() {

    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isCustom = true
        setContentView(if (isCustom) R.layout.activity_constraint else R.layout.activity_constraint_v2)
        if (isCustom) {
            custom()
        } else {
            test()
        }
    }

    @ExperimentalStdlibApi
    private fun custom() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = MyAdapter(buildList {
            for (index in 0..100) {
                add("position = $index")
            }
        })
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val clickButton = findViewById<View>(R.id.click_button)
        val button = findViewById<View>(R.id.button)
        val animateView = findViewById<View>(R.id.animate_view)
        clickButton.setOnClickListener {
//            val layoutParams = button.layoutParams
//            layoutParams.width = 500
//            button.layoutParams = layoutParams

            val valueAnimator = ValueAnimator.ofInt(animateView.width, animateView.width + 100)
            valueAnimator.addUpdateListener {
                val layoutParams1 = animateView.layoutParams
                layoutParams1.width = valueAnimator.animatedValue as Int
                animateView.layoutParams = layoutParams1
            }
            valueAnimator.start()
        }
    }

    private fun test() {
        val view1 = findViewById<View>(R.id.view1)
        val view2 = findViewById<View>(R.id.view2)
        val view3 = findViewById<View>(R.id.view3)
        val button = findViewById<View>(R.id.button)
        button.setOnClickListener {

            Log.i(
                "pby123",
                "view1 = ${(view1.layoutParams as ConstraintLayout.LayoutParams).constraintWidget.isInHorizontalChain}, view2 = ${(view2.layoutParams as ConstraintLayout.LayoutParams).constraintWidget.isInHorizontalChain}, view3 = ${(view3.layoutParams as ConstraintLayout.LayoutParams).constraintWidget.isInHorizontalChain}"
            )
        }
    }
}