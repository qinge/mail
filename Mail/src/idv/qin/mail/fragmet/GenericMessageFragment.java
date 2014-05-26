package idv.qin.mail.fragmet;

import idv.qin.mail.R;
import idv.qin.utils.MyBuildConfig;
import idv.qin.utils.MyLog;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ScrollView;

public class GenericMessageFragment extends BaseFragment {

	public static final String GENERIC_MESSAGE_FRAGMENT = "GenericMessageFragment";
	private WebView webView;
	private OnGestureListener gestureListener;
	private GestureDetector detector;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
/*		gestureListener = new MyOnGestureListener();
		detector = new GestureDetector(mainActivity, gestureListener);*/
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.generic_message_fragment_layout, container, false);
		initComponent();
		return rootView;
	}

	private void initComponent() {
		webView = (WebView) rootView.findViewById(R.id.generic_message_webview);
		WebSettings settings = webView.getSettings();
		
		settings.setUseWideViewPort(true); 
		settings.setLoadWithOverviewMode(true); 
		// 设置是否可缩放
		settings.setBuiltInZoomControls(true);
		settings.setDisplayZoomControls(false);
		//  默认编码
		settings.setDefaultTextEncodingName("utf-8");

		webView.setBackgroundColor(0);
		webView.setWebViewClient(new WebViewClient());
		webView.loadUrl("http://www.baidu.com");
//		webView.loadUrl("http://blog.csdn.net/yijianhantian/article/details/9397767");
/*		webView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return detector.onTouchEvent(event);
			}
		});
*/	}
	
	

	/*@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		rootView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return detector.onTouchEvent(event);
			}
		});
	}*/

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	private final class MyWebViewClient extends WebViewClient{

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
		}

		@Override  
	    public boolean shouldOverrideUrlLoading(WebView  view, String  url)  
	    {  
			view.loadUrl(url);
	        return true;  
	    }  
		
		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				SslError error) {
			handler.proceed();
			super.onReceivedSslError(view, handler, error);
		}

		@Override
		public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
			if(event.getAction() == KeyEvent.KEYCODE_BACK && view.canGoBack()){
				view.goBack();
				return true;
			}
			return super.shouldOverrideKeyEvent(view, event);
		}
		
	}
	
	private final class  MyOnGestureListener extends SimpleOnGestureListener {

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			if(MyBuildConfig.DEBUG){
				MyLog.e(TAG, "e1.getX()="+e1.getX());
				MyLog.e(TAG, "e2.getX()="+e2.getX());
				MyLog.e(TAG, "distanceX = ="+distanceX);
				MyLog.e(TAG, "distanceY="+distanceY);
			}
			if(distanceX > 10){
				return true;
			}
			if(distanceY > 150){
				return true;
			}
//			if(Math.abs(e1.getX() - e2.getX()) > 10){
//				scrollView.setEnabled(false);
//				return true;
//			}
//			if(Math.abs(e1.getY() - e2.getY()) > 150){
//				scrollView.setEnabled(true);
//				return true;
//			}
			return super.onScroll(e1, e2, distanceX, distanceY);
		}
		
	}

}
