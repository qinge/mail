package idv.qin.view;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.nostra13.universalimageloader.utils.StorageUtils;

import idv.qin.mail.MainActivity;
import idv.qin.mail.R;
import idv.qin.mail.fragmet.BaseFragment;
import idv.qin.utils.MyLog;
import idv.qin.utils.SerializeBitmap;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class AttachFragment extends BaseFragment implements View.OnClickListener{
	public static final String ATTACH_FRAGMENT_FLAG = "AttachFragment";
	private static final int PICK_CAMERA_SUCCESS =  0x0001;
	private static final int PICK_PICTURE_SUCCESS = 0x0002;
	private static final int PICK_FILE_SUCCESS = 0x0003;
	
	private ExtraedListener listener;
	private Button camera_button;
	private Button picture_button;
	private Button file_button;
	private Uri imageUri; // 调用摄像头时候拍照图片保存路径
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainActivity = (MainActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		currentView = inflater.inflate(R.layout.add_attach_dialog, container, false);
		
		initComponent(currentView);
		
		processTouchEvent(currentView);
		
		return currentView;
	}
	

	public void setOnExtraedListener(ExtraedListener listener) {
		this.listener = listener;
	}

	private void initComponent(View currentView) {
		if(currentView == null){
			return ;
		}
		camera_button = (Button) currentView.findViewById(R.id.compose_add_attach_dialog_camera_tv);
		if(camera_button != null)
			camera_button.setOnClickListener(this);
		picture_button = (Button) currentView.findViewById(R.id.compose_add_attach_dialog_album_tv);
		
		if(picture_button != null){
			picture_button.setOnClickListener(this);
		}
		
		file_button = (Button) currentView.findViewById(R.id.compose_add_attach_dialog_file_tv);
		if(file_button != null){
			file_button.setOnClickListener(this);
		}
	}

	private void processTouchEvent(View currentView) {
		currentView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				dismissSelf();
				return true;
			}
		});
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.compose_add_attach_dialog_camera_tv:
			intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
			imageUri = Uri.fromFile(createCameraFileSavePath());
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			startActivityForResult(intent, PICK_CAMERA_SUCCESS);
			break;
		case R.id.compose_add_attach_dialog_album_tv:
			intent.setAction(Intent.ACTION_PICK);
			intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent, PICK_PICTURE_SUCCESS);
			break;
		case R.id.compose_add_attach_dialog_file_tv:
			intent.setAction(Intent.ACTION_GET_CONTENT);
			intent.setType("application/*");
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			String title = mainActivity.getResources().getString(R.string.select_file);
			startActivityForResult(Intent.createChooser(intent, title), PICK_FILE_SUCCESS);
			break;

		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		String path = null ;
		 if (resultCode == Activity.RESULT_OK ) {
			 switch (requestCode) {
				case PICK_CAMERA_SUCCESS :
					path = imageUri.getPath();
					break;
				case PICK_PICTURE_SUCCESS :
					path = getSelectedImagePath(data);
					break;
				case PICK_FILE_SUCCESS :
					path = getSelectedFilePath(data);
					if(!path.contains(".")){
						path = getSelectedImagePath(data);
					}
					break;
				}
			 listener.onExtraed(path);
		 }
		 dismissSelf();
	}

	
	/**
	 * return file path eg : mnt/sdcard/...
	 * @param data
	 * @return
	 */
	private String getSelectedImagePath(Intent data) {
		Uri selectedImage = data.getData();
		 String[] filePathColumn = { MediaStore.Images.Media.DATA };
		 Cursor cursor = mainActivity.getContentResolver().query(selectedImage,filePathColumn, null, null, null);
		 data.getData().getPath();
		 String picturePath = null;
		 if(cursor != null && cursor.moveToFirst()){
			 int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			 picturePath = cursor.getString(columnIndex);
			 cursor.close();
		 }
		 return picturePath;
	}
	
	private String getSelectedFilePath(Intent data) {
		if(data == null){
			return "";
		}else {
			return data.getData().getPath();
		}
	}
	
	
	private File createCameraFileSavePath(){
		File fileDir = StorageUtils.getCacheDirectory(mainActivity);
		File imageCacheDir = new File(fileDir, "cache/image");
		if(!imageCacheDir.exists() ){
			imageCacheDir.mkdirs();
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss"); // 年月日时分秒
		String fileName = format.format(new Date())+".jpg";
		File file = new File(imageCacheDir, fileName);
		return file;
	}
	
	private void dismissSelf() {
		mainActivity.getFragmentManager().popBackStack(ATTACH_FRAGMENT_FLAG ,
				FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}
	
	public interface ExtraedListener{
		void onExtraed(String path);
	}
	
}
