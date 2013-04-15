package com.tarena.fashionmusic.db;

import java.util.ArrayList;

import com.tarena.fashionmusic.entry.MusicGroup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class MusicGroupDao {
	private DBOpenhelper helper;
	private Context context;
	public MusicGroupDao(Context context){
		this.helper = new DBOpenhelper(context);
		this.context = context;
	}
	/**
	 * ����·���
	 * @param group
	 * @return
	 */
	public long addGroup(MusicGroup group) {
		long rowId = 0;
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("title", group.getTitle());
		rowId = db.insert(DBOpenhelper.MUSICGROUP_TBL, null, values);
		db.close();
		return rowId;
	}
	
	/**
	 * �޸ķ�������
	 * @param title Ҫ�޸ĳɵı���
	 * @param id Ҫ�޸ĵ�id
	 * @return
	 */
	public long updateGroup(String title,int id) {
		long rowId = 0;
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("title", title);
		rowId=db.update(DBOpenhelper.MUSICGROUP_TBL, values, "_id=?", new String[]{String.valueOf(id)});
		db.close();
		return rowId;
	}
	
	/**
	 *  ɾ������
	 * @param groupId
	 * @return
	 */
	public int deleteGroup(int groupId){
		SQLiteDatabase db = helper.getWritableDatabase();
		db.beginTransaction();//��ʼ����
		db.delete(helper.MUSICITEM_TBL, "groupid=" + groupId, null);
		db.delete(helper.MUSICGROUP_TBL, "_id=" + groupId, null);
		db.setTransactionSuccessful();
		db.endTransaction();//��������
		db.close();
		return -1;
	}
	
	
	
	
	/**
	 * ��ѯ����
	 */
	public ArrayList<MusicGroup> getGroups(){
		ArrayList<MusicGroup> groups = new ArrayList<MusicGroup>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from " + helper.MUSICGROUP_TBL, null);
		while(c.moveToNext()){
			MusicGroup group = new MusicGroup();
			group.setTitle(c.getString(c.getColumnIndex("title")));
			group.setId(c.getInt(c.getColumnIndex("_id")));
			group.setItems(new MusicItemDao(context).getMusicsByGroup(group.getId()));
			groups.add(group);
		}
		c.close();
		db.close();
		return groups;
	}
	
	public int getDataCount(){
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from " + helper.MUSICGROUP_TBL, null);
		return c.getCount();
	}
	
}
