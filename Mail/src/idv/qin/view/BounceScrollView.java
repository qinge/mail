package idv.qin.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;


public class BounceScrollView extends ScrollView {
	private View innerViewTree;

	private float y;

	private Rect normal = new Rect();

	private boolean isCount = false;
	
	private final static int VALIDATE_SPAN = 40;

	private BounceScrollViewListener mListener  = null;
	
	private float xDistance, yDistance, xLast, yLast;
	

	public BounceScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();
                
                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;
                
                if(xDistance > yDistance){
                    return false;
                }  
        }

        return super.onInterceptTouchEvent(ev);
    }
	

	@Override
	protected void onFinishInflate() {
		if (getChildCount() > 0) {
			innerViewTree = getChildAt(0);
		}
	}
	

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (innerViewTree != null) {
			commOnTouchEvent(ev);
		}

		return super.onTouchEvent(ev);
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		if(null == mListener)return;
		int dy = t-oldt;
		//Log.e("",String.format("l:%d,t:%d,oldl:%d,oldt:%d,dy:%d", l,t,oldl,oldt,t-oldt));
		
		if( Math.abs(dy)<VALIDATE_SPAN)return;
		
		if(dy>0)
			mListener.onScrollUp();
		else if(dy<0)
			mListener.onScrollDown();
			
	}

	public void commOnTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_UP:
			if (isNeedAnimation()) {
				animation();
				isCount = false;
			}
			break;

		case MotionEvent.ACTION_MOVE:
			final float preY = y;
			float nowY = ev.getY();
			int deltaY = (int) (preY - nowY);
			if (!isCount) {
				deltaY = 0;
			}

			y = nowY;

			if (isNeedMove()) {
				if (normal.isEmpty()) {
					normal.set(innerViewTree.getLeft(), innerViewTree.getTop(),
							innerViewTree.getRight(), innerViewTree.getBottom());
				}

				innerViewTree.layout(innerViewTree.getLeft(), innerViewTree.getTop() - deltaY / 2,
						innerViewTree.getRight(), innerViewTree.getBottom() - deltaY / 2);
			}
			isCount = true;
			break;

		default:
			break;
		}
	}

	/**
	 * Scroll back
	 */
	public void animation() {
//		TranslateAnimation ta = new TranslateAnimation(0, 0, innerViewTree.getTop(),normal.top);

		if(null != mListener)mListener.onBacked();
		innerViewTree.layout(normal.left, normal.top, normal.right, normal.bottom);
		normal.setEmpty();
	}
	
	public void setBounceScrollViewListener(BounceScrollViewListener l){
		mListener = l;
	}
	

	public boolean isNeedAnimation() {
		return !normal.isEmpty();
	}

	public boolean isNeedMove() {
		int offset = innerViewTree.getMeasuredHeight() - getHeight();
		int scrollY = getScrollY();

		if (scrollY == 0 || scrollY == offset) {
			return true;
		}
		return false;
	}
	
	
	public interface BounceScrollViewListener{
		public void onBacked();
		public void onScrollUp();
		public void onScrollDown();
	}

	
//	@Override
//	protected float getTopFadingEdgeStrength() {
//	    if (getChildCount() == 0) {
//	        return 0.0f;
//	    }
//	    return 1.0f;
//	}
//	 
//	@Override
//	protected float getBottomFadingEdgeStrength() {
//	    if (getChildCount() == 0) {
//	        return 0.0f;
//	    }
//	    return 1.0f;
//	}
}
