// Demo.aidl
package com.jade.kotlindemo;

import State;

// Declare any non-default types here with import statements

interface Demo {

 void test(in int a, out State state);
}