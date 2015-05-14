package com.example.miuscplayer;

import android.content.Context;
import android.os.Debug;
import android.os.StrictMode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.animation.BaseInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.OverScroller;
import android.widget.Scroller;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EViewGroup;

/**
 * Created by he.b.wang on 15/5/13.
 */
@EViewGroup
public class ScrollLinearLayout extends LinearLayout {

    private static String TAG = "ScrollLinearLayout";
    private Scroller mScroller;
//    private OverScroller mScroller;
    private VelocityTracker mVelocityTracker;
//    private MyInterpolator interpolator;
    private ScrollerRunnable scrollerRunnable;

    private int startY;

    public ScrollLinearLayout(Context context){
        super(context);
    }

    public ScrollLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @AfterInject
    public void init(){
        Log.d(TAG, "init");
//        interpolator = new MyInterpolator();
        mScroller = new Scroller(getContext());
    }

    class MyInterpolator extends BaseInterpolator{

        @Override
        public float getInterpolation(float input) {
            Log.d(TAG, "speed:" + input);
            return 0;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "onTouchEvent ACTION_DOWN");
                if (mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain();
                } else {
                    mVelocityTracker.clear();
                }
                if (scrollerRunnable != null) {
                    scrollerRunnable.setFininsh(true);
                }
                startY = (int) event.getRawY();
                return true;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "onTouchEvent ACTION_MOVE");
                int temp = startY - (int) event.getRawY();
                int currY = getScrollY();
                if (currY <= 0 && temp < 0) {

//                    scrollTo(0, 0);
                } else {
                    scrollBy(0, temp);
                }
                mVelocityTracker.addMovement(event);
                startY = (int) event.getRawY();
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "onTouchEvent ACTION_UP");
                int positionY = getScrollY();
                mVelocityTracker.addMovement(event);
                mVelocityTracker.computeCurrentVelocity(1000);
                float initVelocity = mVelocityTracker.getYVelocity();

                Log.d(TAG, "start speed:" + initVelocity);
//                mScroller.startScroll(0, positionY, 0, -200, 1000);
                if(scrollerRunnable == null){
                    scrollerRunnable = new ScrollerRunnable();
                }

                scrollerRunnable.start((int) initVelocity);
                invalidate();
                return true;
            case MotionEvent.ACTION_CANCEL:
                mVelocityTracker.recycle();
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    class ScrollerRunnable implements Runnable{

        private int oldSpeed;
        private int currSpeed;

        private int sum = 0;

        private static final int T = 15;

        private boolean isFininsh = false;

        public boolean isFinish(){
            return isFininsh;
        }

        public void setFininsh(boolean isFininsh){
            this.isFininsh = isFininsh;
        }

        public void start(int speed) {
            isFininsh = false;
            oldSpeed = currSpeed;
            currSpeed = speed;
            Log.d(TAG, "old_speed:" + oldSpeed + "    curr_speed:" + currSpeed + "   runnable:" + this + "   currTime:" + System.currentTimeMillis());
            postOnAnimation(this);
        }

        @Override
        public void run() {
            mVelocityTracker.computeCurrentVelocity(1000);
            int temp = currSpeed > 0 ? (currSpeed - 100) : (currSpeed + 100);
            if(oldSpeed != 0){
                int currY = getScrollY();
                int s = (oldSpeed + currSpeed) * T / 1000;
                sum += s;
                Log.d(TAG, "s:" + s);
                if (currY < 0) {
                    s = 0;
                    isFininsh = true;
                } else if (currY > getHeight()) {
                    s = getHeight();
                    isFininsh = true;
                }
                scrollBy(0, -s);
            }
            if(!isFininsh && ((currSpeed > 0 && temp > 0) || (currSpeed < 0 && temp < 0))){
                start(temp);
            }else{
                Log.d(TAG, "sum:" + sum);
                isFininsh = true;
            }
        }
    }
}
