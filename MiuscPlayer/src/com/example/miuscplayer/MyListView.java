package com.example.miuscplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Scroller;

/**
 * Created by he.b.wang on 15/5/14.
 */
public class MyListView extends ListView {

    private final String TAG = "MyListView";
    private VelocityTracker mVelocityTracker;
    private Scroller mScroller;
    private boolean isHorizontal = false;
    private boolean isFrist = true;

    private int startX;
    private View itemView;
    private int position;
    private OnDeleteListener onDeleteListener;

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }

    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    public OnDeleteListener getOnDeleteListener() {
        return onDeleteListener;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
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
                mVelocityTracker.addMovement(ev);
                mVelocityTracker.computeCurrentVelocity(1000);
                if (mVelocityTracker.getXVelocity() < 0 && Math.abs(mVelocityTracker.getXVelocity()) > Math.abs(mVelocityTracker.getYVelocity()) && isFrist) {
                    Log.d(TAG, "横向");
                    isHorizontal = true;
                    isFrist = false;
                    position = pointToPosition((int) ev.getX(), (int) ev.getY());
                    itemView = getChildAt(position - getFirstVisiblePosition());
                    return true;
                }
                isFrist = false;
                break;
            case MotionEvent.ACTION_UP:
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
                startX = (int) ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.addMovement(ev);
                mVelocityTracker.computeCurrentVelocity(1000);
                if (isHorizontal) {
                    Log.d(TAG, "ListView横向滑动");
                    int width = getWidth();
                    int temp = startX - (int)ev.getX();

                    startX = (int) ev.getX();
                    if (itemView != null) {
                        Log.d(TAG, "itemWidth:" + width + "   scrollX:" + itemView.getScrollX());
                    }
                    if (itemView != null && (mVelocityTracker.getXVelocity() < 0 || itemView.getScrollX() > 0)) {
                        itemView.scrollBy(temp, 0);
                        if (itemView.getScrollX() < 0) {
                            itemView.scrollTo(0, 0);
                        }

                    }
                    if (itemView != null && itemView.getScrollX() >= width) {
                        deleteItem();
                        MotionEvent e = MotionEvent.obtain(ev);
                        e.setAction(MotionEvent.ACTION_CANCEL);
                        dispatchTouchEvent(e);
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (itemView != null) {
                    int scrollX = itemView.getScrollX();
                    int itemWidth = itemView.getWidth();
                    int offset = 0;
                    if (scrollX * 2 > itemWidth) {
                        offset = itemWidth - scrollX;
                    } else {
                        offset = -scrollX;
                    }
                    Log.d(TAG, "scroll scrollX:" + scrollX + "  offset:" + offset);
                    mScroller.startScroll(scrollX, 0, offset, 0);
                    invalidate();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void deleteItem() {
        Log.d(TAG, "deleteItem:" + position + "   " + itemView);
        if (onDeleteListener != null) {
            onDeleteListener.onDeleter(position, this);
        }
        itemView.scrollTo(0, 0);
        itemView = null;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset() && itemView != null) {
            Log.d(TAG, "currX:" + mScroller.getCurrX());
            itemView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            Log.d(TAG, "itemWidth:" + itemView.getWidth() + "   scrollX:" + itemView.getScrollX());
            if (itemView.getScrollX() >= itemView.getWidth()) {
                deleteItem();
            }
            invalidate();
        }
    }

    public interface OnDeleteListener{
        public void onDeleter(int position, AdapterView listView);
    }

}
