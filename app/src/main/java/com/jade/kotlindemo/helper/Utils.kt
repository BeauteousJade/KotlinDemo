package com.jade.kotlindemo.helper

import java.util.*

object Utils {

    @JvmStatic
    fun generateColor(): String {
        val random = Random()
        val red = Integer.toHexString(random.nextInt(256)).run {
            if (length == 1) "0$this" else this
        }
        val green = Integer.toHexString(random.nextInt(256)).run {
            if (length == 1) "0$this" else this
        }
        val blue = Integer.toHexString(random.nextInt(256)).run {
            if (length == 1) "0$this" else this
        }
        return "#${red}${green}${blue}"
    }
}