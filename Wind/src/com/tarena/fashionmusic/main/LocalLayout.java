package com.tarena.fashionmusic.main;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.tarena.fashionmusic.MusicLayIntenface;
import com.tarena.fashionmusic.MyApplication;
import com.tarena.fashionmusic.R;
import com.tarena.fashionmusic.db.MusicGroupDao;
import com.tarena.fashionmusic.db.MusicItemDao;
import com.tarena.fashionmusic.entry.Music;
import com.tarena.fashionmusic.entry.MusicGroup;
import com.tarena.fashionmusic.entry.MusicItem;
import com.tarena.fashionmusic.main.adapter.LocalMusicListAdapter;
import com.tarena.fashionmusic.util.Constant;

public class LocalLayout extends LinearLayout implements MusicLayIntenface {

	View rootview;
	ListView localistview;
	LayoutInflater inflater;
	Context context;
	MusicItemDao musicItemDao;
	LocalMusicListAdapter adapter;
	Handler handler;

	public static final int MULTCHOSE = 1;
	public static final int STAR_ADDGROUP = 2;
	public static final int SINGLE_CHOSE = 3;
	public static final int CHANGE_LIST = 4;

	public LocalLayout(Context context, Handler handler) {
		super(context);
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));
		rootview = inflater.inflate(R.layout.musiclist, this, true);
		this.handler = handler;
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		musicItemDao = new MusicItemDao(context);
		localistview = (ListView) rootview.findViewById(R.id.lvSounds);
	}

	ArrayList<Music> locallist;
	ArrayList<Integer> musicid = new ArrayList<Integer>();;
	MusicItem musicItem;

	@Override
	public void initData() {

		locallist = MyApplication.musics;
		adapter = new LocalMusicListAdapter(context, locallist, localistview);
		localistview.setAdapter(adapter);
	}

	@Override
	public void initListener() {
		localistview.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				String[] data = { "����", "��ӵ�����", "ɾ��" };
				ShowpopChoes(data, arg2);
				return false;
			}
		});

		localistview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				goplay(arg2);
			}
		});

	}

	public void ShowpopChoes(String[] data, final int positions) {
		AlertDialog.Builder builder = new Builder(context);
		AlertDialog dialog = builder
				.setTitle("����ѡ��")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setSingleChoiceItems(data, -1,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// ��ȡ��id
								Music music = locallist.get(positions);
								if (which == 0) {// ����
									goplay(positions);
								} else if (which == 1) {// ��ӵ�����
									AddToGroup(context, music.getId(), false,
											null);
								} else if (which == 2) {// ɾ��
									// ��ý�����ɾ�������ֵļ�¼
									LocalLayout.this.context
											.getContentResolver()
											.delete(ContentUris
													.withAppendedId(
															MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
															music.getId()),
													null, null);
									// ��sdcard��ɾ���ļ�
									new File(music.getSavePath()).delete();
									// ����listView
									LocalLayout.this.adapter.remove(positions);
									// �����ַ����б����Ƴ�������Ϣ
									musicItemDao.deleteItemByMusicid(music
											.getId());

								}
								dialog.cancel();
							}
						}).setNegativeButton("ȡ��", null).show();
	}

	/**
	 * ��ӵ�ǰ���ֵ�����
	 * 
	 * @param context
	 * @param positions
	 */
	private void AddToGroup(final Context context, final int musicId,
			final boolean ismult, final ArrayList<Integer> ids) {
		// ��ѯ���е�musicgroup
		ArrayList<MusicGroup> groups = new MusicGroupDao(context).getGroups();
		// �����е�musicgrouptitle������һ��������
		final String[] titles = new String[groups.size()];
		for (int i = 0; i < titles.length; i++) {
			titles[i] = groups.get(i).getId() + "��" + groups.get(i).getTitle();
		}
		// ������ѡ�Ի���
		AlertDialog.Builder builder = new Builder(context);
		final AlertDialog dialog = builder
				.setTitle("ѡ�����")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setSingleChoiceItems(titles, -1,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// ��ȡ��id
								int groupId = Integer.parseInt(titles[which]
										.substring(0,
												titles[which].indexOf("��")));
								if (ismult) {
									for (Integer id : ids) {
										addToGroup(id, groupId);
									}
								} else {
									addToGroup(musicId, groupId);
								}
								// �رնԻ���
								dialog.cancel();
							}
						}).setNegativeButton("ȡ��", null).show();
	}

	private void addToGroup(final int musicId, int groupId) {
		// ����musicItem����
		musicItem = new MusicItem();
		musicItem.setGroupId(groupId);
		musicItem.setMusicId(musicId);
		// ������ݵ�musicitem��
		musicItemDao.addMusicItem(musicItem);
	}

	public void goplay(int position) {
		Intent intent = new Intent(Constant.ACTION_JUMR);
		intent.putExtra("position", position);
		context.sendBroadcast(intent);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void Refresh(Object... obj) {
		int FLAG = Integer.parseInt(String.valueOf(obj[0]));
		if (FLAG == MULTCHOSE) {
			localistview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			adapter.setmultchosemode(true);
		} else if (FLAG == STAR_ADDGROUP) {
			Iterator i = adapter.getchoseMap().values().iterator();
			while (i.hasNext()) {
				Music o = (Music) i.next();
				musicid.add(o.getId());
			}
			AddToGroup(context, 0, true, musicid);
			handler.sendEmptyMessage(10);
		} else if (SINGLE_CHOSE == FLAG) {
			adapter.setmultchosemode(false);
		} else if (CHANGE_LIST == FLAG) {
			adapter.addItems((ArrayList<Music>) obj[1]);
		}
	}

}
