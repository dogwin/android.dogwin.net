package com.tarena.fashionmusic.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.tarena.fashionmusic.R;
import com.tarena.fashionmusic.db.MusicDao;
import com.tarena.fashionmusic.entry.Music;
import com.tarena.fashionmusic.main.MainActivity;
import com.tarena.fashionmusic.util.HttpTool;
import com.tarena.fashionmusic.util.StreamTool;

public class DownloadService extends Service {
	private static final int MSG_OK = 1;// �������
	private static final int MSG_ERROR = 2;// ���ش���
	private static final int MSG_START = 0;// ��ʼ����
	// �������
	private ArrayList<Music> taskQueue;
	// ������ѯ�߳�
	private Thread thread;
	// Service�Ƿ��Ѿ�unbind�ı�ʶֵ
	private boolean isUnbind = false;

	// ��ǰ�����ļ����ܳ���
	private long fileLength;
	// ��ǰ���ص������ļ���
	private String currentMusicName;

	// binder����������Activity����Serviceͨ��
	public class MyBinder extends Binder {
		// ���������(�˷�����activity�е��ã��ڴ�service��ִ��)
		public void addTask(Music task) {
			if (!taskQueue.contains(task)) {
				taskQueue.add(task);
				synchronized (thread) {
					thread.notify();
				}
			}
		}
	}

	// �߳�ͨ�Ŷ���
	private Handler handler;

	@Override
	public IBinder onBind(Intent intent) {
		// ����binder����
		return new MyBinder();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// ��ʼ���������
		taskQueue = new ArrayList<Music>();
		// ����������ѯ�߳�
		thread = new Thread() {
			@Override
			public void run() {
				while (true) {
					// �����������������ʱ��ѭ����������
					while (taskQueue.size() > 0) {
						// ��������
						try {
							// ȡ����������еĵ�һ������
							Music task = taskQueue.remove(0);
							// //��ȡ��ǰ���ص�������ļ�����
							String uri = HttpTool.URI + task.getMusicPath();
							fileLength = HttpTool.getLength(uri, null, null,
									HttpTool.GET) / 1024;
							// ��ȡ��ǰ���ص�������ļ���
							currentMusicName = task.getMusicName();
							// ��ʼ����ʱ������Ϣ�����߳�
							handler.sendEmptyMessage(MSG_START);
							// �����ļ�
							InputStream in = HttpTool.getStream(uri, null,
									null, HttpTool.GET);
							StreamTool.save(in, task.getSavePath(), handler,
									fileLength);
							// �������ʱ������Ϣ�����߳�
							Message msg = handler.obtainMessage(MSG_OK, task);
							handler.sendMessage(msg);
						} catch (IOException e) {
							e.printStackTrace();
							// ����ʧ��ʱ������Ϣ�����߳�
							handler.sendEmptyMessage(MSG_ERROR);
						}

					}
					// ��������б�������������������ɣ���service�Ѿ����
					// ��ֹͣservice���˳��߳�
					if (isUnbind) {
						stopSelf();
						break;
					}

					// ��������б�Ϊ�գ���δ������̵߳ȴ�
					try {
						synchronized (this) {
							this.wait();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		};
		// �߳�����
		thread.start();
		// ����handler����
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// ���֪ͨ����������
				NotificationManager manager = (NotificationManager) getApplication()
						.getSystemService(NOTIFICATION_SERVICE);
				// ����֪ͨ����
				Notification noti = new Notification(
						android.R.drawable.stat_sys_download, "֪ͨ",
						System.currentTimeMillis());
				// ������֪ͨʱ����
				// noti.defaults = Notification.DEFAULT_LIGHTS;
				// ����֪ͨ�������
				noti.flags = Notification.FLAG_NO_CLEAR;
				// ����֪ͨ��ʾ�Զ���View��ͼ
				noti.contentView = new RemoteViews(getApplication()
						.getPackageName(), R.layout.notiitem);
				// ����֪ͨ��PendingIntent
				noti.contentIntent = PendingIntent.getActivity(
						DownloadService.this, 0, new Intent(
								DownloadService.this, MainActivity.class),
						PendingIntent.FLAG_UPDATE_CURRENT);
				switch (msg.what) {
				case MSG_START:// ��ʼ����
					// ����֪ͨ��ʾ��������
					noti.contentView.setTextViewText(R.id.tvMusicName_noti,
							currentMusicName);
					// ����֪ͨ�е��ļ��ܳ���
					noti.contentView.setTextViewText(R.id.tvFileLength,
							String.valueOf(fileLength) + "kb");
					// ����֪ͨ�е������صĳ���
					noti.contentView
							.setTextViewText(R.id.tvLoadedLength, "0kb");
					// ����֪ͨ�еĽ������ĵ�ǰ����
					noti.contentView.setProgressBar(R.id.progressBar1,
							(int) fileLength, 0, false);
					// ����֪ͨ��֪ͨ��
					manager.notify(0, noti);
					break;
				case StreamTool.MSG_PROGRESS:// ���ؽ�����
					noti.contentView.setTextViewText(R.id.tvMusicName_noti,
							currentMusicName);
					noti.contentView.setTextViewText(R.id.tvFileLength,
							String.valueOf(fileLength) + "kb");
					noti.contentView.setTextViewText(R.id.tvLoadedLength,
							String.valueOf(msg.arg1) + "kb");
					noti.contentView.setProgressBar(R.id.progressBar1,
							(int) fileLength, msg.arg1, false);
					manager.notify(0, noti);
					break;
				case MSG_OK:// �������
					// music��Ϣ��ӵ�loadedmusic����
					MusicDao musicDao = new MusicDao(DownloadService.this);
					musicDao.insert((Music) msg.obj);
					// ����������ɵĹ㲥
					Intent intent = new Intent(
							DownloadManager.ACTION_DOWNLOAD_COMPLETE);
					Bundle bundle = new Bundle();
					bundle.putSerializable("music", (Music) msg.obj);
					intent.putExtras(bundle);
					sendBroadcast(intent);
					Toast.makeText(DownloadService.this, "�������", 1).show();
				case MSG_ERROR:// ���س���
					manager.cancel(0);
					break;

				}

			}

		};

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// ��service���ʱ������inUnbindΪtrue
		isUnbind = true;
		return super.onUnbind(intent);
	}

}
