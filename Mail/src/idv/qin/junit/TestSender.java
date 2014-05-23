package idv.qin.junit;

import java.io.IOException;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Message.Builder;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

public class TestSender {
	public static void main(String[] args) {
		System.out.println("-------------");
		String key = "AIzaSyBV6i0TuyTOCwlkl2t9cXRjC8rwWu5Zngc"; // yijian...@gmail project key
		String regId = "APA91bH-BJM-nygl-5500jTWvX8ZEg7pHxZQoMdddnP18r2w2CRzip2XcCPke2qo5wnAbFG6jHK-Ha_WDVN81E0vHbkcEDZ2mGcwjaesIevZJFthpOXMEo2UEg3xB5kqJt8Ae9VAy3k2FkCsC1y-r3lttBkATZCCJA";
		
		Result result = null;
		try {
			Sender sender = new Sender(key);
			Builder builder = new Message.Builder();
			builder.addData("msg", "test success-ipoca");
			Message message = builder.build();
		
			result = sender.send(message, regId, 5);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Sent message to one device: "+result);
	}

}
