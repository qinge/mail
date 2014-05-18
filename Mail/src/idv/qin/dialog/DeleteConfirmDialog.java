package idv.qin.dialog;

import idv.qin.mail.R;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

/**
 * 删除数据时候确认对话框
 * @author qinge
 *
 */
public class DeleteConfirmDialog extends Dialog implements android.view.View.OnClickListener{

	private LayoutInflater inflater;
	private SelectedListener selectedListener;
	private Button confirmButton;
	private Button cancelButton;
	
	
	public DeleteConfirmDialog(Context context) {
		super(context);
		init(context);
	}


	public DeleteConfirmDialog(Context context, int theme) {
		super(context, theme);
		init(context);
	}
	
	private void init(Context context) {
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(inflater != null){
			View view = inflater.inflate(R.layout.delete_confirm_dialog_layout, null);
		}
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
	}
	
	
	
	
	

	public void setSelectedListener(SelectedListener selectedListener) {
		this.selectedListener = selectedListener;
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	

	public static interface SelectedListener{
		void onOkSelected();
		void onCancelSelected();
	}

}
