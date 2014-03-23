package idv.qin.view;

import idv.qin.mail.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;

public class CustomDialog {
	
	private Context context;
	
	private CustomDialogClickListener clickListener;

	/**
	 * 构造函数给对象赋值 两个都不能为 null
	 * @param context
	 * @param clickListener {@link CustomDialogClickListener} 对话框ok 或者 cancel 按钮点击监听类
	 */
	public CustomDialog(Context context, CustomDialogClickListener clickListener) {
		this.context = context;
		this.clickListener = clickListener;
	}
	
	/**
	 * 自定义对话框 并指定 view  如果 layoutId == 0 使用默认自定义view.
	 * @param positiveTitle  按钮显示文字 为 nul 默认显示 ok
	 * @param negativeTitle  按钮显示文字  为 nul 默认显示 cancel
	 * @param layoutId  自定义view 布局 id 如果为 0 使用默认自定义 view
	 */
	public void showDialog(String positiveTitle, String negativeTitle, int layoutId){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
//		builder.setTitle("title");
		builder.setCancelable(false);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dialogView = inflater.inflate(layoutId != 0 ? layoutId : R.layout.dialog_layout,null);
		builder.setView(dialogView);
		builder.setPositiveButton(positiveTitle !=null && !"".equals(positiveTitle) ? positiveTitle : "Ok", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(clickListener != null){
					clickListener.onClickOk();
				}
				dialog.dismiss();
			}
		});
		builder.setNegativeButton(negativeTitle !=null && !"".equals(negativeTitle) ? negativeTitle : "cancle", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(clickListener != null){
					clickListener.onClickCancel();
				}
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	
	
	/**
	 * 自定义对话框点击监听接口
	 * @author qin
	 *
	 */
	public static interface CustomDialogClickListener{
		/**
		 * 点击 OK 按钮
		 */
		void onClickOk();
		
		/**
		 * 点击取消按钮
		 */
		void onClickCancel();
	}
}
