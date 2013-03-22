package com.zhongxicai.Beans;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class BookRank {
	
	 int rankNum;
	 String name;
	 String author;
	 int clickNum;
	 int downNum;
	 String imgUrl;
	 String downLoadUrl;
	 String readOnlineUrl;
	 String contentUrl;
	 Bitmap bitmap;
	 Drawable drawable;
	 
	public BookRank(){
		super();
	}

	public BookRank(int rankNum,String name, Bitmap bitmap,String author,int clickNum,int downNum,String imgUrl,String  downLoadUrl,String  readOnlineUrl,String contentUrl){
		        this.author=author;
				this.clickNum=clickNum;
				this.downLoadUrl=downLoadUrl;
				this.downNum=downNum;
				this.name=name;
				this.readOnlineUrl=readOnlineUrl;
				this.imgUrl=imgUrl;
				this.rankNum=rankNum;
				this.bitmap=bitmap;
				this.contentUrl=contentUrl;
	}
	public BookRank(int rankNum,String name, Drawable drawable,String author,int clickNum,int downNum,String imgUrl,String  downLoadUrl,String  readOnlineUrl,String contentUrl){
        this.author=author;
		this.clickNum=clickNum;
		this.downLoadUrl=downLoadUrl;
		this.downNum=downNum;
		this.name=name;
		this.readOnlineUrl=readOnlineUrl;
		this.imgUrl=imgUrl;
		this.rankNum=rankNum;
		this.drawable=drawable;
		this.contentUrl=contentUrl;
}
	
	public String getcontentUrl(){
		return contentUrl;
	}
	
	/**
	 * ȡ��������
	 * */
	public String getauthor(){
		return author;
	}
	/**
	 * ȡ�õ����
	 * */
	public int getclickNum(){
		return clickNum;
	}
	/**
	 * ȡ�����ص�ַ
	 * */
	public String getdownLoadUrl(){
		return downLoadUrl;
	}
	/**
	 * ȡ��������
	 * */
	public int getdownNum(){
		return downNum;
	}
	/**
	 * ȡ������
	 * */
	public String getname(){
		return name;
	}
	/**
	 * ȡ���Ķ���ַ
	 * */
	public String getreadOnlineUrl(){
		return readOnlineUrl;
	}
	/**
	 * ȡ�÷���ͼƬ��ַ
	 * */
	public String getimgUrl(){
		return imgUrl;
	}
	/**
	 * ȡ�÷���ͼƬ
	 * */
	public Bitmap getbitmap (){
		return bitmap;
	}
	/**
	 * ȡ�÷���ͼƬ
	 * */
	public Drawable getdrawable (){
		return drawable;
	}
	/**
	 * ȡ����������
	 * */
	public int getrankNum(){
		return rankNum;
	}
	
	/**
	 * ����������
	 * */
	public void setauthor(String author){
		this.author=author;
	}
	/**
	 * ���õ����
	 * */
	public void setclickNum(int clickNum){
		this.clickNum=clickNum;
	}
	/**
	 * �������ص�ַ
	 * */
	public void setdownLoadUrl(String downLoadUrl){
		this.downLoadUrl=downLoadUrl;
	}
	/**
	 *���������� 
	 * */
	public void setdownNum(int downNum){
		this.downNum=downNum;
	}
	/**
	 * ��������
	 * */
	public void setname(String name){
		this.name=name;
	}
	/**
	 * �����Ķ���ַ
	 * */
	public void setreadOnlineUrl(String readOnlineUrl){
		this.readOnlineUrl=readOnlineUrl;
	}
	/**
	 * ���÷���ͼƬ��ַ
	 * */
	public void setimgUrl(String imgUrl){
		this.imgUrl=imgUrl;
	}
	/**
	 * ���÷���ͼƬ
	 * */
	public void setbitmap(Bitmap bitmap){
		this.bitmap=bitmap;
	}
	/**
	 * ���÷���ͼƬ
	 * */
	public void setdrawable(Drawable drawable){
		this.drawable=drawable;
	}
	/**
	 * ������������
	 * */
	public void setrankNum(int rankNum){
		this.rankNum=rankNum;
	}
   public void setcontentUrl(String contentUrl){
	   this.contentUrl=contentUrl;
   }
}
