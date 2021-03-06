package com.tarena.fashionmusic.play.notify;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

import com.tarena.fashionmusic.MyApplication;
import com.tarena.fashionmusic.R;
import com.tarena.fashionmusic.entry.Music;
import com.tarena.fashionmusic.play.MusicPlayActivity;
import com.tarena.fashionmusic.service.MusicPlayerService;
import com.tarena.fashionmusic.util.BitmapTool;

public class MyNotiofation {
	public static final int NOTIFICATION_ID = 123321456;

	public static void getNotif(Context context, Music music,
			NotificationManager manager) {
		if (music != null) {
			Notification notification = new Notification(
					android.R.drawable.ic_media_play, music.getMusicName(),
					System.currentTimeMillis());
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.notification);
			int nowplaystatus = MusicPlayerService.status;
			// 设置通知显示的内容
			Bitmap bitmap = BitmapTool.getbitBmBykey(MyApplication.context,
					music.getAlbumkey());
			if (bitmap != null) {
				remoteViews.setImageViewBitmap(R.id.image, bitmap);
			} else {
				remoteViews.setImageViewResource(R.id.image,
						R.drawable.app_icon);
			}
			// 前一首
			remoteViews.setOnClickPendingIntent(R.id.btnPrevious_player,
					PendingIntent.getBroadcast(context, NOTIFICATION_ID,
							new Intent("com.tarena.action.PREVIOUS"),
							PendingIntent.FLAG_ONE_SHOT));
			// 暂停播放
			remoteViews.setOnClickPendingIntent(
					R.id.btnPlay_player,
					getinten(context, remoteViews, nowplaystatus, manager,
							music));
			// 后一首
			remoteViews.setOnClickPendingIntent(R.id.btnNext_player,
					PendingIntent.getBroadcast(context, NOTIFICATION_ID,
							new Intent("com.tarena.action.NEXT"),
							PendingIntent.FLAG_ONE_SHOT));
			remoteViews.setTextViewText(R.id.no_musicname, "            "
					+ music.getSinger() + " : " + music.getMusicName());
			// 将内容指定给通知
			notification.contentView = remoteViews;
			// 指定点击通知后跳到那个Activity
			notification.contentIntent = PendingIntent.getActivity(context, 0,
					new Intent(context, MusicPlayActivity.class),
					PendingIntent.FLAG_UPDATE_CURRENT);
			// 指定通知可以清除
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			// 指定通知不能清除
			// notification.flags|=Notification.FLAG_NO_CLEAR;
			// 通知显示的时候播放默认声音
			notification.flags |= Notification.FLAG_SHOW_LIGHTS;
			// 向NotificationManager注册一个notification，并用NOTIFICATION_ID作为管理的唯一标示
			manager.notify(NOTIFICATION_ID, notification);
		}
	}

	private static PendingIntent getinten(Context context,
			RemoteViews remoteViews, int nowplaystatus,
			NotificationManager manager, Music music) {
		PendingIntent intent = null;
		if (nowplaystatus == 3) {
			intent = PendingIntent.getBroadcast(context, 0, new Intent(
					"com.tarena.action.PAUSE"),
					PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setImageViewResource(R.id.btnPlay_player,
					R.drawable.desktop_pause);
		} else {
			intent = PendingIntent.getBroadcast(context, 1, new Intent(
					"com.tarena.action.PLAY"),
					PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setImageViewResource(R.id.btnPlay_player,
					R.drawable.desktop_play);
		}
		return intent;
	}

}
