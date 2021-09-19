package com.jade.kotlindemo;


import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class MethodExecAspectJ {

    @Pointcut("call (* com.jade.kotlindemo.page.aspectj.AspectJDemoActivity.**(..))")
    public void pointCut() {

    }

    @Around("pointCut()")
    public void onMethodExec(ProceedingJoinPoint joinPoint) {
        long time = -System.currentTimeMillis();
        try {
            joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        time += System.currentTimeMillis();
        String s = joinPoint.getSignature().toString();
        Log.i("pby123", "time = " + time + " string = " + s);
    }
}
