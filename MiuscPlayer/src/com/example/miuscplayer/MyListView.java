package com.example.miuscplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.ListView;

/**
 * Created by he.b.wang on 15/5/14.
 */
public class MyListView extends ListView {

    private final String TAG = "MyListView";
    private VelocityTracker mVelocityTracker;

    private boolean isHorizontal = false;
    private boolean isFrist = true;

    private int startX;
    private View itemView;

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "dispatchTouchEvent ACTION_DOWN");
                itemView = null;
                isHorizontal = false;
                isFrist = true;
                if (mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain();
                } else {
                    mVelocityTracker.clear();
                }
                mVelocityTracker.addMovement(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "dispatchTouchEvent ACTION_MOVE");
                mVelocityTracker.addMovement(ev);
                mVelocityTracker.computeCurrentVelocity(1000);
                Log.d(TAG, Math.abs(mVelocityTracker.getXVelocity()) + "   " + Math.abs(mVelocityTracker.getYVelocity()));
                if (Math.abs(mVelocityTracker.getXVelocity()) > Math.abs(mVelocityTracker.getYVelocity()) && isFrist) {
                    Log.d(TAG, "横向");
                    isHorizontal = true;
                    isFrist = false;
                    int i = pointToPosition((int) ev.getX(), (int) ev.getY());
                    int f = getFirstVisiblePosition();
                    itemView = getChildAt(i - getFirstVisiblePosition());
                    return true;
                }
                isFrist = false;
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "dispatchTouchEvent ACTION_UP");
                isHorizontal = false;
                isFrist = false;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "onTouchEvent ACTION_DOWN");
                startX = (int) ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "onTouchEvent ACTION_MOVE");
                if (isHorizontal) {
                    Log.d(TAG, "ListView横向滑动");

                    int temp = startX - (int)ev.getX();
                    startX = (int) ev.getX();
                    if (itemView != null) {
                        itemView.scrollBy(temp, 0);
                        Log.d(TAG, "View " + itemView + "   ViewParent:" + itemView.getParent());
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "onTouchEvent ACTION_UP");
                break;
        }
        return super.onTouchEvent(ev);
    }

}
