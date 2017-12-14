package com.example.liyangos3323.androidotest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.ArrayList;

/**
 * Created by liyangos3323 on 2017/11/22.
 */

public class HotWordView extends ViewGroup {

    private static final int CHILDNUM = 4;

    public HotWordView(Context context) {
        super(context);
    }

    public HotWordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
//        context.getResources().
    }

    public int getScreenWidth(){
        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int width = manager.getDefaultDisplay().getWidth();
        return width;
    }

    private void init(Context context) {
        initData(context);
    }
    ArrayList<String> list = new ArrayList<>();
    private void initData(Context context) {
        for (int i = 0; i < 40; i++) {
            list.add(" This is data " + i);
        }
    }

    public HotWordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        int screenWidth = getScreenWidth();
        for (int i = 0; i < CHILDNUM; i=i+2) {
            View childLeft = getChildAt(i);
            View childRight = getChildAt(i+1);

        }
    }
}
