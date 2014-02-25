package idv.qin.utils;

import idv.qin.mail.R;
import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class OutAnimationUtil {

	private static Animation animation ;
	private OutAnimationUtil(){}
	
	// when system start  load this object 
	public static void loadAnimation(Context context){
		animation = AnimationUtils.loadAnimation(context, R.anim.out_animation);
		
	}
	public static Animation getOutAnimation(){
		return animation;
	}
	
}
