package wei.ye.mplayer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;

public class YcMusicPlayerActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		new Handler().postDelayed(new Runnable() {

			public void run() {
				offLayout();
				Intent first = new Intent(getBaseContext(), YcMusicPlay.class);
				startActivity(first);
				finish();
			}
		}, 1500);
	}

	// 播放自定义动画
	public void offLayout() {
		ImageView startView = (ImageView) findViewById(R.id.startView);
		startView.startAnimation(new TvOffAnimation());
	}
	/**
	 * 老电视关闭动画效果
	 * @author 叶超
	 *
	 */
	public class TvOffAnimation extends Animation {
		private int halfWidth;
		private int halfHeight;
		@Override
		protected void applyTransformation(float interpolatedTime, Transformation t) {
			super.applyTransformation(interpolatedTime, t);
			final Matrix matrix = t.getMatrix();
			if (interpolatedTime < 0.8) {
				matrix.preScale(1+0.625f*interpolatedTime, 1-interpolatedTime/0.8f+0.01f,halfWidth,halfHeight);
			}else{
				matrix.preScale(7.5f*(1-interpolatedTime),0.01f,halfWidth,halfHeight);
			}
		}
		@Override
		public void initialize(int width, int height, int parentWidth,
				int parentHeight) {
			super.initialize(width, height, parentWidth, parentHeight);
			setDuration(1000);
			setFillAfter(true);
			//保存View的中心点
			halfWidth = width / 2;
			halfHeight = height / 2;
			setInterpolator(new AccelerateDecelerateInterpolator());
		}
		
	}
}