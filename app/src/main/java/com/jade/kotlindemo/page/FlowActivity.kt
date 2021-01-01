package com.jade.kotlindemo.page

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FlowActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val refreshChannel = ConflatedBroadcastChannel<Int>()
        val flow: Flow<Int> = channelFlow {
            refreshChannel.asFlow()
                .onStart {
                    emit(1)
                }.scan(0) { a, b ->
                    Log.i("pby123", "a = $a, b = $b")
                    return@scan b
                }.filterNot {
                    it == 0
                }
                .collect {
                    Log.i("pby123", "it = $it")
                    send(it)
                }
        }
        GlobalScope.launch {
            flow.collect {
                Log.i("pby123", "it1 = $it")
            }
        }
//        GlobalScope.launch {
//            Log.i("pby123", "refresh")
//            refreshChannel.offer(3)
//        }

    }
}