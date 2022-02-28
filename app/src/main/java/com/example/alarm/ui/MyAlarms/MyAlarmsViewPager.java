package com.example.alarm.ui.MyAlarms;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class MyAlarmsViewPager extends ViewPager {

    public enum SwipeDirection {
        ALL, LEFT, RIGHT, NONE;
    }
    private float initialXValue;
    private SwipeDirection direction;

    public MyAlarmsViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.direction = SwipeDirection.ALL;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(this.isSwipeAllowed(event)) {
            return super.onTouchEvent(event);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if(this.isSwipeAllowed(event)) {
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }

    private boolean isSwipeAllowed(MotionEvent event) {
        if(this.direction == SwipeDirection.ALL) return true;
        if(this.direction == SwipeDirection.NONE) return false;
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            initialXValue = event.getX();
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float diffX = event.getX() - initialXValue;
            if(diffX > 0 && direction == SwipeDirection.RIGHT) {
                return false;
            } else if (diffX < 0 && direction == SwipeDirection.LEFT) {
                return false;
            }
        }
        return true;
    }

    public void setAllowedSwipeDirection(SwipeDirection direction) {
        this.direction = direction;
    }
}
