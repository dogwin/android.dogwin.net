package com.tarena.fashionmusic.db;

import java.io.File;
import java.util.ArrayList;

import com.tarena.fashionmusic.entry.Music;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class MusicDao {
	private DBOpenhelper helper;
	private SQLiteDatabase db;

	public MusicDao(Context context) {
		helper = new DBOpenhelper(context);
	}

	/**
	 * �ͷ�db����ķ���
	 */
	public void close() {
		if (db != null && db.isOpen()) {
			db.close();
		}
	}
	/**
	 * ����в����¼�¼
	 * @param music
	 * @return
	 */
	public long insert(Music music) {
		long rowId = -1;
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("_id", music.getId());
		values.put("musicname", music.getMusicName());
		values.put("singer", music.getSinger());
		values.put("_data", music.getSavePath());
		// insert����ֵ�����������ɹ��󣬲����е�id��ʧ��-1
		rowId = db.insert(DBOpenhelper.LOADEDMUSIC_TBL, null, values);
		db.close();
		return rowId;
	}

	/**
	 * ���±��м�¼
	 * @param music
	 * @return
	 */
	public int update(Music music) {
		int count = 0;
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("musicname", music.getMusicName());
		values.put("singer", music.getSinger());
		// ���β�����Ӱ��ļ�¼����
		count = db.update(DBOpenhelper.LOADEDMUSIC_TBL, values,
				"_id=" + music.getId(), null);
		db.close();
		return count;
	}

	/**
	 * ɨ������Ŀ¼�µ������ļ��������±��еļ�¼
	 */
	public void scanDIR() {
		// ��ѯ��������������Ϣ
		Cursor c = query();
		// ������ѯ�����
		while (c.moveToNext()) {
			// ��ȡ���ֵ�id�ͱ���·��
			int id = c.getInt(c.getColumnIndex("_id"));
			String path = c.getString(c.getColumnIndex("_data"));
			// ����������ļ��Ѳ����ڣ���ӱ���ɾ��������¼
			if (!(new File(path).exists())) {
				delete(id);
			}
		}
		// �ͷ���Դ
		c.close();
		close();
	}

	/**
	 * ����idɾ�����еļ�¼
	 * @param id
	 * @return
	 */
	public int delete(int id) {
		int count = 0;
		SQLiteDatabase db = helper.getWritableDatabase();
		count = db.delete(DBOpenhelper.LOADEDMUSIC_TBL, "_id=" + id, null);
		db.close();
		return count;
	}

	/**
	 * ��ѯ���е�ȫ����¼
	 * @return
	 */
	public Cursor query() {
		Cursor cursor = null;
		db = helper.getReadableDatabase();
		cursor = db.rawQuery("select * from " + DBOpenhelper.LOADEDMUSIC_TBL,
				null);
		return cursor;
	}

	/**
	 * ���ر��еļ�¼����
	 * @return
	 */
	public int getCount() {
		int count = 0;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from "
				+ DBOpenhelper.LOADEDMUSIC_TBL, null);
		if (cursor.moveToNext()) {
			count = cursor.getInt(0);
		}
		cursor.close();
		db.close();
		return count;
	}

	/**
	 * �ж�ָ��id��music�Ƿ�����ڱ���
	 * @param id
	 * @return
	 */
	public boolean exists(int id) {
		boolean isExists = false;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from "
				+ DBOpenhelper.LOADEDMUSIC_TBL + " where _id=" + id, null);
		if (cursor.moveToNext()) {
			int count = cursor.getInt(0);
			if (count > 0) {
				isExists = true;
			}
		}
		cursor.close();
		db.close();
		return isExists;
	}

	/**
	 * @return
	 */
	public ArrayList<Music> getPageData() {
		ArrayList<Music> musics = new ArrayList<Music>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from "
				+ DBOpenhelper.LOADEDMUSIC_TBL, null);
		while (cursor.moveToNext()) {
			Music music = new Music();
			music.setId(cursor.getInt(cursor.getColumnIndex("_id")));
			music.setMusicName(cursor.getString(cursor
					.getColumnIndex("musicname")));
			music.setSinger(cursor.getString(cursor.getColumnIndex("singer")));
			music.setSavePath(cursor.getString(cursor.getColumnIndex("_data")));
			musics.add(music);
		}
		cursor.close();
		db.close();
		return musics;
	}

	/**
	 * ��ҳ��ѯ
	 * 
	 * @param currentPage
	 *            ��ǰҳ��
	 * @param pageSize
	 *            ÿҳ����
	 * @return
	 */
	public ArrayList<Music> getPageData(int currentPage, int pageSize) {
		ArrayList<Music> musics = new ArrayList<Music>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from "
				+ DBOpenhelper.LOADEDMUSIC_TBL + " limit ?,?",
				new String[] { String.valueOf((currentPage - 1) * pageSize),
						String.valueOf(pageSize) });
		while (cursor.moveToNext()) {
			Music music = new Music();
			music.setId(cursor.getInt(cursor.getColumnIndex("_id")));
			music.setMusicName(cursor.getString(cursor
					.getColumnIndex("musicname")));
			music.setSinger(cursor.getString(cursor.getColumnIndex("singer")));
			music.setSavePath(cursor.getString(cursor.getColumnIndex("_data")));

			musics.add(music);
		}
		cursor.close();
		db.close();
		return musics;
	}
}
