package com.tarena.fashionmusic.db;

import java.util.ArrayList;

import com.tarena.fashionmusic.entry.MusicItem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MusicItemDao {
	private DBOpenhelper helper;

	public MusicItemDao(Context context) {
		this.helper = new DBOpenhelper(context);
	}

	/**
	 * ���������Ŀ
	 */
	public long addMusicItem(MusicItem item) {
		long rowId = -1;
		if (!exists(item.getGroupId(), item.getMusicId())) {
			SQLiteDatabase db = helper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("musicid", item.getMusicId());
			values.put("groupid", item.getGroupId());
			db.insert(helper.MUSICITEM_TBL, null, values);
			db.close();
		}
		return rowId;
	}

	/**
	 * ɾ��������Ŀ
	 */
	public int deleteItemById(int id){
		int count = 0;
		SQLiteDatabase db = helper.getWritableDatabase();
		count = db.delete(helper.MUSICITEM_TBL, "_id=" + id, null);
		db.close();
		return count;
	}

	/**
	 * ����musicIdɾ��������Ŀ
	 */
	public int deleteItemByMusicid(int musicId){
		int count = 0;
		SQLiteDatabase db = helper.getWritableDatabase();
		count = db.delete(helper.MUSICITEM_TBL, "musicid=" + musicId, null);
		db.close();
		return count;
	}
	/**
	 * ���ݷ���idɾ��������Ŀ
	 */
	public int deleteItemByGroupid(int groupId){
		int count = 0;
		SQLiteDatabase db = helper.getWritableDatabase();
		count = db.delete(helper.MUSICITEM_TBL, "groupid=" + groupId, null);
		db.close();
		return count;
	}
	/**
	 * ����groupid��ѯ������Ŀ
	 */
	public ArrayList<MusicItem> getMusicsByGroup(int groupId){
		ArrayList<MusicItem> items = new ArrayList<MusicItem>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from " + helper.MUSICITEM_TBL + " where groupid=" + groupId, null);
		while(c.moveToNext()){
			MusicItem item = new MusicItem();
			item.setId(c.getInt(c.getColumnIndex("_id")));
			item.setMusicId(c.getInt(c.getColumnIndex("musicid")));
			item.setGroupId(groupId);
			items.add(item);
		}
		c.close();
		db.close();
		return items;
	}

	/**
	 * �жϸ÷����Ƿ��Ѿ����ڸ�������Ŀ
	 */
	public boolean exists(int groupId, int musicId) {
		boolean isExists = false;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db
				.rawQuery("select * from " + helper.MUSICITEM_TBL
						+ " where groupid=? and musicid=?", new String[] {
						String.valueOf(groupId), String.valueOf(musicId) });
		if (c.moveToNext()) {
			isExists = true;
		}
		c.close();
		db.close();
		return isExists;
	}
}
