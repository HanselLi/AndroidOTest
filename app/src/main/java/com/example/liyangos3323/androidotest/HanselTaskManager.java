package com.example.liyangos3323.androidotest;

import java.util.Objects;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by liyangos3323 on 2017/11/3.
 */

public class HanselTaskManager {
    public static void addBackGroundTask(){
        Task.callInBackground(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return null;
            }
        }).onSuccess(new Continuation<Object, Object>() {
            @Override
            public Object then(Task<Object> task) throws Exception {
                return null;
            }
        });
    }
}
