package idv.qin.view;

import idv.qin.mail.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class SideBar extends View {
	
	  // @auth qin 改变文字大小 设置 dm
    private DisplayMetrics displayMetrics;
    
    public void setDisplayMetrics(DisplayMetrics displayMetrics) {
		this.displayMetrics = displayMetrics;
	}  
    
	// 26个字母  
    public static String[] b = { "A", "B", "C", "D", "E", "F", "G", "H", "I",  
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",  
            "W", "X", "Y", "Z", "#" };  
    private int choose = -1;
    private  Paint paint = new Paint();
    private TextView mTextDialog;
    
    // 触摸事件  
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;  
    
    /** 
     * 向外公开的方法 
     *  
     * @param onTouchingLetterChangedListener 
     */  
    public void setOnTouchingLetterChangedListener(  
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {  
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;  
    }  
    
    /** 
     * 为SideBar设置显示字母的TextView 
     * @param mTextDialog 
     */  
    public void setTextView(TextView mTextDialog) {  
        this.mTextDialog = mTextDialog;  
    }  
    

	public SideBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	public SideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public SideBar(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 获取焦点改变背景色
		int height  = getHeight();
		int width = getWidth();
		int singleHeight = height / b.length ; // 获取每一个字母的高度 
		
		for(int i = 0 ; i < b.length ; i++){
			paint.setColor(Color.rgb(33, 65, 98));
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAntiAlias(true);
			paint.setTextSize(20 * (int)(displayMetrics != null ? (displayMetrics.density / 1.5) : 1));
			 
			// 选中的状态
			if(i == choose){
				paint.setColor(Color.parseColor("#3399ff"));
				paint.setFakeBoldText(true);
			}
			// x 坐标等于中间 - 字符串宽度的一半
			float xPos = width / 2 - paint.measureText(b[i]) /2 ;
			float yPos = singleHeight * i + singleHeight ;
			canvas.drawText(b[i], xPos, yPos, paint);
			paint.reset();
		}
		
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float y = event.getY();
		final int oldChoose = choose;
		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		final int c = (int)(y / getHeight() * b.length);// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的数.  
		
		switch (action) {
		case MotionEvent.ACTION_UP:
			setBackgroundDrawable(new ColorDrawable(0x00000000));
			choose = -1;
			invalidate();
			if(mTextDialog != null){
				mTextDialog.setVisibility(View.INVISIBLE);
			}
			break;

		default:
			setBackgroundResource(R.drawable.sidebar_background);
			if(oldChoose != c){
				if( c >= 0 && c < b.length){
					if(listener!= null){
						listener.onTouchingLetterChanged(b[c]);
					}
					if(mTextDialog != null){
						mTextDialog.setText(b[c]);
						mTextDialog.setVisibility(View.VISIBLE);
					}
					choose = c ;
					invalidate();
				}
			}
			break;
		}
		return true;
	}
	
	
	 /** 
     * 接口 
     *  
     * @author coder 
     *  
     */  
    public interface OnTouchingLetterChangedListener {  
        public void onTouchingLetterChanged(String s);  
    }  
}
