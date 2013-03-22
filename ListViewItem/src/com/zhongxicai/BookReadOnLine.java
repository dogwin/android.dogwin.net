package com.zhongxicai;

import java.util.ArrayList;
import java.util.List;

import com.zhongxicai.AllListener.OnReadOnlineBottonBarListener;
import com.zhongxicai.AllListener.OnReadOnlineTextBarListener;
import com.zhongxicai.Beans.ContentDialog;
import com.zhongxicai.Beans.ContentDialog.OnContentItemListener;
import com.zhongxicai.Beans.ReadOnlineBottomBar;
import com.zhongxicai.Beans.TextBar;
import com.zhongxicai.Beans.TopBar;
import com.zhongxicai.UrlDownLoad.AsyncContentLoader;
import com.zhongxicai.UrlDownLoad.AsyncContentLoader.OnAsyncContentLoaderListener;
import com.zhongxicai.UrlDownLoad.AsyncTextLoader;
import com.zhongxicai.UrlDownLoad.AsyncTextLoader.OnAsyncTextLoaderListener;
import com.zhongxicai.UrlDownLoad.BookHttpDownload;
import com.zhongxicai.XMLPullParser.PullServiceLoadContent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.zhongxicai.Beans.BookContent;

public class BookReadOnLine extends Activity  implements OnReadOnlineBottonBarListener,OnReadOnlineTextBarListener{
	ReadOnlineBottomBar bottombar;
	TextBar textbar;
	TopBar topbar;
	
