package com.example.liyangos3323.androidotest;

import android.content.Context;

/**
 * Created by liyangos3323 on 2017/12/8.
 */

public class DB {
    private Context context;

    public DB getInstance(Context context){
        this.context = context;
        return new DB();
    }
    public static void insert(){
        
    }
}
