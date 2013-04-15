package com.tarena.fashionmusic;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.tarena.fashionmusic.main.MainActivity;

public class FashionMusicActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences sp = getSharedPreferences("service", 0);
		if (sp.getBoolean("isStart", false)) {
			startActivity(new Intent(this, MainActivity.class));
			finish();
		} else {
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			// …Ë÷√»´∆¡œ‘ æ
			this.getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
			setContentView(R.layout.welcome_logo);

			ImageView iv = (ImageView) findViewById(R.id.logo);
			Animation animation = AnimationUtils.loadAnimation(this,
					R.anim.logo);

			iv.startAnimation(animation);

			animation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					startActivity(new Intent(FashionMusicActivity.this,
							MainActivity.class));
					finish();
				}
			});

		}
	}

}
