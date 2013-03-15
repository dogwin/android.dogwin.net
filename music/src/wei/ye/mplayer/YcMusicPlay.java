package wei.ye.mplayer;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TabHost;
import android.widget.TextView;

public class YcMusicPlay extends TabActivity {
	// 歌曲列表
	ListView mListView;
	public static YcMusicAdapter mAdapter;
	// 上一首，播放暂停、下一首
	public static ImageButton pre_button, play_button, next_button;
	// 歌曲当前播放时间、歌曲总时间
	public static int playingTime = 0;
	public static int songTime = 0;
	// 绑定SeekBar和时间TextView
	public static SeekBar seekbar;
	public TextView play_time;
	// 歌手名和专辑图像
	static TextView mName;
	static ImageView mAlbum;
	private TabHost mTabHost;
	// 歌词
	public static YcMusicLrcView mLyricView;

	private void findBy() {
		// 歌曲列表
		mListView = (ListView) findViewById(R.id.mlistView);
		// 上一首，播放暂停、下一首
		pre_button = (ImageButton) findViewById(R.id.pre_button);
		play_button = (ImageButton) findViewById(R.id.play_button);
		next_button = (ImageButton) findViewById(R.id.next_button);
		// 进度条
		seekbar = (SeekBar) findViewById(R.id.mseekBar);
		// 时间TextView
		play_time = (TextView) findViewById(R.id.play_time);
		// 歌手名和专辑图像
		mName = (TextView) findViewById(R.id.mText);
		mAlbum = (ImageView) findViewById(R.id.album_imageView);
		// 歌词
		mLyricView = (YcMusicLrcView) findViewById(R.id.mLrcView);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.music_play);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar);
		// 绑定
		findBy();
		// TabHost
		newTab();
		// 随便听听界面
		tryListen();
		mAdapter = new YcMusicAdapter(this);
		mListView.setAdapter(mAdapter);
		pre_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (YcMusicPlay.mAdapter.musicList != null
						&& YcMusicPlay.mAdapter.musicList.size() != 0) {
					Intent pre = new Intent(YcMusicPlay.this,
							YcMusicService.class);
					pre.putExtra("control", "previous");
					startService(pre);
				}
			}
		});
		play_button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (YcMusicPlay.mAdapter.musicList != null
						&& YcMusicPlay.mAdapter.musicList.size() != 0) {
					Intent play = new Intent(YcMusicPlay.this,
							YcMusicService.class);
					play.putExtra("control", "play");
					startService(play);
				}

			}
		});

		next_button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (YcMusicPlay.mAdapter.musicList != null
						&& YcMusicPlay.mAdapter.musicList.size() != 0) {
					Intent next = new Intent(YcMusicPlay.this,
							YcMusicService.class);
					next.putExtra("control", "next");
					startService(next);
				}
			}
		});

		mListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent play = new Intent(YcMusicPlay.this, YcMusicService.class);
				play.putExtra("control", "listClick");
				play.putExtra("musicId", arg2);
				startService(play);
			}
		});
		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (YcMusicService.mplayer != null) {
					if (fromUser) {
						YcMusicService.mplayer.seekTo(progress);
					}
					play_time.setText(mAdapter.toTime(progress));
				} else {
					seekBar.setMax(0);
				}

			}
		});
	}

	// 自定义TabHost
	public void newTab() {
		// 获取TabHoast对象
		mTabHost = getTabHost();
		/* 为TabHost添加标签 */
		// 新建一个newTabSpec(newTabSpec)
		// 设置其标签和图标(setIndicator)
		// 设置内容(setContent)
		TextView myMusic = new TextView(this);
		TextView musicLrc = new TextView(this);
		TextView elseListen = new TextView(this);
		myMusic.setText("我的音乐");
		musicLrc.setText("歌曲歌词");
		elseListen.setText("随便听听");
		myMusic.setGravity(Gravity.CENTER);
		musicLrc.setGravity(Gravity.CENTER);
		elseListen.setGravity(Gravity.CENTER);
		mTabHost.addTab(mTabHost.newTabSpec("mlistView").setIndicator(myMusic)
				.setContent(R.id.musicTab));
		mTabHost.addTab(mTabHost.newTabSpec("mLrcView").setIndicator(musicLrc)
				.setContent(R.id.lrcTab));
		mTabHost.addTab(mTabHost.newTabSpec("mLrcView")
				.setIndicator(elseListen).setContent(R.id.elseListen));
	}

	// 主菜单点击返回键，弹出对话框
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(YcMusicPlay.this)
					.setIcon(R.drawable.dialog_information)
					.setTitle("提示")
					.setMessage("你确定要退出Yc音乐吗？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									/* 在这里设计当对话框按钮单击之后要运行的事件 */
									stopService(new Intent(YcMusicPlay.this,
											YcMusicService.class));
									System.exit(0);
								}
							}).setNegativeButton("取消", null).show();
		}
		return super.onKeyDown(keyCode, event);
	}

	public void tryListen() {
		Gallery mGallery = (Gallery) findViewById(R.id.mGallery);
		mGallery.setAdapter(new ImageAdapter(this));
		mGallery.setSelection(images.length / 2);
	};

	private class ImageAdapter extends BaseAdapter {
		private Context context;

		public ImageAdapter(Context context) {
			this.context = context;
		}

		// 可以return images.lenght(),在这里返回Integer.MAX_VALUE
		// 是为了使图片循环显示
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView iv = new ImageView(context);
			// 设置具体要显示的图片
			iv.setImageResource(images[position % images.length]);
			// 设置ImageView的高度和宽度
			iv.setLayoutParams(new Gallery.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			iv.setAdjustViewBounds(true);
			return iv;
		}
	}

	private static int[] images = { R.drawable.s70, R.drawable.s80,
			R.drawable.riyu, R.drawable.huayu, R.drawable.hanyu,
			R.drawable.oumei, R.drawable.fayu, R.drawable.newsong,
			R.drawable.youlove, R.drawable.yueyu };

	// 设置菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, Menu.FIRST + 1, 1, "关于").setIcon(
				android.R.drawable.ic_menu_info_details);
		menu.add(Menu.NONE, Menu.FIRST + 2, 2, "退出").setIcon(
				android.R.drawable.ic_lock_power_off);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST + 1:
			// 这里就是设置弹出对话框的地方
			new AlertDialog.Builder(YcMusicPlay.this)
					.setIcon(R.drawable.dialog_information).setTitle("关于")
					.setMessage(R.string.about).setPositiveButton("确定", null)
					.show();
			break;

		case Menu.FIRST + 2:
			new AlertDialog.Builder(YcMusicPlay.this)
					.setIcon(R.drawable.dialog_information)
					.setTitle("提示")
					.setMessage("你确定要退出Yc音乐吗？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									/* 在这里设计当对话框按钮单击之后要运行的事件 */
									stopService(new Intent(YcMusicPlay.this,
											YcMusicService.class));
									System.exit(0);
								}
							}).setNegativeButton("取消", null).show();

			break;
		}
		return false;
	}

}
