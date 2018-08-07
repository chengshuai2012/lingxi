package com.link.cloud.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by Administrator on 2017/12/12.
 */

public class RollListView extends ListView {
    public RollListView(Context context) {

        super(context);
    }
    public RollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RollListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){

        if(ev.getAction()==MotionEvent.ACTION_MOVE)
            return true;
        return super.dispatchTouchEvent(ev);
    }
}
