package com.jade.kotlindemo.page.dataStore

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.jade.kotlindemo.BaseActivity
import com.jade.kotlindemo.R

class DataStoreActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_store)
        findViewById<View>(R.id.preferences_data_store).setOnClickListener {
            val intent =
                Intent(this@DataStoreActivity, PreferencesDataStoreActivity::class.java)
            startActivity(intent)
        }
    }
}