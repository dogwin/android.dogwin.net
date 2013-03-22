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
	 * 取得作者名
	 * */
	public String getauthor(){
		return author;
	}
	/**
	 * 取得点击量
	 * */
	public int getclickNum(){
		return clickNum;
	}
	/**
	 * 取得下载地址
	 * */
	public String getdownLoadUrl(){
		return downLoadUrl;
	}
	/**
	 * 取得下载量
	 * */
	public int getdownNum(){
		return downNum;
	}
	/**
	 * 取得书名
	 * */
	public String getname(){
		return name;
	}
	/**
	 * 取得阅读地址
	 * */
	public String getreadOnlineUrl(){
		return readOnlineUrl;
	}
	/**
	 * 取得封面图片地址
	 * */
	public String getimgUrl(){
		return imgUrl;
	}
	/**
	 * 取得封面图片
	 * */
	public Bitmap getbitmap (){
		return bitmap;
	}
	/**
	 * 取得封面图片
	 * */
	public Drawable getdrawable (){
		return drawable;
	}
	/**
	 * 取得排行名次
	 * */
	public int getrankNum(){
		return rankNum;
	}
	
	/**
	 * 设置作者名
	 * */
	public void setauthor(String author){
		this.author=author;
	}
	/**
	 * 设置点击量
	 * */
	public void setclickNum(int clickNum){
		this.clickNum=clickNum;
	}
	/**
	 * 设置下载地址
	 * */
	public void setdownLoadUrl(String downLoadUrl){
		this.downLoadUrl=downLoadUrl;
	}
	/**
	 *设置下载量 
	 * */
	public void setdownNum(int downNum){
		this.downNum=downNum;
	}
	/**
	 * 设置书名
	 * */
	public void setname(String name){
		this.name=name;
	}
	/**
	 * 设置阅读地址
	 * */
	public void setreadOnlineUrl(String readOnlineUrl){
		this.readOnlineUrl=readOnlineUrl;
	}
	/**
	 * 设置封面图片地址
	 * */
	public void setimgUrl(String imgUrl){
		this.imgUrl=imgUrl;
	}
	/**
	 * 设置封面图片
	 * */
	public void setbitmap(Bitmap bitmap){
		this.bitmap=bitmap;
	}
	/**
	 * 设置封面图片
	 * */
	public void setdrawable(Drawable drawable){
		this.drawable=drawable;
	}
	/**
	 * 设置排行名次
	 * */
	public void setrankNum(int rankNum){
		this.rankNum=rankNum;
	}
   public void setcontentUrl(String contentUrl){
	   this.contentUrl=contentUrl;
   }
}
