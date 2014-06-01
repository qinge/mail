package idv.qin.mail;

import idv.qin.domain.MailMessageBean;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.ZoomDensity;

public class MessageActivity extends Activity {
	private FragmentManager fragmentManager = null;
	private String tag = "MessageActivity";
	private MailMessageBean bean;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.message_activity_layout);
		fragmentManager = getFragmentManager();
		bean = (MailMessageBean) getIntent().getExtras().getSerializable("MailMessageBean");
		
		if(fragmentManager.findFragmentByTag(tag) == null){
			swipFragment();
		}
	}
	

	private void swipFragment() {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Bundle bundle = new Bundle();
		bundle.putSerializable("MailMessageBean", bean);
		Fragment fragment = new GenericMessageFragment();
		fragment.setArguments(bundle);
		transaction.replace(R.id.message_main_continer, fragment);
		transaction.commit();
	}

	
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		/*if(keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()){
			webView.goBack();
			return true;
		}*/
		return super.onKeyDown(keyCode, event);
	}




	public final static class GenericMessageFragment extends Fragment {

		public static final String GENERIC_MESSAGE_FRAGMENT = "GenericMessageFragment";
		private View rootView;
		private WebView webView;
		private MailMessageBean fragmentBean;
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
	/*		gestureListener = new MyOnGestureListener();
			detector = new GestureDetector(mainActivity, gestureListener);*/
			fragmentBean = (MailMessageBean) getArguments().getSerializable("MailMessageBean");
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
			settings.setJavaScriptEnabled(true);
			settings.setUseWideViewPort(true); 
			settings.setLoadWithOverviewMode(true); 
			// 设置是否可缩放
			settings.setBuiltInZoomControls(true);
			settings.setDisplayZoomControls(false);
			//  默认编码
			settings.setDefaultTextEncodingName("utf-8");
			webView.setBackgroundColor(0);
			webView.setWebViewClient(new WebViewClient());
			webView.loadDataWithBaseURL(null, fragmentBean.mailContent.content, "text/html",
                    "utf-8", null);
		}
		
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
		
	}
}
