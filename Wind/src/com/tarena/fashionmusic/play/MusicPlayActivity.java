package com.tarena.fashionmusic.play;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.AsyncQueryHandler;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.tarena.fashionmusic.MyApplication;
import com.tarena.fashionmusic.R;
import com.tarena.fashionmusic.entry.BaiduMusic;
import com.tarena.fashionmusic.entry.Music;
import com.tarena.fashionmusic.lrc.BaiduLrc;
import com.tarena.fashionmusic.lrc.Lyric;
import com.tarena.fashionmusic.lrc.LyricView;
import com.tarena.fashionmusic.lrc.PlayListItems;
import com.tarena.fashionmusic.lrc.xml.LRCXmlParser;
import com.tarena.fashionmusic.main.MainActivity;
import com.tarena.fashionmusic.main.adapter.NowPlayListAdapter;
import com.tarena.fashionmusic.play.popmeu.lrcsetting;
import com.tarena.fashionmusic.service.MusicPlayerService;
import com.tarena.fashionmusic.ui.KeywordsFlow;
import com.tarena.fashionmusic.util.BitmapTool;
import com.tarena.fashionmusic.util.Constant;
import com.tarena.fashionmusic.util.HttpTool;
import com.tarena.fashionmusic.util.MusicPreference;
import com.tarena.fashionmusic.util.Rotate3dAnimation;
import com.tarena.fashionmusic.util.SavelrcTool;
import com.tarena.fashionmusic.util.StrTime;

public class MusicPlayActivity extends Activity implements OnClickListener {

	ImageView chose_albumlist;
	private ViewGroup mContainer;
	ViewPager viewPager;
	RelativeLayout contain_viewpage;
	ListView album_list;
	private SeekBar seekBar;
	private List<View> mListViews;
	private ImageView ivnowpage, ivshow_album;
	private ImageButton btprevious, btplay, btnext, bt_playmode, bt_down;
	private TextView tvsongname, tvsinger, tvcurrent, tvdurction, tvcurrlrc,
			tv_nolrc;
	private AwesomePagerAdapter pagerAdapter;
	private MusicinfoRec MusicinfoRec;
	Bitmap nowbitmap;
	private KeywordsFlow keywordsFlow;
	private LyricView lyricView;
	public static Lyric mLyric;
	public static final String TTpath = "mnt/sdcard/TMusic/";
	int progress = 0;
	public static Intent intent;
	public Context context;
	int position, nowplaymode;// 当前播放歌曲下标 播放模式
	boolean ishavelrc = false;
	int totalms = 1;
	Music music;
	LayoutInflater inflater;
	MusicPreference musicPreference;
	AudioManager mAudioManager;
	int MAX_WORLDS = 10;

	Timer timer = new Timer();
	TimerTask task = new TimerTask() {
		public void run() {
			sendBroadcast(new Intent(Constant.ACTION_STOP));
			MyApplication.getInstance().exit();
		}
	};

