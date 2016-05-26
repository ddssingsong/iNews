package com.jhs.inews.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * 下拉弹动布局
 * @author dds
 *
 */
public class BouncyLinearLayout extends LinearLayout {  
	  
    private Scroller mScroller;  
    private GestureDetector mGestureDetector;  
      
    public BouncyLinearLayout(Context context) {  
        this(context, null);  
    }  
      
    public BouncyLinearLayout(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        setClickable(true);  
        setLongClickable(true);  
        mScroller = new Scroller(context);  
        mGestureDetector = new GestureDetector(context, new BouncyGestureListener());  
    }  
  

    protected void smoothScrollTo(int fx, int fy) {  
        int dx = fx - mScroller.getFinalX();  
        int dy = fy - mScroller.getFinalY();  
        smoothScrollBy(dx, dy);  
    }  
  

    protected void smoothScrollBy(int dx, int dy) {  
        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy);
        invalidate();
    }  
      
    @Override  
    public void computeScroll() {  
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }  
        super.computeScroll();  
    }  
      
    @Override  
    public boolean onTouchEvent(MotionEvent event) {  
        switch (event.getAction()) {  
        case MotionEvent.ACTION_UP :  
            smoothScrollTo(0, 0);  
            break;  
        default:  
            return mGestureDetector.onTouchEvent(event);  
        }  
        return super.onTouchEvent(event);  
    }  
      
    class BouncyGestureListener implements GestureDetector.OnGestureListener {  
  
        @Override  
        public boolean onDown(MotionEvent e) {  
            return true;  
        }  
  
        @Override  
        public void onShowPress(MotionEvent e) {  
              
        }  
  
        @Override  
        public boolean onSingleTapUp(MotionEvent e) {  
            return false;  
        }  
  
        @Override  
        public boolean onScroll(MotionEvent e1, MotionEvent e2,  
                float distanceX, float distanceY) {  
            int dis = (int)((distanceY-0.5)/2);  
            smoothScrollBy(0, dis);  
            return false;  
        }  
  
        @Override  
        public void onLongPress(MotionEvent e) {  
              
        }  
  
        @Override  
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,  
                float velocityY) {  
            return false;  
        }  
          
    }  
}  
