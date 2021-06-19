package com.jade.kotlindemo.page.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jade.kotlindemo.R
import com.jade.kotlindemo.helper.fragment.BaseFragment

class TestFragmentActivity : AppCompatActivity() {

    private val mFragmentRed = BaseFragment.newInstance(R.layout.fragment_red)
    private val mFragmentGreen = BaseFragment.newInstance(R.layout.fragment_green)
    private var mCurrentFragment: BaseFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_test)
        findViewById<View>(R.id.add).setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                mCurrentFragment = mFragmentRed
                add(R.id.container, mFragmentRed)
                commitNowAllowingStateLoss()
            }
        }
        findViewById<View>(R.id.remove).setOnClickListener {
            mCurrentFragment?.let {
                supportFragmentManager.beginTransaction().apply {
                    remove(it)
                    commitNowAllowingStateLoss()
                    mCurrentFragment = null
                }
            }
        }
        findViewById<View>(R.id.show).setOnClickListener {
            mCurrentFragment?.let {
                supportFragmentManager.beginTransaction().apply {
                    show(it)
                    commitNowAllowingStateLoss()
                }
            }
        }
        findViewById<View>(R.id.hide).setOnClickListener {
            mCurrentFragment?.let {
                supportFragmentManager.beginTransaction().apply {
                    hide(it)
                    commitNowAllowingStateLoss()
                }
            }
        }

        findViewById<View>(R.id.attach).setOnClickListener {
            mCurrentFragment?.let {
                supportFragmentManager.beginTransaction().apply {
                    attach(it)
                    commitNowAllowingStateLoss()
                }
            }
        }

        findViewById<View>(R.id.detach).setOnClickListener {
            mCurrentFragment?.let {
                supportFragmentManager.beginTransaction().apply {
                    detach(it)
                    commitNowAllowingStateLoss()
                }
            }
        }

        findViewById<View>(R.id.replace).setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                mCurrentFragment = if (mCurrentFragment == mFragmentRed) mFragmentGreen else mFragmentRed
                replace(R.id.container, mCurrentFragment!!)
                commitNowAllowingStateLoss()
            }
        }
    }
}