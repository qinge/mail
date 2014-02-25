package idv.qin.view;

import idv.qin.mail.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class CustomExtraView extends LinearLayout {

	public CustomExtraView(Context context) {
		super(context);
		this.addView(LayoutInflater.from(getContext()).inflate(R.layout.write_mail_fragment_item,
				null));
	}

}
