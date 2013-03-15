package wei.ye.mplayer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import wei.ye.mplayer.YcMusicLrc.LyricContent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;

public class YcMusicService extends Service implements OnCompletionListener {
	public static MediaPlayer mplayer;
	public static int playing_id = 0;
	private YcMusicLrc mLrcRead;
	private int index=0;
	private int CurrentTime=0;
	private int CountTime=0;
	private List<LyricContent> LyricList=new ArrayList<LyricContent>();
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		initMediaSource(initMusicUri(0));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		String playFlag = intent.getExtras().getString("control");
		if ("play".equals(playFlag)) {
			playMusic();
		} else if ("next".equals(playFlag)) {
			offHandler();
			playNext();
		} else if ("previous".equals(playFlag)) {
			// playFlag1 = false;结束上一个线程
			offHandler();
			playPre();
		} else if ("listClick".equals(playFlag)) {
			// playFlag1 = false;结束上一个线程
			offHandler();
			playing_id = intent.getExtras().getInt("musicId");
			initMediaSource(initMusicUri(playing_id));
			playMusic();
		}
	}

	public void offHandler() {
		playFlag = false;
	}

	/**
	 * 初始化媒体对象
	 * 
	 * @param mp3Path
	 */
	public void initMediaSource(String mp3Path) {
		Uri mp3Uri = Uri.parse(mp3Path);
		if (mplayer != null) {
			mplayer.stop();
			mplayer.reset();
			mplayer = null;
		}
		mplayer = MediaPlayer.create(this, mp3Uri);
		mplayer.setOnCompletionListener(this);
		mLrcRead=new YcMusicLrc();
	}

	/**
	 * 返回列表第几行的歌曲路径
	 * 
	 * @param _id
	 *            表示歌曲序号，从0开始
	 * @return
	 */
	public String initMusicUri(int _id) {
		playing_id = _id;
		return YcMusicPlay.mAdapter.musicList.get(playing_id).getMusicPath();
	}

	/**
	 * 音乐播放方法，并且带有暂停方法
	 */
	public Thread thread;
	public boolean playFlag = true;

	public void playMusic() {

		if (mplayer != null) {
			if (mplayer.isPlaying()) {
				YcMusicPlay.play_button
						.setImageResource(R.drawable.play_button_xml);
				mplayer.pause();
			} else {
				setInfo();
				YcMusicPlay.play_button
						.setImageResource(R.drawable.pause_button_xml);
				mplayer.start();
				try {
					mLrcRead.Read(YcMusicPlay.mAdapter.musicList.get(playing_id).getMusicPath());
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			LyricList=mLrcRead.GetLyricContent();			
			//设置歌词资源
			YcMusicPlay.mLyricView.setSentenceEntities( LyricList);      
			mHandler.post(mRunnable);
			playFlag = true;
			thread = new Thread() {
				@Override
				public void run() {
					while (playFlag) {
						YcMusicPlay.playingTime = mplayer.getCurrentPosition();
						YcMusicPlay.seekbar
								.setProgress(YcMusicPlay.playingTime);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

				}
			};
			thread.start();
		}
		mplayer.setOnCompletionListener(new OnCompletionListener() {
			public void onCompletion(MediaPlayer mp) {
				playNext();
			}
		});
	}

	public void setInfo() {
		// 获得歌曲时间
		YcMusicPlay.songTime = YcMusicPlay.mAdapter.musicList.get(
				YcMusicService.playing_id).getMusicTime();
		YcMusicPlay.seekbar.setMax(YcMusicPlay.songTime);
		YcMusicPlay.mName.setText(YcMusicPlay.mAdapter
				.toMp3(YcMusicPlay.mAdapter.musicList.get(YcMusicService.playing_id)
						.getMusicName()));
		String url = YcMusicPlay.mAdapter.getAlbumArt(YcMusicPlay.mAdapter.musicList
				.get(YcMusicService.playing_id).getMusicId());
		if (url != null) {
			YcMusicPlay.mAlbum.setImageURI(Uri.parse(url));
		} else {
			YcMusicPlay.mAlbum.setImageResource(R.drawable.album);
		}
	}

	// 上一首
	public void playPre() {
		if (playing_id == 0) {
			playing_id = YcMusicPlay.mAdapter.musicList.size() - 1;
			initMediaSource(initMusicUri(playing_id));
		} else {
			initMediaSource(initMusicUri(--playing_id));
		}
		playMusic();
	}

	// 下一首
	public void playNext() {
		if (playing_id == YcMusicPlay.mAdapter.musicList.size() - 1) {
			initMediaSource(initMusicUri(0));
		} else {
			initMediaSource(initMusicUri(++playing_id));
		}
		playMusic();
	}

	public void onCompletion(MediaPlayer arg0) {

	}

	Handler mHandler=new Handler();
	
    Runnable mRunnable= new Runnable() {
		public void run() {
			
			YcMusicPlay.mLyricView.SetIndex(Index());
			
			YcMusicPlay.mLyricView.invalidate();
			
			mHandler.postDelayed(mRunnable, 100);
		}
	};
	
	public int Index(){
        if(mplayer.isPlaying()){
		 CurrentTime=mplayer.getCurrentPosition();

		 CountTime=mplayer.getDuration();
        }
		if(CurrentTime<CountTime){

			for(int i=0;i<LyricList.size();i++){
				if(i<LyricList.size()-1){
					if(CurrentTime<LyricList.get(i).getLyricTime()&&i==0){
						index=i;
					}

					if(CurrentTime>LyricList.get(i).getLyricTime()&&CurrentTime<LyricList.get(i+1).getLyricTime()){
						index=i;
					}
				}

				if(i==LyricList.size()-1&&CurrentTime>LyricList.get(i).getLyricTime()){
					index=i;
				}
			}
		}

		return index;
	}
}