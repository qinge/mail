package idv.qin.mail.fragmet;

import idv.qin.adapter.PackageAdapter;
import idv.qin.domain.PackageItem;
import idv.qin.mail.MainActivity;
import idv.qin.mail.R;
import idv.qin.utils.OutAnimationUtil;
import idv.qin.utils.SettingsManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.Toast;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;

public class SendBoxFragment extends BaseFragment implements View.OnClickListener{

	public static final String SENDBOX_FRAGMENT_TAG = "SendBoxFragment";
	private static final int REQUEST_CODE_SETTINGS = 0;
    private PackageAdapter adapter;
    private List<PackageItem> data;
    private SwipeListView swipeListView;
    private ProgressDialog progressDialog;
    private Button button_ok;
	private Button button_back;
	    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainActivity = (MainActivity) getActivity();
		 data = new ArrayList<PackageItem>();
	    adapter = new PackageAdapter(mainActivity, data);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		currentView = inflater.inflate(R.layout.send_box_fragment, container, false);
		swipeListView = (SwipeListView) currentView.findViewById(R.id.example_lv_list);
		button_ok = (Button) currentView.findViewById(R.id.head_bar_ok);
		button_ok.setOnClickListener(this);
		button_back = (Button) currentView.findViewById(R.id.head_bar_back);
		button_back.setOnClickListener(this);
		return currentView;
	}

	@Override
	public void onResume() {
		super.onResume();
		 swipeListView.setSwipeListViewListener(new BaseSwipeListViewListener() {
	            @Override
	            public void onOpened(int position, boolean toRight) {
	            }

	            @Override
	            public void onClosed(int position, boolean fromRight) {
	            }

	            @Override
	            public void onListChanged() {
	            }

	            @Override
	            public void onMove(int position, float x) {
	            }

	            @Override
	            public void onStartOpen(int position, int action, boolean right) {
	                Log.d("swipe", String.format("onStartOpen %d - action %d", position, action));
	            }

	            @Override
	            public void onStartClose(int position, boolean right) {
	                Log.d("swipe", String.format("onStartClose %d", position));
	            }

	            @Override
	            public void onClickFrontView(int position) {
	                Log.d("swipe", String.format("onClickFrontView %d", position));
	                PackageItem item = (PackageItem) swipeListView.getItemAtPosition(position);
	        		Toast.makeText(mainActivity, item.getName(), 0).show();
	            }

	            @Override
	            public void onClickBackView(int position) {
	                Log.d("swipe", String.format("onClickBackView %d", position));
	            }

	            @Override
	            public void onDismiss(int[] reverseSortedPositions) {
	                for (int position : reverseSortedPositions) {
	                    data.remove(position);
	                }
	                adapter.notifyDataSetChanged();
	            }

	        });

	        swipeListView.setAdapter(adapter);

	        reload();

	        new ListAppTask().execute();

	        progressDialog = new ProgressDialog(mainActivity);
	        progressDialog.setMessage("loading");
	        progressDialog.setCancelable(false);
	        progressDialog.show();

	    }

	    private void reload() {
	        SettingsManager settings = SettingsManager.getInstance();
	        swipeListView.setSwipeMode(settings.getSwipeMode());
	        swipeListView.setSwipeActionLeft(settings.getSwipeActionLeft());
	        swipeListView.setSwipeActionRight(settings.getSwipeActionRight());
	        swipeListView.setOffsetLeft(convertDpToPixel(settings.getSwipeOffsetLeft()));
	        swipeListView.setOffsetRight(convertDpToPixel(settings.getSwipeOffsetRight()));
	        swipeListView.setAnimationTime(settings.getSwipeAnimationTime());
	        swipeListView.setSwipeOpenOnLongPress(settings.isSwipeOpenOnLongPress());
	    }

	    public int convertDpToPixel(float dp) {
	        DisplayMetrics metrics = getResources().getDisplayMetrics();
	        float px = dp * (metrics.densityDpi / 160f);
	        return (int) px;
	    }


	    @Override
	    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);
	        switch (requestCode) {
	            case REQUEST_CODE_SETTINGS:
	                reload();
	        }
	    }

	    public class ListAppTask extends AsyncTask<Void, Void, List<PackageItem>> {

	        protected List<PackageItem> doInBackground(Void... args) {
	            PackageManager appInfo = mainActivity.getPackageManager();
	            List<ApplicationInfo> listInfo = appInfo.getInstalledApplications(0);
	            Collections.sort(listInfo, new ApplicationInfo.DisplayNameComparator(appInfo));

	            List<PackageItem> data = new ArrayList<PackageItem>();

	            for (int index = 0; index < listInfo.size(); index++) {
	                try {
	                    ApplicationInfo content = listInfo.get(index);
	                    if ((content.flags != ApplicationInfo.FLAG_SYSTEM) && content.enabled) {
	                        if (content.icon != 0) {
	                            PackageItem item = new PackageItem();
	                            item.setName(mainActivity.getPackageManager().getApplicationLabel(content).toString());
	                            item.setPackageName(content.packageName);
	                            item.setIcon(mainActivity.getPackageManager().getDrawable(content.packageName, content.icon, content));
	                            data.add(item);
	                        }
	                    }
	                } catch (Exception e) {

	                }
	            }

	            return data;
	        }

	        protected void onPostExecute(List<PackageItem> result) {
	            data.clear();
	            data.addAll(result);
	            adapter.notifyDataSetChanged();
	            if (progressDialog != null) {
	                progressDialog.dismiss();
	                progressDialog = null;
	            }
	        }
	    }

	
	
	
	
	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_bar_ok:
			
			break;
		case R.id.head_bar_back:
			backPrevPage(R.id.send_box_main_area);
			break;
		}
		
	}

	public void backPrevPage(){
		backPrevPage(R.id.send_box_main_area);
	}
}
