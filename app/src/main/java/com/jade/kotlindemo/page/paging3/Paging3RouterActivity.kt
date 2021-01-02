package com.jade.kotlindemo.page.paging3

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jade.kotlindemo.R
import com.jade.kotlindemo.page.paging3.page.Paging3WithNetWorkActivity
import com.jade.kotlindemo.page.paging3.page.Paging3WithNetworkAndDataBaseActivity

class Paging3RouterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paging3_router)
        initClick(R.id.with_network, Paging3WithNetWorkActivity::class.java)
        initClick(
            R.id.with_network_and_data_base,
            Paging3WithNetworkAndDataBaseActivity::class.java
        )
    }

    private fun initClick(id: Int, clazz: Class<*>) {
        findViewById<View>(id).setOnClickListener {
            val intent = Intent(this, clazz)
            startActivity(intent)
        }
    }
}