package idv.qin.mail.fragmet.sendbox;

import idv.qin.domain.PackageItem;
import idv.qin.mail.R;
import idv.qin.mail.fragmet.BaseFragment;
import idv.qin.view.SwipeDismissListView;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SendBoxFragment extends BaseFragment implements View.OnClickListener{

	public static final String SENDBOX_FRAGMENT_TAG = "SendBoxFragment";
	private static final int REQUEST_CODE_SETTINGS = 0;
    private SwipeDismissListView dismissListView;
    private ProgressDialog progressDialog;
    private Button button_ok;
	private Button button_back;
	    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.send_box_fragment, container, false);
		dismissListView = (SwipeDismissListView) rootView.findViewById(R.id.example_lv_list);
		button_ok = (Button) rootView.findViewById(R.id.head_bar_ok);
		button_ok.setOnClickListener(this);
		button_back = (Button) rootView.findViewById(R.id.head_bar_back);
		button_back.setOnClickListener(this);
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
	

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
