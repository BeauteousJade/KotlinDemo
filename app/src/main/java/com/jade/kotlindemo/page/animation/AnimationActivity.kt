package com.jade.kotlindemo.page.animation

import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.util.Printer
import android.view.Choreographer
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import com.jade.kotlindemo.R
import java.lang.StringBuilder

class AnimationActivity : AppCompatActivity() {

    private val mUIHandler = Handler(Looper.getMainLooper())
    private var count = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation)


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            mUIHandler.looper.queue.addIdleHandler {
                Log.i("pby123", "addIdleHandler")
                true
            }
        }
        startAnimation()
//        printLogger()
    }


    private fun printLogger() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            mUIHandler.looper.setMessageLogging {
                val queue = mUIHandler.looper.queue
                if (count < 1000) {
                    val declaredFields = Choreographer::class.java.declaredFields;
                    val field = Choreographer::class.java.getDeclaredField("mCallbackQueues")
                    field.isAccessible = true
                    val mCallbackQueues = field.get(Choreographer.getInstance()) as Array<Any>
                    val callback = mCallbackQueues[3]
                    val a = callback::class.java.declaredMethods
                    a[0].name
                    val headField = callback::class.java.getDeclaredField("mHead")
                    headField.isAccessible = true
                    val head = headField.get(callback)
                    var next: Any? = head
                    val stringBuilder = StringBuilder()
                    while (next != null) {
                        val actionField = next::class.java.getDeclaredField("action")
                        actionField.isAccessible = true
                        val action = actionField.get(next)
                        stringBuilder.append("$action----")
                        val nextField = next::class.java.getDeclaredField("next")
                        nextField.isAccessible = true
                        next = nextField.get(next)
                    }


                    Log.i(
                        "pby123",
                        "stringBuilder = $stringBuilder"
                    )
                }
                count++
            }

        }
    }

    private fun startAnimation() {
        val animation = AlphaAnimation(1f, 0.5f)
        animation.duration = 100
        animation.repeatCount = -1
        animation.repeatMode = Animation.REVERSE
        val view = findViewById<View>(R.id.view)
        view.startAnimation(animation)
    }

    private fun startAnimator() {
        val view = findViewById<View>(R.id.view)
        val valueAnimator = ValueAnimator.ofFloat(1f, 0.5f)
        valueAnimator.repeatCount = -1
        valueAnimator.repeatMode = ValueAnimator.REVERSE
        valueAnimator.addUpdateListener {
            val value = it.animatedValue?.run {
                (this as? Float) ?: 1f
            } ?: 1f
            view.scaleX = value
            view.scaleY = value
        }
        valueAnimator.start()
    }

}