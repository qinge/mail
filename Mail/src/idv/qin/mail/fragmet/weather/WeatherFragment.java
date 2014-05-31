package idv.qin.mail.fragmet.weather;

import java.text.SimpleDateFormat;
import java.util.Date;

import idv.qin.core.WeatherService;
import idv.qin.mail.R;
import idv.qin.mail.fragmet.BaseFragment;
import idv.qin.utils.CommonUtil;
import idv.qin.utils.PreferencesManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class WeatherFragment extends BaseFragment implements OnClickListener {

	public static final String WEATHER_FRAGMENT_FLAG = "WeatherFragment";
	public static final String LOCATION = "location";
	private Button button_back;
	private Button button_ok;
	private TextView date_text;
	private TextView temperature_text;
	private TextView location_text;
	private WeatherService service;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}
		
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		service = new WeatherService(handler, mainActivity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.weather_fragment, container, false);
		initComponent();
		return rootView;
	}
	
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		service.getCityWeather(0);
	}

	private void initComponent() {
		button_ok = (Button) rootView.findViewById(R.id.head_bar_ok);
		button_ok.setVisibility(View.INVISIBLE);
		button_back = (Button) rootView.findViewById(R.id.head_bar_back);
		button_back.setOnClickListener(this);
		
		date_text = (TextView) rootView.findViewById(R.id.weather_fragment_date_text);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年-MM月-dd日");
		date_text.setText(dateFormat.format(new Date()));
		
		temperature_text = (TextView) rootView.findViewById(R.id.weather_fragment_temperature_text);
		
		location_text = (TextView) rootView.findViewById(R.id.weather_fragment_location_text);
		String location = PreferencesManager.getInstance(mainActivity).getValue(LOCATION);
		if(CommonUtil.isEmpty(location)){
			location = "guanxi";
		}
		location_text.setText(location);
		
	}


	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_bar_back:
			backPrevPage(R.id.weather_main_area);
			break;

		default:
			break;
		}
	}

	
	
}
