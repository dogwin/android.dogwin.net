package com.tarena.fashionmusic.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

public class MusicPreference {

	SharedPreferences sharedPreferences;

	public MusicPreference(Context context) {
		sharedPreferences = context.getSharedPreferences("music_preference",
				Context.MODE_PRIVATE);
	}

	/**
	 * ��������˳�ʱ�� ����λ��
	 * 
	 * @param context
	 * @param position
	 */
	public void savaPlayPosition(Context context, int position) {
		sharedPreferences.edit().putInt("position", position).commit();
	}

	/**
	 * ��ȡ�˳�ʱ�Ĳ���λ��
	 * 
	 * @param context
	 * @return
	 */
	public int getsaveposition(Context context) {
		return sharedPreferences.getInt("position", 0);
	}

	/**
	 * ���� ����ģʽ
	 * 
	 * @param context
	 * @param playmode
	 *            0 ˳�򲥷� 1 ������� 2 ����ѭ��
	 */
	public void savaPlayMode(Context context, int playmode) {
		sharedPreferences.edit().putInt("playmode", playmode).commit();
	}

	/**
	 * ��ȡ����ģʽ
	 * 
	 * @param context
	 * @return int playmode 0 ˳�򲥷� 1 ������� 2 ����ѭ��
	 */
	public int getPlayMode(Context context) {
		return sharedPreferences.getInt("playmode", 0);
	}

	/**
	 * �����ʴ�С
	 * 
	 * @param context
	 * @param lrc_size
	 *            ��ʴ�С
	 */
	public void savaLrcSize(Context context, int lrc_size) {
		sharedPreferences.edit().putInt("lrc_size", lrc_size).commit();
	}

	/**
	 * ��ȡ��ʴ�С
	 * 
	 * @param context
	 * @return int lrc_size ��ʴ�С
	 */
	public int getLrcSize(Context context) {
		return sharedPreferences.getInt("lrc_size", 22);
	}

	/**
	 * ��������ɫ
	 * 
	 * @param context
	 * @param lrc_color�����ɫ
	 */
	public void savaLrcColor(Context context, int lrc_color) {
		sharedPreferences.edit().putInt("lrc_color", lrc_color).commit();
	}

	/**
	 * ��ȡ�����ɫС
	 * 
	 * @param context
	 * @return int lrc_color �����ɫ
	 */
	public int getLrcColor(Context context) {
		return sharedPreferences.getInt("lrc_color", Color.rgb(51, 181, 229));
	}

	/**
	 * ����Ĭ�ϱ�ǩ����
	 * 
	 * @param context
	 * @param tab_count��ǩ����
	 */
	public void savaTagCount(Context context, int tab_count) {
		sharedPreferences.edit().putInt("tab_count", tab_count).commit();
	}

	/**
	 * ��ȡĬ�ϱ�ǩ����
	 * @param context
	 * @return int tab_count ��ǩ����
	 */
	public int getTagCount(Context context) {
		return sharedPreferences.getInt("tab_count", 10);
	}

	/**
	 * ��ȡ ��ǩ ѡ���Ĭ��λ��
	 * @param data
	 * @param context
	 * @return
	 */
	public int getTagPos(String[] data, Context context) {
		int n = 0;
		for (int i = 0; i < data.length; i++) {
			if (String.valueOf(getTagCount(context)).equals(data[i])) {
				n = i;
				break;
			}
		}
		return n;
	}
}
