package idv.qin.mail.fragmet.inbox;

import java.util.List;

import idv.qin.core.ReceiveMailService;
import idv.qin.domain.MailMessageBean;
import idv.qin.domain.MailMessageBean.MailHeadBean;
import idv.qin.mail.MainActivity;
import idv.qin.mail.R;
import idv.qin.mail.fragmet.BaseFragment;
import idv.qin.refresh.PullToRefreshBase.OnRefreshListener;
import idv.qin.utils.CustomHandler;
import idv.qin.utils.OutAnimationUtil;
import idv.qin.view.PullToRefreshListView;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class InboxFragment extends BaseFragment implements View.OnClickListener{
	
	public static final String INBOX_FRAGMENT_TAG = "InboxFragment";
	private ListView listView;
	private PullToRefreshListView mPullRefreshListView;
	private Button buttonOk;
	private Button buttonBack;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainActivity = (MainActivity) getActivity();
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		currentView = inflater.inflate(R.layout.inbox_fragment, container, false);
		initComponent();
		
		// Set a listener to be invoked when the list should be refreshed.
		processRefreshAction();
		
		return currentView;
	}
	
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ReceiveMailService service = new ReceiveMailService(mainActivity, new ReceiverHandler());
		try {
			service.getHeadMessage(0, 10);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		 //listView.setAdapter(adapter);
	}
	
	private void initComponent() {
		mPullRefreshListView = (PullToRefreshListView)currentView.findViewById(R.id.pull_refresh_list);
		buttonOk = (Button) currentView.findViewById(R.id.head_bar_ok);
		buttonOk.setOnClickListener(this);
		buttonBack = (Button) currentView.findViewById(R.id.head_bar_back);
		buttonBack.setOnClickListener(this);
	}
	
	private void processRefreshAction() {
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// Do work to refresh the list here.
				new GetDataTask().execute();
			}

			@Override
			public void onLoading() {}
		});
	}


	

	
	private class GetDataTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// Call onRefreshComplete when the list has been refreshed.
			mPullRefreshListView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_bar_ok:
			
			break;
		case R.id.head_bar_back:
			backPrevPage(R.id.inbox_main_area);
			break;
		}
		
	}

	public void backPrevPage(){
		backPrevPage(R.id.inbox_main_area);
	}
	

	private class ReceiverHandler extends CustomHandler{

		@Override
		public void success(Message msg) {
			if(msg == null){
				return ;
			}
			List<MailMessageBean> messageBeans = (List<MailMessageBean>) msg.obj;
			if(messageBeans == null || messageBeans.size() <= 0){
				return ;
			}
			Toast.makeText(mainActivity, messageBeans.get(0).mailHead.uid, Toast.LENGTH_SHORT).show();
		
			
		}

		@Override
		public void fail(Message msg) {
			
		}

	}
}
