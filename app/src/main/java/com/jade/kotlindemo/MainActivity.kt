package com.jade.kotlindemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.jade.kotlindemo.helper.DataBaseHelper
import com.jade.kotlindemo.page.animation.AnimationActivity
import com.jade.kotlindemo.page.aspectj.AspectJDemoActivity
import com.jade.kotlindemo.page.constraint.ConstraintDemoActivity
import com.jade.kotlindemo.page.flow.FlowActivity
import com.jade.kotlindemo.page.fragment.TestFragmentActivity
import com.jade.kotlindemo.page.nested.NestedScrollActivity
import com.jade.kotlindemo.page.paging3.Paging3RouterActivity
import com.jade.kotlindemo.page.paging3.page.Paging3WithNetWorkActivity
import com.jade.kotlindemo.page.room.RoomActivity

class MainActivity : AppCompatActivity() {

    private lateinit var mContainer: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        addPage<FlowActivity>("flow")
        addPage<Paging3RouterActivity>("paging3")
        addPage<RoomActivity>("room")
        addPage<AnimationActivity>("animation")
        addPage<ConstraintDemoActivity>("constraintDemo")
        addPage<TestFragmentActivity>("testFragment")
        addPage<AspectJDemoActivity>("AspectJDemo")
        addPage<NestedScrollActivity>("nestedScroll")
    }

    private fun init() {
        DataBaseHelper.initDataBase(this)
        mContainer = findViewById(R.id.container)
    }

    private inline fun <reified T : Activity> addPage(buttonText: String) {
        val button = Button(this).apply {
            text = buttonText
            gravity = Gravity.CENTER
            isAllCaps = false
            setOnClickListener {
                val intent = Intent(this@MainActivity, T::class.java)
                startActivity(intent)
            }
        }
        mContainer.addView(
            button,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

}