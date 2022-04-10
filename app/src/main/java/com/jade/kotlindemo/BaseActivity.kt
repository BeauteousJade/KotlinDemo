package com.jade.kotlindemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    companion object {
        private fun log(tag: String, msg: String) {
            Log.i(tag, msg)
        }
    }

    private val mIsPrintLog by lazy { isPrintLog() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dispatchLog("onCreate")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        dispatchLog("onSaveInstanceState")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        dispatchLog("onRestoreInstanceState");
    }

    override fun onStart() {
        super.onStart()
        dispatchLog("onStart")
    }

    override fun onRestart() {
        super.onRestart()
        dispatchLog("onRestart")
    }

    override fun onResume() {
        super.onResume()
        dispatchLog("onResume")
    }

    override fun onPause() {
        super.onPause()
        dispatchLog("onPause")
    }

    override fun onStop() {
        super.onStop()
        dispatchLog("onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        dispatchLog("onDestroy")
    }


    private fun dispatchLog(msg: String) {
        if (mIsPrintLog) {
            log(javaClass.simpleName, msg)
        }
    }


    protected open fun isPrintLog() = false

}