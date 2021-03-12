package com.jade.kotlindemo.page.animation;

import android.view.Choreographer;

import java.lang.reflect.Field;

public class Utils {

    public static void help() {
        Choreographer instance = Choreographer.getInstance();
        Field[] declaredFields = instance.getClass().getDeclaredFields();
        int a = 10;
    }
}