	String urlstr;
	String string;
	String contenturlstr;
	String text;
	boolean pageexist;
	int  pageid;
	int barshowing;
	BookHttpDownload loadNetString;
	AsyncTextLoader loadText;
	AsyncContentLoader loadContent;
	ContentDialog contentdialog;
	PullServiceLoadContent pullloadcontent;
	ContentItemListener  contentItemListener ;
	final int TEXTLOADED=1;
	final int CONTENTLODED=2;
	List<BookContent> content;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.readonline);
		bottombar=(ReadOnlineBottomBar)findViewById(R.id.readonlinebottombar);
		textbar=(TextBar)findViewById(R.id.readonlinetextbar);
		topbar=(TopBar)findViewById(R.id.readonlinetopbar);
		textbar.setOnReadOnlineTextBarListener(this);
		bottombar.setOnReadOnlineBottonBarListener(this);
	   loadNetString=new BookHttpDownload(); 
		Bundle bundle=this.getIntent().getExtras();
		urlstr=bundle.getString("url");
		pageid=bundle.getInt("pageid");	
	    contenturlstr=bundle.getString("contenturl");
	    content=new ArrayList<BookContent>();
	    barshowing=1;      
	    loadText=new AsyncTextLoader(new AsyncTextLoaderListener()); 
	    loadText.execute(urlstr);  
	    contentItemListener =new ContentItemListener();
	    topbar.setTitleText("第"+pageid+"章");
	}

	
	  // <----------------bottombar监听------------------------>  
	/**
	 * 上一章按钮响应---》从网络获取上一章内容
	 * */
	public void onClickPrepagebtn(View v) {
		String chapterdir=urlstr.substring(0,urlstr.lastIndexOf("/")+1);	
		    pageid--;
			urlstr=chapterdir+pageid+".txt";
			if(loadNetString.getInputStream(urlstr)==null){
				pageid++;
				 showToast("前面木有了。。。");
			}
			else{
			loadText=new AsyncTextLoader(new AsyncTextLoaderListener()); 
			loadText.execute(urlstr);   
			topbar.setTitleText("第"+pageid+"章");
			}
			
	}
	/**
	 * 下一章按钮响应----》从网络获取下一章内容
	 * */
	public void onCLickNextpagebtn(View v) {
		String chapterdir=urlstr.substring(0,urlstr.lastIndexOf("/")+1);
		     pageid++;
			 urlstr=chapterdir+pageid+".txt";
			 if(loadNetString.getInputStream(urlstr)==null){
				 pageid--;
				 showToast("后面木有了。。。");
				}
			 else{
					loadText=new AsyncTextLoader(new AsyncTextLoaderListener()); 
					loadText.execute(urlstr);   
					topbar.setTitleText("第"+pageid+"章");
					}
			
	}
	/**
	 * 目录按钮响应---》获取目录
	 * */
	public void onClickContentbtn(View v) {
       if(content.size()==0){
    	   loadContent=new AsyncContentLoader(new AsyncContentLoaderListener());
    	   loadContent.execute(contenturlstr);
       }
       else{
    	   showContentDialog(content);
       }
       
	}
	/**
	 * 点击章节目录---》跳转到指定章节
	 * */
	public void turnToChapter(int pageid,String urlstr){
			this.urlstr=urlstr;
			this.pageid=pageid;
			 loadText=new AsyncTextLoader(new AsyncTextLoaderListener()); 
			    loadText.execute(urlstr); 
	}
	/**
	 * Toast显示没有章节
	 * */
	public void showToast(String str){
    	Toast.makeText(this,str, Toast.LENGTH_SHORT).show();
    }
    //<---------------- textbar监听---------------->
	
	/**
	 * 屏幕中间按钮响应
	 * */
	public void onClickMiddlebtnListener(View v) {
	 switch(barshowing){
	 case 0:
		 showbar();
		 break;
	 case 1:
		 hidebar();
		 break;
	 }
	}
	 /**
	 * textbar控件--->屏幕文本响应
	 * */
	public void onClickTextListener(View v) {
		hidebar();
	}
	
	 /**
	 * textbar控件--->屏幕文本响应
	 * */
	public void onTouchTextListener(View v) {
		hidebar();
	}
	//<--------------被调用的方法和类------------------>
	/**
	 * 显示上下两工具条
	 * */
	public void showbar(){
		topbar.setVisibility(View.VISIBLE);
		bottombar.setVisibility(View.VISIBLE);
		barshowing=1;
	}
	/**
	 * 隐藏上下两工具条
	 * */
	public void hidebar(){
		topbar.setVisibility(View.GONE);
		bottombar.setVisibility(View.GONE);
		barshowing=0;
	}

	/**
	 * 显示目录列表
	 * */
	public void showContentDialog(List<BookContent> content){
		contentdialog=new ContentDialog(this,content,contentItemListener);
        contentdialog.show(); 
	}
	/**
	 * 异步下载文本的监听
	 * */
	class AsyncTextLoaderListener implements OnAsyncTextLoaderListener{
		private  ProgressDialog pdialog;
		public void onPreLoader() {
			pdialog=new ProgressDialog(BookReadOnLine.this);
			pdialog.setTitle("加载中...");
			pdialog.show();		
		}
		public void onOverLoader(String result) {
			pdialog.cancel();
			textbar.setBarText(result);
		}
		public void onProgressLoader() {
	
		}
		
	}
	/**
	 * 异步下载目录的监听
	 * */
	class AsyncContentLoaderListener implements OnAsyncContentLoaderListener{
		private  ProgressDialog pdialog;
		public void onPreLoader() {
			pdialog=new ProgressDialog(BookReadOnLine.this);
			pdialog.setTitle("初始化目录中...");
			pdialog.show();	
		}

		public void onOverLoader(List<BookContent> content) {
			pdialog.cancel();
			BookReadOnLine.this.content=content;
			showContentDialog(content); 
		}

		public void onProgressLoader() {
			// TODO Auto-generated method stub
			
		}
		
	}
	/**
	 * 点击目录响应监听
	 * */
	class  ContentItemListener  implements OnContentItemListener{
		public void onContentItemClickListener(int position) {
		  String urlstr=content.get(position).getChapterUrl();
		  turnToChapter(position+1,urlstr);
		  topbar.setTitleText("第"+pageid+"章");
		  contentdialog.dismiss();
		}	
	}
	
}