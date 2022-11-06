package com.jade.kotlindemo.page.dataStore

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.jade.kotlindemo.BaseActivity
import com.jade.kotlindemo.R

class PreferencesDataStoreActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val keyEditText = findViewById<EditText>(R.id.key_edit_text)
        val valueEditText = findViewById<EditText>(R.id.value_edit_text)
        val saveDataButton = findViewById<Button>(R.id.save_data_button)
        saveDataButton.setOnClickListener {
            val key = keyEditText.text.toString()
            val value = valueEditText.text.toString()
        }
    }
}