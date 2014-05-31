package idv.qin.core;

import android.content.Context;
import android.os.Handler;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class WeatherService {

	private String tempLink = "http://webservice.webxml.com.cn/WebServices/WeatherWS.asmx/getWeather?theCityCode=2255&theUserID=";
	
	private Handler handler;
	private Context context;

	public WeatherService(Handler handler , Context context) {
		super();
		this.handler = handler;
		this.context = context;
	}
	
	public void getCityCode(String cityName){
		
	}
	
	public void getCityWeather(int cityCode){
		RequestQueue queue = Volley.newRequestQueue(context);
		StringRequest stringRequest = new StringRequest(tempLink,  
                new Response.Listener<String>() {  
                    @Override  
                    public void onResponse(String response) {  
                    	System.out.println(response);
                    }  
                }, new Response.ErrorListener() {  
                    @Override  
                    public void onErrorResponse(VolleyError error) {  
                    }  
                });  
		queue.add(stringRequest);  
	}
}
