package com.tarena.fashionmusic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.util.Log;

import com.tarena.fashionmusic.entry.Music;
import com.tarena.fashionmusic.entry.impl.Musicdata;
import com.tarena.fashionmusic.util.MusicPreference;

public class MyApplication extends Application {
	public static MediaPlayer mediaPlayer;
	public static MusicPreference musicPreference;
	public static ArrayList<Music> musics = new ArrayList<Music>();
	public static boolean isStart = false;
	public List<Activity> activityList = new LinkedList<Activity>();
	public static Bitmap bitmap_l;
	public static Bitmap bitmap_s;
	public static MyApplication instance;

	/**
	 * ��ȡmusic����
	 * 
	 * @return
	 */
	public ArrayList<Music> getMusics() {
		return musics;
	}

	public static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
		new Thread(new Runnable() {
			@Override
			public void run() {
				setMusics(Musicdata.getMultiDatas(context));
				bitmap_l = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.default_bg_l);
				bitmap_s = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.default_bg_s);
			}
		}).start();
		mediaPlayer = new MediaPlayer();
		musicPreference = new MusicPreference(context);
	}

	/**
	 * ����music����
	 * 
	 * @param musics
	 */
	public void setMusics(ArrayList<Music> ms) {
		musics.clear();
		musics = ms;
		Log.i("test", "�б���" + this.musics.size());
	}

	public MyApplication() {
	}

	// ����ģʽ�л�ȡΨһ��MyApplicationʵ��
	public static MyApplication getInstance() {
		if (null == instance) {
			instance = new MyApplication();
		}
		return instance;
	}

	// ���Activity��������
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// ��������Activity��finish
	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
		System.exit(0);
	}

	/**
	 * ��musics������׷��һ��miusic��Ϣ
	 * 
	 * @param musics
	 */
	public void append(ArrayList<Music> musics) {
		if (musics != null) {
			this.musics.addAll(musics);
		}
	}

	/**
	 * ��musics������׷��1��music��Ϣ
	 * 
	 * @param music
	 */
	public void append(Music music) {
		if (music != null) {
			this.musics.add(music);
		}
	}
}
