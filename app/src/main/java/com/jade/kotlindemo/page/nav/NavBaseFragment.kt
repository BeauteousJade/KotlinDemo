package com.jade.kotlindemo.page.nav

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

open class NavBaseFragment : Fragment() {

    private val mTag = this.javaClass.name


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        log("onCreate bundle = $arguments")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        log("onCreateView")
        if (getLayoutId() != 0) {
            return inflater.inflate(getLayoutId(), container, false)
        }
        return getInflateView(inflater)
    }

    @LayoutRes
    protected open fun getLayoutId(): Int {
        return 0
    }

    protected open fun getInflateView(inflater: LayoutInflater): View? {
        return null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        log("onViewCreated")
    }

    override fun onStart() {
        super.onStart()
        log("onStart")
    }

    override fun onResume() {
        super.onResume()
        log("onResume")
    }

    override fun onPause() {
        super.onPause()
        log("onPause")
    }

    override fun onStop() {
        super.onStop()
        log("onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        log("onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
    }

    protected fun log(msg: String) {
        Log.i(mTag, "$msg, hashCode = ${this.hashCode()}")
    }
}