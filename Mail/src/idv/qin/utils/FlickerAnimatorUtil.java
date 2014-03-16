package idv.qin.utils;

import android.view.View;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * 图标点击闪动工具类
 * @author qinge
 *
 */
public class FlickerAnimatorUtil {
	private static final int ANIMATOR_DURATION = 500;

	private FlickerAnimatorUtil(){}
	
	/**
	 * 闪动 view 
	 * @param view
	 * @param flickerAnimatorStateListener 可为 null 
	 */
	public static void flickerView(final View view, final FlickerAnimatorStateListener flickerAnimatorStateListener){
		if(view == null){
			return ;
		}
		
		ObjectAnimator object_anim_scale_x = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 1.6f, 1.0f);
		ObjectAnimator object_anim_scale_y = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 1.6f, 1.0f);
		ObjectAnimator object_anim_alpha = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.2f, 1.0f);
		
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.play(object_anim_scale_x).with(object_anim_scale_y).with(object_anim_alpha);
		animatorSet.setDuration(ANIMATOR_DURATION);
		animatorSet.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator arg0) {
				view.setEnabled(false);
				if(flickerAnimatorStateListener != null){
					flickerAnimatorStateListener.flickerAnimatorStart();
				}
				
			}
			
			@Override
			public void onAnimationRepeat(Animator arg0) {
				if(flickerAnimatorStateListener != null){
					flickerAnimatorStateListener.flickerAnimatorRepeat();
				}
			}
			
			@Override
			public void onAnimationEnd(Animator arg0) {
				view.setEnabled(true);
				if(flickerAnimatorStateListener != null){
					flickerAnimatorStateListener.flickerAnimatorEnd();
				}
				
			}
			
			@Override
			public void onAnimationCancel(Animator arg0) {
				view.setEnabled(true);
				if(flickerAnimatorStateListener != null){
					flickerAnimatorStateListener.flickerAnimatorCancel();
				}
			}
		});
		animatorSet.start();
	}
	
	public static interface FlickerAnimatorStateListener{
		void flickerAnimatorStart();
		void flickerAnimatorRepeat();
		void flickerAnimatorCancel();
		void flickerAnimatorEnd();
	}
	
	/**
	 * 闪动监听到适配器类 
	 * @author qinge
	 *
	 */
	public static class FlickerAnimatorStateAdapter implements FlickerAnimatorStateListener{

		@Override
		public void flickerAnimatorStart() {}

		@Override
		public void flickerAnimatorRepeat() {}

		@Override
		public void flickerAnimatorCancel() {}

		@Override
		public void flickerAnimatorEnd() {}
		
	}
}
