package com.jhs.inews.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/**
 * 下拉弹动ScrollView
 * 
 * @author dds
 *
 */
public class MyScrollView extends ScrollView {

	private static final int size = 4;
	private View mRoot;
	private float y;
	private Rect normal = new Rect();;



	public MyScrollView(Context context) {
		super(context);
	}

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}

	@SuppressLint("MissingSuperCall")
	@Override
	protected void onFinishInflate() {
		if (getChildCount() > 0) {
			mRoot = getChildAt(0);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mRoot == null) {
			return super.onTouchEvent(ev);
		} else {
			commOnTouchEvent(ev);
		}
		return super.onTouchEvent(ev);
	}

	public void commOnTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			y = ev.getY();
			break;
		case MotionEvent.ACTION_UP:
			if (isNeedAnimation()) {
				animation();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			final float preY = y;
			float nowY = ev.getY();
			/**
			 */
			int deltaY = (int) (preY - nowY) / size;

			y = nowY;
			if (isNeedMove()) {
				if (normal.isEmpty()) {
					normal.set(mRoot.getLeft(), mRoot.getTop(), mRoot.getRight(), mRoot.getBottom());
					return;
				}
				int yy = mRoot.getTop() - deltaY;

				mRoot.layout(mRoot.getLeft(), yy, mRoot.getRight(), mRoot.getBottom() - deltaY);
			}
			break;
		default:
			break;
		}
	}

	public void animation() {
		TranslateAnimation ta = new TranslateAnimation(0, 0, mRoot.getTop() - normal.top, 0);
		ta.setDuration(200);
		mRoot.startAnimation(ta);
		mRoot.layout(normal.left, normal.top, normal.right, normal.bottom);
		normal.setEmpty();
	}

	public boolean isNeedAnimation() {
		return !normal.isEmpty();
	}

	public boolean isNeedMove() {
		int offset = mRoot.getMeasuredHeight() - getHeight();
		int scrollY = getScrollY();
		if (scrollY == 0 || scrollY == offset) {
			return true;
		}
		return false;
	}

	
}
