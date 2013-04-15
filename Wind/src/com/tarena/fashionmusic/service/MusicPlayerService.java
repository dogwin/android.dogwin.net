package com.tarena.fashionmusic.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.tarena.fashionmusic.MyApplication;
import com.tarena.fashionmusic.entry.Music;
import com.tarena.fashionmusic.entry.impl.Musicdata;
import com.tarena.fashionmusic.play.notify.MyNotiofation;
import com.tarena.fashionmusic.util.Constant;
import com.tarena.fashionmusic.util.HttpTool;
import com.tarena.fashionmusic.widget.AppWidget;

public class MusicPlayerService extends Service {

	public class PhoneStatRec extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			TelephonyManager mTelManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			boolean isringpause = false;
			switch (mTelManager.getCallState()) {
			case TelephonyManager.CALL_STATE_RINGING:// ����
				if (mPlayer != null && mPlayer.isPlaying()) {
					mPlayer.pause();
					isringpause = true;
				}
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:// ͨ��
				if (mPlayer != null && mPlayer.isPlaying()) {
					mPlayer.pause();
					isringpause = true;
				}
				break;
			case TelephonyManager.CALL_STATE_IDLE:// ͨ������
				if (mPlayer != null && isringpause == true) {
					mPlayer.start();
					isringpause = false;
				}
				break;
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private MyReciever mReceiver;
	private apwReciver apReciver;
	private PhoneStatRec phoneStatRec;
	public static MediaPlayer mPlayer;
	private ArrayList<Music> musicList;
	public static String[] SongNamekeywords;
	private int current;// ��ǰ���ŵĸ����±�
	private int nowcurr = 0;// ��ǰ���Ž���
	private int totalms = 0;// ��ǰ������ʱ��
	private int playmode = 0;// ����ģʽ 0 ˳�򲥷� 1 ������� 2 ����ѭ��
	private int mode_current = 0;
	public static int status = 1;// 1 δ���� 2 ��ͣ 3 ����
	Music nowplaymusic;
	private NotificationManager manager;
	Context context;

	/* ����������ſ��ƵĹ㲥 */
	private class MyReciever extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {

			if (Constant.ACTION_PLAY.equals(intent.getAction())) {
				switch (status) {
				case 1:
					play();
					break;
				case 2:
					mPlayer.start();
					break;
				case 3:
					mPlayer.pause();
					sendBroadcast(new Intent("com.tarena.ispause"));
					status = 2;
					break;
				}
				status = 3;
				sendBroadcast(new Intent("com.tarena.isplay"));
				MyNotiofation.getNotif(MusicPlayerService.this, nowplaymusic,
						manager);
			}
			// ��ͣ
			else if (Constant.ACTION_PAUSE.equals(intent.getAction())) {
				mPlayer.pause();
				status = 2;
				MyNotiofation.getNotif(MusicPlayerService.this, nowplaymusic,
						manager);
				sendBroadcast(new Intent("com.tarena.ispause"));
			}
			// ֹͣ
			else if (Constant.ACTION_STOP.equals(intent.getAction())) {
				mPlayer.stop();
				mPlayer.release();
				MyApplication.musicPreference
						.savaPlayPosition(context, current);
				stopSelf();
			}
			// ��һ��
			else if (Constant.ACTION_PREVIOUS.equals(intent.getAction())) {
				previous();
				status = 3;
			}
			// ��һ��
			else if (Constant.ACTION_NEXT.equals(intent.getAction())) {
				next();
				status = 3;
			}
			// JUMP
			else if (Constant.ACTION_JUMR.equals(intent.getAction())) {
				int position = intent.getIntExtra("position", 0);
				if (position >= 0) {
					jump(position);
				}
				// JUMP_OTHER
			} else if (Constant.ACTION_JUMR_OTHER.equals(intent.getAction())) {
				String name = intent.getStringExtra("name");
				Log.i("test", musicList.size() + "--position" + "---" + name);
				int position = getdataindex(name);
				if (position >= 0) {
					jump(position);
				}
			} else if (Constant.ACTION_FIND.equals(intent.getAction())) {
				String name = intent.getStringExtra("name");
				int position = getindex(name);
				if (position >= 0) {
					jump(position);
				}
			}
			// seek
			else if (Constant.ACTION_SEEK.equals(intent.getAction())) {
				try {
					nowcurr = (intent.getIntExtra("seekcurr", 0)) * totalms
							/ 100;
					mPlayer.seekTo(nowcurr);
					if (status == 2) {
						mPlayer.start();
					}
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (Constant.ACTION_UPDATE_ALL.equals(intent.getAction())) {
				updataAllMusicInfo(false, nowplaymusic);

			} else // ���ò���ģʽ
			if (Constant.ACTION_SET_PLAYMODE.equals(intent.getAction())) {
				int n = intent.getIntExtra("play_mode", -1);
				playmode = n;
				if (n == 2) {
					mode_current = current;
				}
				// �����б����仯
			} else if (Constant.ACTION_LISTCHANGED.equals(intent.getAction())) {
				musicList.clear();
				musicList.addAll(MyApplication.musics);
				initAllsongNames();
			} else if (Constant.ACTION_NET_PLAY.equals(intent.getAction())) {
				Music netMusic = (Music) intent
						.getSerializableExtra("net_music");
				playnetMusic(netMusic);
			}
		}

	}

	/**
	 * ���µ�ǰ���Ÿ�������ϸ��Ϣ
	 * 
	 * @param isnet
	 * @param music
	 */
	private Intent updataintent;

	private void updataAllMusicInfo(boolean isnet, Music music) {
		if (updataintent == null) {
			updataintent = new Intent(Constant.ACTION_UPDATE);
		}
		if (isnet) {
			updataintent.putExtra("status", status);
			updataintent.putExtra("music", music);
			updataintent.putExtra("isnet", true);
		} else {
			updataintent.putExtra("status", status);
			updataintent.putExtra("music", nowplaymusic);
			updataintent.putExtra("position", current);
			updataintent.putExtra("totalms", totalms);
		}
		sendBroadcast(updataintent);
		MyNotiofation.getNotif(MusicPlayerService.this, nowplaymusic, manager);
	}

	SharedPreferences sp;

	@Override
	public void onCreate() {
		super.onCreate();
		sp = getSharedPreferences("service", 0);
		sp.edit().putBoolean("isStart", true).commit();
		// �㲥������
		mReceiver = new MyReciever();
		apReciver = new apwReciver();
		phoneStatRec = new PhoneStatRec();
		updataintent = new Intent(Constant.ACTION_UPDATE);
		context = this;
		mPlayer = MyApplication.mediaPlayer;
		manager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mPlayer.setOnCompletionListener(new OnCompletionListener() {
			/**
			 * ���ֲ�����ɵĴ�����
			 */
			@Override
			public void onCompletion(MediaPlayer mp) {
				next();// ������һ��
			}
		});
		// ��ǰ���ŵ������б�
		musicList = ((MyApplication) getApplication()).getMusics();
		// ��ǰ�������ֵ�����
		current = MyApplication.musicPreference.getsaveposition(this);
	}

	private void initAllsongNames() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				SongNamekeywords = Musicdata.GetAll(musicList);
			}
		}).start();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		initAllsongNames();
		if (mPlayer == null) {
			mPlayer = MyApplication.mediaPlayer;
		}
		musicList = ((MyApplication) getApplication()).getMusics();
		// ��̬ע��㲥
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.ACTION_LISTCHANGED);
		filter.addAction(Constant.ACTION_PLAY);
		filter.addAction(Constant.ACTION_PAUSE);
		filter.addAction(Constant.ACTION_PREVIOUS);
		filter.addAction(Constant.ACTION_NEXT);
		filter.addAction(Constant.ACTION_SEEK);
		filter.addAction(Constant.ACTION_STOP);
		filter.addAction(Constant.ACTION_JUMR);
		filter.addAction(Constant.ACTION_JUMR_OTHER);
		filter.addAction(Constant.ACTION_UPDATE_ALL);
		filter.addAction(Constant.ACTION_FIND);
		filter.addAction(Constant.ACTION_NET_PLAY);
		filter.addAction(Constant.ACTION_SET_PLAYMODE);
		filter.addAction(Constant.ACTION_STAR_THREAD);
		registerReceiver(mReceiver, filter);
		/* ע��appwidget�Ĺ㲥 */
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(AppWidget.PRIVOICE_ACTION);
		intentFilter.addAction(AppWidget.NEXT_ACTION);
		intentFilter.addAction(AppWidget.PLAY_ACTION);
		intentFilter.addAction(AppWidget.START_APP);
		registerReceiver(apReciver, intentFilter);

		IntentFilter mIntentFilter = new IntentFilter();
		mIntentFilter.addAction("android.intent.action.PHONE_STATE");
		registerReceiver(phoneStatRec, mIntentFilter);
		playmode = MyApplication.musicPreference.getPlayMode(this);
	}

	@Override
	public void onDestroy() {
		Log.i("info", sp.getBoolean("isStart", false) + "");
		// ȡ���㲥ע��
		unregisterReceiver(mReceiver);
		unregisterReceiver(phoneStatRec);
		MyApplication.musicPreference.savaPlayPosition(context, current);
		super.onDestroy();
	}

	/**
	 * ���ݸ������ƻ�ȡ��ǰ�����ڲ����б��е�λ��
	 * 
	 * @param name
	 * @return �±�λ��
	 */
	public int getindex(String name) {
		int index = 0;
		for (int i = 0; i < musicList.size(); i++) {
			if (musicList.get(i).getMusicName().equals(name)) {
				index = i;
				break;
			}
		}
		return index;
	}

	/**
	 * ���ݸ���·����ȡ��ǰ�����ڲ����б��е�λ��
	 * 
	 * @param savepath
	 * @return �±�λ��
	 */
	public int getdataindex(String savepath) {
		int index = 0;
		if (musicList.size() > 0) {
			for (int i = 0; i < musicList.size(); i++) {
				if (musicList.get(i).getSavePath() != null
						&& musicList.get(i).getSavePath().equals(savepath)) {
					index = i;
					break;
				}
			}
		}
		return index;
	}

	/**
	 * ������������
	 * 
	 * @param netMusic
	 */
	public void playnetMusic(Music netMusic) {
		try {
			mPlayer.reset();
			mPlayer.setDataSource(HttpTool.URI + netMusic.getMusicPath());
			Log.e("test", netMusic.getMusicPath());
			mPlayer.prepareAsync();
			mPlayer.setOnPreparedListener(new OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					mp.start();
					status = 3;
					sendBroadcast(new Intent(Constant.ACTION_DISS_DIALOG));
				}
			});
			updataAllMusicInfo(true, netMusic);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ���ŵ�ǰ����
	 */
	private void play() {
		if (musicList != null && musicList.size() > 0) {
			Log.i("cpu", "" + playmode + isjump);
			if (playmode == 1) {// ���
				if (!isjump) {
					current = new Random().nextInt(musicList.size());
				}
			} else if (playmode == 2) {// ����
				current = mode_current;
			}
			nowplaymusic = musicList.get(current);
			Log.i("music", current + "��ǰ���ŵĸ���");
			isjump = false;
			try {
				mPlayer.reset();
				mPlayer.setDataSource(nowplaymusic.getSavePath());
				mPlayer.prepare();
				mPlayer.start();
				status = 3;
				totalms = mPlayer.getDuration();
				updataAllMusicInfo(false, null);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ������һ������
	 */
	private void previous() {
		if (musicList != null && musicList.size() > 0) {
			if (current == 0) {
				current = musicList.size() - 1;
			} else {
				current--;
			}
			play();
		}
	}

	/**
	 * ������һ������
	 */
	private void next() {
		if (musicList != null && musicList.size() > 0) {
			if (current == musicList.size() - 1) {
				current = 0;
			} else {
				current++;
			}
			play();
		}
	}

	/**
	 * ���ŵ����ĳһλ�ø���
	 * 
	 * @param position
	 */
	boolean isjump = false;

	private void jump(int position) {
		Log.i("test", musicList.size() + "--position" + position);
		if (musicList != null && musicList.size() > 0) {
			current = position;
			isjump = true;
			mode_current = current;
			play();
		}
	}

	public class apwReciver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i("action", intent.getAction() + "��ǰ״̬" + status);
			if (intent.getAction().equals(AppWidget.PLAY_ACTION)) {
				if (status == 2) {// ��ͣ
					mPlayer.start();
					if (status == 0) {
						play();
					}
					status = 3;
					sendBroadcast(new Intent(AppWidget.IS_PLAY_ACTION));
				} else if (status == 3) {// ����
					mPlayer.pause();
					status = 2;
					sendBroadcast(new Intent(AppWidget.IS_PAUSE_ACTION));
				}
			} else if (intent.getAction().equals(AppWidget.NEXT_ACTION)) {
				next();
			} else if (intent.getAction().equals(AppWidget.PRIVOICE_ACTION)) {
				previous();
			} else if (intent.getAction().equals(AppWidget.START_APP)) {
				Intent intent2 = new Intent(AppWidget.UPDATE_APP);
				intent2.putExtra("music", musicList.get(current));
				intent2.putExtra("status", status);
				sendBroadcast(intent2);
			}

		}

	}
}