	private class MusicinfoRec extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 更新时间进度
			// if (intent.getAction().equals(Constant.ACTION_UPDATE_TIME)) {
			// int curms = intent.getIntExtra("musiccurrent", 0);
			// int totalms = intent.getIntExtra("totalms", 1);// 总时长
			// tvcurrent.setText(StrTime.gettim(curms));
			// try {
			// // 将当前的时间 转换成ProgressBar进度值
			// int progress = curms * 100 / totalms;
			// // 设置当前进度
			// seekBar.setProgress(progress);
			// } catch (ArithmeticException e) {
			// e.printStackTrace();
			// }
			// // 更新 歌曲信息
			// } else
			if (intent.getAction().equals(Constant.ACTION_UPDATE)) {
				position = intent.getIntExtra("position", 0);
				music = (Music) intent.getSerializableExtra("music");
				totalms = intent.getIntExtra("totalms", 288888);// 总时长
				Log.i("SMZ", totalms + "");
				try {
					tvsongname.setText(music.getMusicName());
					tvsinger.setText(music.getSinger());
					tvdurction.setText(StrTime.getTime(music.getTime()));
					ShowSongalbum(context);
					if (MyApplication.mediaPlayer.isPlaying()) {
						btplay.setImageResource(R.drawable.desktop_pausebt_b);
						isplaying = true;
					} else {
						isplaying = false;
						btplay.setImageResource(R.drawable.desktop_playbt_b);
					}
					((NowPlayListAdapter) album_list.getAdapter())
							.showNowPlayPos(position);
					ishavelrc = false;
					ShowLyric(TTpath + music.getMusicName() + "-"
							+ music.getSinger() + ".lrc");// 显示歌词
					Log.i("music", " 开始更新 下载歌词");
					tvcurrlrc.setText("");
					if (album_list.isFocused()) {
						album_list.setSelection(position);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (Constant.ACTION_UPDATE_LRC.equals(intent.getAction())) {
				String currLrc = intent.getStringExtra("lrccurr");
				if (currLrc != null && tvcurrlrc != null && !currLrc.equals(""))
					if (ishavelrc) {
						tvcurrlrc.setText(currLrc);
					} else
						tvcurrlrc.setText("");
			}
		}
	}

	/**
	 * 显示 专辑图片!
	 * 
	 * @param context
	 * @param intent
	 */
	Animation animation;

	private void ShowSongalbum(Context context) {

		String albumkey = music.getAlbumkey();
		if (albumkey != null && !"".equals(albumkey)) {
			nowbitmap = BitmapTool.getbitBmBykey(context, music.getAlbumkey());
		} else {
			nowbitmap = MyApplication.bitmap_l;
		}
		if (nowbitmap != null && nowbitmap.isRecycled() == false) {
			ivshow_album.setImageBitmap(nowbitmap);
		} else {
			ivshow_album.setImageBitmap(MyApplication.bitmap_l);
		}

		ivshow_album.startAnimation(animation);
	}

	private void feedKeywordsFlow(KeywordsFlow keywordsFlow, String[] arr) {
		Random random = new Random();
		for (int i = 0; i < MAX_WORLDS; i++) {
			int ran = random.nextInt(arr.length);
			String tmp = arr[ran];
			keywordsFlow.feedKeyword(tmp);
		}
	}

	Handler nameshandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case 1:
				final BaiduMusic b = (BaiduMusic) msg.obj;
				Log.i("music", "解析完毕---------" + b.getLrcid());
				if (b.getLrcid() != null) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								SavelrcTool.save(HttpTool.getStream(
										BaiduLrc.getLrcPath(b.getLrcid()),
										null, null, HttpTool.GET), TTpath
										+ tvsongname.getText().toString() + "-"
										+ tvsinger.getText().toString()
										+ ".lrc", nameshandler);
								Log.i("music",
										"xiancheng---------" + b.getLrcid());
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}).start();
				}
				break;
			case 2:
				keywordsFlow.rubKeywords();
				feedKeywordsFlow(keywordsFlow,
						MusicPlayerService.SongNamekeywords);
				keywordsFlow.removeAllViews();
				Random random = new Random();
				int ran = random.nextInt(10);
				if (ran % 2 == 0) {
					keywordsFlow.go2Show(KeywordsFlow.ANIMATION_OUT);
				} else {
					keywordsFlow.go2Show(KeywordsFlow.ANIMATION_IN);
				}
				break;
			case 10:
				Log.i("music", "下载保存完毕-开始更新--------");
				ShowLyric(TTpath + tvsongname.getText().toString() + "-"
						+ tvsinger.getText().toString() + ".lrc");
				break;
			case 20:
				try {
					int progress = curms * 100 / totalms;
					// 设置当前进度
					seekBar.setProgress(progress);
				} catch (Exception e) {
					e.printStackTrace();
				}
				tvcurrent.setText(StrTime.gettim(curms));
				Log.i("SMZ", "SSSSS" + progress);
				break;
			}

		}
	};
	boolean isshowkeyflower = true;
	showThread thread;

	class showThread extends Thread {
		@Override
		public void run() {
			while (isshowkeyflower) {
				try {
					if (viewPager.getCurrentItem() == 2) {
						nameshandler.sendEmptyMessage(2);
					}
					sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	boolean isrunable = true;
	int curms;

	class ProgeressThread extends Thread {
		@Override
		public void run() {
			while (isrunable) {
				if (MyApplication.mediaPlayer != null
						&& MyApplication.mediaPlayer.isPlaying()) {
					curms = MyApplication.mediaPlayer.getCurrentPosition();
					nameshandler.sendEmptyMessage(20);
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			super.run();
		}

	}

	Uri mUri;

	// 当我们从外部音频资源,会进入到当前Activity播放
	private void startPlayback() {
		Intent intent = getIntent();
		mUri = intent.getData();
		if (mUri != null) {
			String scheme = mUri.getScheme();
			AsyncQueryHandler mAsyncQueryHandler = new AsyncQueryHandler(
					getContentResolver()) {
				@Override
				protected void onQueryComplete(int token, Object cookie,
						Cursor cursor) {
					if (cursor != null && cursor.moveToFirst()) {
						int titleIdx = cursor
								.getColumnIndex(MediaStore.Audio.Media.TITLE);
						int artistIdx = cursor
								.getColumnIndex(MediaStore.Audio.Media.ARTIST);
						int idIdx = cursor
								.getColumnIndex(MediaStore.Audio.Media._ID);
						int displaynameIdx = cursor
								.getColumnIndex(OpenableColumns.DISPLAY_NAME);
						int albumindex = cursor
								.getColumnIndex(MediaStore.Audio.Media.ALBUM);
						if (idIdx >= 0) {
							long mMediaId = cursor.getLong(idIdx);
						}
						// if (albumindex > 0) {
						// tvAlbum.setText(cursor.getString(albumindex));
						// }
						if (titleIdx >= 0) {
							tvsongname.setText(cursor.getString(titleIdx));
							if (artistIdx >= 0) {
								tvsinger.setText(cursor.getString(artistIdx));
							}
						}
						if (displaynameIdx >= 0) {
							String name = cursor.getString(displaynameIdx);
							Log.i("test", name + "-------title");
						} else {
							Log.w("errror", "Cursor had no names for us");
						}
					} else {
						Log.w("error", "empty cursor");
					}
					if (cursor != null) {
						cursor.close();
					}
				}
			};
			if (scheme.equals(ContentResolver.SCHEME_CONTENT)) {
				if (mUri.getAuthority() == MediaStore.AUTHORITY) {
					mAsyncQueryHandler.startQuery(0, null, mUri, new String[] {
							MediaStore.Audio.Media.TITLE,
							MediaStore.Audio.Media.ARTIST }, null, null, null);
				} else {
					mAsyncQueryHandler.startQuery(0, null, mUri, null, null,
							null, null);
				}
			} else if (scheme.equals("file")) {
				String path = mUri.getPath();
				mAsyncQueryHandler.startQuery(0, null,
						MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
						new String[] { MediaStore.Audio.Media._ID,
								MediaStore.Audio.Media.TITLE,
								MediaStore.Audio.Media.ARTIST },
						MediaStore.Audio.Media.DATA + "=?",
						new String[] { path }, null);
				Intent intent2 = new Intent(Constant.ACTION_JUMR_OTHER);
				intent2.putExtra("name", path);
				sendBroadcast(intent2);
			} else {
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.playlayout);
		MyApplication.getInstance().addActivity(this);
		startService(new Intent(this, MusicPlayerService.class));
		musicPreference = MyApplication.musicPreference;
		context = this;
		chose_albumlist = (ImageView) findViewById(R.id.chose_albumlist);
		mContainer = (ViewGroup) findViewById(R.id.container);
		viewPager = (ViewPager) findViewById(R.id.center_body_view_pager);
		mContainer
				.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE);
		/* 中间块的布局 */
		contain_viewpage = (RelativeLayout) findViewById(R.id.contain_viewpager);
		/* 中间隐藏的listview */
		album_list = (ListView) findViewById(R.id.showalbum_listview);
		/* 底部的布局 */
		seekBar = (SeekBar) findViewById(R.id.my_seekbar);
		tvcurrent = (TextView) findViewById(R.id.tv_current_time);
		tvdurction = (TextView) findViewById(R.id.tv_durrction);
		tvsongname = (TextView) findViewById(R.id.tv_songname);
		tvsinger = (TextView) findViewById(R.id.tv_singer_name);
		/* 顶部按钮 */
		bt_down = (ImageButton) findViewById(R.id.handler_down_icon);
		/* 最底部的 一排按钮 */
		btplay = (ImageButton) findViewById(R.id.btn_play);
		btprevious = (ImageButton) findViewById(R.id.btn_previous);
		btnext = (ImageButton) findViewById(R.id.btn_next);
		bt_playmode = (ImageButton) findViewById(R.id.btnplay_mode);
		/* 显示当前在第几页 */
		ivnowpage = (ImageView) findViewById(R.id.show_nowpage);
		inflater = LayoutInflater.from(context);
		mListViews = new ArrayList<View>();
		mListViews.add(inflater.inflate(R.layout.layout_lrc, null));
		mListViews.add(inflater.inflate(R.layout.layout_albumtest, null));
		mListViews.add(inflater.inflate(R.layout.layout_keyflower, null));

		ivshow_album = (ImageView) (mListViews.get(1))
				.findViewById(R.id.show_album);
		tvcurrlrc = (TextView) (mListViews.get(1))
				.findViewById(R.id.tvcrrent_lrc);
		/* 会飞的标签 */
		keywordsFlow = (KeywordsFlow) (mListViews.get(2)
				.findViewById(R.id.frameLayout1));
		/* 歌词 */
		lyricView = (LyricView) (mListViews.get(0))
				.findViewById(R.id.audio_lrc);
		tv_nolrc = (TextView) (mListViews.get(0)).findViewById(R.id.tv_nolrc);
		pagerAdapter = new AwesomePagerAdapter();

		viewPager.setAdapter(pagerAdapter);

		viewPager.setCurrentItem(1);
		MusicinfoRec = new MusicinfoRec();
		NowPlayListAdapter adapter = new NowPlayListAdapter(this,
				MyApplication.musics);
		album_list.setAdapter(adapter);
		broadcastIntent = new Intent();
		intent = new Intent(Constant.ACTION_UPDATE_LRC);
		/* 初始化Views 的视图监听 */
		initViewsListener();
		animation = AnimationUtils.loadAnimation(MusicPlayActivity.this,
				R.anim.albumcom);
	}

	@Override
	protected void onStart() {
		super.onStart();
		// 启动的时候更新 页面当前播放信息
		sendBroadcast(new Intent(Constant.ACTION_UPDATE_ALL));
		nowplaymode = musicPreference.getPlayMode(context);
		if (nowplaymode == 0) {// 0 顺序播放 1 随机播放 2 单曲循环
			bt_playmode.setImageResource(R.drawable.play_node1);
		} else if (nowplaymode == 1) {
			bt_playmode.setImageResource(R.drawable.play_mode_random);
		} else {
			bt_playmode.setImageResource(R.drawable.play_mode_only);
		}
		MAX_WORLDS = musicPreference.getTagCount(context);
		startPlayback();
	}

	Thread myThread;

	@Override
	protected void onResume() {
		super.onResume();
		mAudioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.ACTION_UPDATE);
		filter.addAction(Constant.ACTION_UPDATE_LRC);
		registerReceiver(MusicinfoRec, filter);

		isrunable = true;
		// 启动更新进度条的线程
		myThread = new ProgeressThread();
		myThread.start();

		isshowkeyflower = true;
		thread = new showThread();
		thread.start();
		isupdatalrc = true;
		new Thread(new UIUpdateThread()).start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(MusicinfoRec);
		isupdatalrc = false;
	}

	@Override
	protected void onStop() {
		sendBroadcast(intent);
		isshowkeyflower = false;
		isrunable = false;
		isupdatalrc = false;
		super.onStop();
	}

	private void initViewsListener() {
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
				case 0:
					ivnowpage.setImageResource(R.drawable.show_left);
					break;
				case 1:
					ivnowpage.setImageResource(R.drawable.show_mid);
					break;
				case 2:
					ivnowpage.setImageResource(R.drawable.show_right);
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

		keywordsFlow.setOnItemClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String keyword = ((TextView) v).getText().toString();
				Toast.makeText(MusicPlayActivity.this, keyword,
						Toast.LENGTH_SHORT).show();
				broadcastIntent = new Intent(Constant.ACTION_FIND);
				broadcastIntent.putExtra("name", keyword);
				sendBroadcast(broadcastIntent);
			}
		});
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progres,
					boolean fromUser) {
				if (fromUser == true && Math.abs(progres - progress) >= 5) {
					progress = progres;
					broadcastIntent = new Intent(Constant.ACTION_SEEK);
					broadcastIntent.putExtra("seekcurr", progress);// 讲拖动的进度传进Service
					sendBroadcast(broadcastIntent);
					seekBar.setProgress(progress);
				}
			}
		});
		album_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				broadcastIntent = new Intent(Constant.ACTION_JUMR);
				broadcastIntent.putExtra("position", arg2);
				sendBroadcast(broadcastIntent);
				((NowPlayListAdapter) album_list.getAdapter())
						.showNowPlayPos(arg2);
			}
		});
		chose_albumlist.setOnClickListener(this);
		btplay.setOnClickListener(this);
		btprevious.setOnClickListener(this);
		btnext.setOnClickListener(this);
		bt_playmode.setOnClickListener(this);
		bt_down.setOnClickListener(this);
		initpopmenu();
	}

	// ----------------popmenu-------------------------------------
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (popupWindow.isShowing()) {
				popupWindow.dismiss();
			} else {
				popshow(contain_viewpage);
			}
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (popupWindow.isShowing()) {
				popupWindow.dismiss();
			} else {
				return super.onKeyDown(keyCode, event);
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	PopupWindow popupWindow;

	public void initpopmenu() {
		int width = getWindowManager().getDefaultDisplay().getWidth() - 10; // 菜单的宽度
		int heigth = getWindowManager().getDefaultDisplay().getHeight() / 2 - 20; // 菜单的高度
		popupWindow = new PopupWindow(context);
		View popview = inflater.inflate(R.layout.pop_menu, null);
		popupWindow.setWidth(width / 5);
		popupWindow.setHeight(heigth);
		popupWindow.setContentView(popview);
		popupWindow.setFocusable(true);
		ListView listView = (ListView) popview.findViewById(R.id.pop_menu_list);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Toast.makeText(context,
						arg0.getAdapter().getItem(arg2).toString(), 1).show();
				switch (arg2) {
				case 0:
					startActivityForResult(
							new Intent(context, lrcsetting.class), 2);
					break;
				case 1:
					mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
							AudioManager.ADJUST_RAISE,
							AudioManager.FLAG_SHOW_UI);
					break;
				case 2:
					sleeping();
					break;
				case 3:
					ShowpopChoes(new String[] { "4", "6", "8", "10", "12", "14" });
					break;
				case 4:
					SharedPreferences sp = getSharedPreferences("service", 0);
					sp.edit().putBoolean("isStart", false).commit();
					popupWindow.dismiss();
					sendBroadcast(new Intent(Constant.ACTION_STOP));
					MyApplication.getInstance().exit();
					break;
				}

			}
		});

	}

	public void sleeping() {
		View sleepdialog = getLayoutInflater().inflate(
				R.layout.save_sleeptime_dialog, null);
		final EditText editText = (EditText) sleepdialog
				.findViewById(R.id.sleeptime_edit);
		new AlertDialog.Builder(context).setView(sleepdialog)
				.setTitle("请输入进入睡眠的时间").setIcon(R.drawable.icon_menu_sleepmode)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (editText.getText().toString() == null
								&& StrTime.isnumber(editText.getText()
										.toString())) {
							Toast.makeText(context, "请输入有效时间", 2000).show();
						} else {
							int sleeptime = Integer.parseInt(editText.getText()
									.toString()) * 1000 * 60;
							timer.schedule(task, sleeptime);
						}
					}
				}).setNegativeButton("取消", null).show();
	}

	public void popshow(View v) {
		if (popupWindow.isShowing()) {
			popupWindow.dismiss();
		} else
			popupWindow.showAtLocation(contain_viewpage, Gravity.LEFT, 0, 0);
		popupWindow.setFocusable(true);
		popupWindow.setTouchInterceptor(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					popupWindow.dismiss();
					return true;
				} else
					return false;
			}
		});
	}

	// ---------------------------click
	// 事件----------------------------------------------------
	boolean showalbumlist = false;
	boolean isplaying = false;
	Intent broadcastIntent;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.chose_albumlist:
			if (!showalbumlist) {
				applyRotation(0, 90);
				showalbumlist = true;
				chose_albumlist.setImageResource(R.drawable.chose_playpage);
			} else {
				chose_albumlist.setImageResource(R.drawable.chose_albumlist);
				applyRotation(0, 90);
				showalbumlist = false;

			}
			break;
		case R.id.btn_play:
			if (!isplaying) {// 如果不是播放
				btplay.setImageResource(R.drawable.desktop_pausebt_b);
				broadcastIntent.setAction(Constant.ACTION_PLAY);
				isplaying = true;
			} else {
				broadcastIntent.setAction(Constant.ACTION_PAUSE);
				isplaying = false;
				btplay.setImageResource(R.drawable.desktop_playbt_b);
			}
			sendBroadcast(broadcastIntent);
			break;
		case R.id.btn_previous:
			btplay.setImageResource(R.drawable.desktop_pausebt_b);
			isplaying = true;
			broadcastIntent.setAction(Constant.ACTION_PREVIOUS);
			sendBroadcast(broadcastIntent);
			break;
		case R.id.btn_next:
			btplay.setImageResource(R.drawable.desktop_pausebt_b);
			isplaying = true;
			broadcastIntent.setAction(Constant.ACTION_NEXT);
			sendBroadcast(broadcastIntent);
			break;
		case R.id.btnplay_mode:
			nowplaymode++;
			if (nowplaymode == 1) {
				bt_playmode.setImageResource(R.drawable.play_mode_random);
			} else if (nowplaymode == 2) {
				bt_playmode.setImageResource(R.drawable.play_mode_only);
			} else {
				nowplaymode = 0;
				bt_playmode.setImageResource(R.drawable.play_node1);
			}
			musicPreference.savaPlayMode(context, nowplaymode);
			broadcastIntent.setAction(Constant.ACTION_SET_PLAYMODE);
			broadcastIntent.putExtra("play_mode", nowplaymode);
			sendBroadcast(broadcastIntent);
			break;
		case R.id.handler_down_icon:
			startActivity(new Intent(context, MainActivity.class));
			overridePendingTransition(R.anim.act_bac_in, R.anim.act_bac_out);
			finish();
			break;
		}
	}

	public void ShowpopChoes(String[] data) {
		AlertDialog.Builder builder = new Builder(context);
		AlertDialog dialog = builder
				.setTitle("标签个数选择")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setSingleChoiceItems(data,
						musicPreference.getTagPos(data, context),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:
									MAX_WORLDS = 4;
									musicPreference.savaTagCount(context, 4);
									break;
								case 1:
									MAX_WORLDS = 6;
									musicPreference.savaTagCount(context, 6);
									break;
								case 2:
									MAX_WORLDS = 8;
									musicPreference.savaTagCount(context, 8);
									break;
								case 3:
									MAX_WORLDS = 10;
									musicPreference.savaTagCount(context, 10);
									break;
								case 4:
									MAX_WORLDS = 12;
									musicPreference.savaTagCount(context, 12);
									break;
								case 5:
									MAX_WORLDS = 14;
									musicPreference.savaTagCount(context, 14);
									break;
								}
								dialog.dismiss();
							}
						}).setNegativeButton("取消", null).show();

	}

	// ------------------------------歌词显示-------------------------------------------------
	/**
	 * 显示歌词信息
	 * 
	 * @param music
	 * @param musicname
	 * @param singername
	 */
	String netlrcpath = null;

	private void ShowLyric(String path) {
		if (new File(path).exists()) {
			doshowlrc(music.getSavePath(), path);
			ishavelrc = true;
		} else {
			netlrcpath = BaiduLrc.getMusic(music.getMusicName(),
					music.getSinger());
			tv_nolrc.setVisibility(View.VISIBLE);
			tvcurrlrc.setText("未找到歌词文件");
			lyricView.setVisibility(View.GONE);
			new Thread() {
				@Override
				public void run() {
					try {
						new LRCXmlParser(nameshandler)
								.parse(HttpTool.getStream(netlrcpath, null,
										null, HttpTool.GET));
					} catch (IOException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}.start();
		}
	}

	// 显示歌词
	public void doshowlrc(String musicpath, String lrcpath) {
		tv_nolrc.setVisibility(View.GONE);
		lyricView.setVisibility(View.VISIBLE);
		mLyric = new Lyric(new File(lrcpath), new PlayListItems("", musicpath,
				0l, true));
		lyricView.setmLyric(mLyric);
		lyricView.setSentencelist(mLyric.list);
		// 设置 歌词颜色
		lyricView.setNotCurrentPaintColor(Color.BLACK);
		// 设置当前显示的歌词 颜色
		lyricView.setCurrentPaintColor(musicPreference.getLrcColor(context));
		lyricView.setLrcTextSize(musicPreference.getLrcSize(context));
		lyricView.setTexttypeface(Typeface.SERIF);
		lyricView.setTextHeight(40);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 5) {
			ShowLyric(TTpath + tvsongname.getText().toString() + "-"
					+ tvsinger.getText().toString() + ".lrc");
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	// -------------------lrc--------------------------------------
	boolean isupdatalrc = true;

	class UIUpdateThread implements Runnable {
		long time = 1000;

		public void run() {
			while (isupdatalrc) {
				try {
					if (MyApplication.mediaPlayer.isPlaying()
							&& ishavelrc == true) {
						lyricView.updateIndex(MyApplication.mediaPlayer
								.getCurrentPosition());
						mHandler.post(mUpdateResults);
					}
					Thread.sleep(time);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	// ---------------------lrc-----------------------------------------
	Handler mHandler = new Handler();
	Runnable mUpdateResults = new Runnable() {
		public void run() {
			lyricView.invalidate();
		}
	};

	/* page view 的适配器 */
	private class AwesomePagerAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View collection, int position) {
			try {
				((ViewPager) collection).addView(mListViews.get(position), 0);
			} catch (Exception e) {
			}
			return mListViews.get(position);
		}

		@Override
		public void destroyItem(View collection, int position, Object view) {
			if (position > 0) {
				((ViewPager) collection).removeView(mListViews.get(position));
			}
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == (object);
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

	}

	/**
	 * 3D翻转动画
	 * 
	 * @param start
	 * @param end
	 */
	private void applyRotation(float start, float end) {
		final float centerX = mContainer.getWidth() / 2.0f;
		final float centerY = mContainer.getHeight() / 2.0f;
		Rotate3dAnimation rotation = new Rotate3dAnimation(start, end, centerX,
				centerY, 200.0f, true);
		rotation.setDuration(500);
		rotation.setInterpolator(new AccelerateInterpolator());
		rotation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {

				mContainer.post(new Runnable() {
					@Override
					public void run() {
						if (showalbumlist) {
							contain_viewpage.setVisibility(View.GONE);
							album_list.setVisibility(View.VISIBLE);
							album_list.setSelection(position);
						} else {
							album_list.setVisibility(View.GONE);
							contain_viewpage.setVisibility(View.VISIBLE);
						}
						Rotate3dAnimation rotatiomAnimation = new Rotate3dAnimation(
								-90, 0, centerX, centerY, 200.0f, false);
						rotatiomAnimation.setDuration(500);
						rotatiomAnimation
								.setInterpolator(new DecelerateInterpolator());
						mContainer.startAnimation(rotatiomAnimation);
					}
				});

			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationStart(Animation arg0) {
			}
		});
		mContainer.startAnimation(rotation);
	}

}
