package idv.qin.mail.fragmet.rubbish;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import idv.qin.domain.MailMessageBean;
import idv.qin.mail.R;
import idv.qin.mail.fragmet.BaseFragment;
import idv.qin.view.SwipeDismissListView;
import idv.qin.view.SwipeDismissListView.OnDismissCallback;

/**
 * 删除邮件暂存目录
 * @author qinge
 *
 */
public class RubblishFragment extends BaseFragment implements OnDismissCallback {

	private List<MailMessageBean> beans;
	private SwipeDismissListView dismissListView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.rubblish_fragment_layout, container, false);
		initComponent();	
		return rootView;
	}

	private void initComponent() {
		dismissListView = (SwipeDismissListView) rootView.findViewById(R.id.rubblish_fragment_swip_dismiss_listview);
		dismissListView.setOnDismissCallback(this);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	/**
	 * 滑动删除回调方法
	 */
	@Override
	public void onDismiss(int dismissPosition) {
		// TODO Auto-generated method stub
		
	}
	

}
