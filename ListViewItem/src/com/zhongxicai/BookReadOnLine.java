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
	    topbar.setTitleText("��"+pageid+"��");
	}

	
	  // <----------------bottombar����------------------------>  
	/**
	 * ��һ�°�ť��Ӧ---���������ȡ��һ������
	 * */
	public void onClickPrepagebtn(View v) {
		String chapterdir=urlstr.substring(0,urlstr.lastIndexOf("/")+1);	
		    pageid--;
			urlstr=chapterdir+pageid+".txt";
			if(loadNetString.getInputStream(urlstr)==null){
				pageid++;
				 showToast("ǰ��ľ���ˡ�����");
			}
			else{
			loadText=new AsyncTextLoader(new AsyncTextLoaderListener()); 
			loadText.execute(urlstr);   
			topbar.setTitleText("��"+pageid+"��");
			}
			
	}
	/**
	 * ��һ�°�ť��Ӧ----���������ȡ��һ������
	 * */
	public void onCLickNextpagebtn(View v) {
		String chapterdir=urlstr.substring(0,urlstr.lastIndexOf("/")+1);
		     pageid++;
			 urlstr=chapterdir+pageid+".txt";
			 if(loadNetString.getInputStream(urlstr)==null){
				 pageid--;
				 showToast("����ľ���ˡ�����");
				}
			 else{
					loadText=new AsyncTextLoader(new AsyncTextLoaderListener()); 
					loadText.execute(urlstr);   
					topbar.setTitleText("��"+pageid+"��");
					}
			
	}
	/**
	 * Ŀ¼��ť��Ӧ---����ȡĿ¼
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
	 * ����½�Ŀ¼---����ת��ָ���½�
	 * */
	public void turnToChapter(int pageid,String urlstr){
			this.urlstr=urlstr;
			this.pageid=pageid;
			 loadText=new AsyncTextLoader(new AsyncTextLoaderListener()); 
			    loadText.execute(urlstr); 
	}
	/**
	 * Toast��ʾû���½�
	 * */
	public void showToast(String str){
    	Toast.makeText(this,str, Toast.LENGTH_SHORT).show();
    }
    //<---------------- textbar����---------------->
	
	/**
	 * ��Ļ�м䰴ť��Ӧ
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
	 * textbar�ؼ�--->��Ļ�ı���Ӧ
	 * */
	public void onClickTextListener(View v) {
		hidebar();
	}
	
	 /**
	 * textbar�ؼ�--->��Ļ�ı���Ӧ
	 * */
	public void onTouchTextListener(View v) {
		hidebar();
	}
	//<--------------�����õķ�������------------------>
	/**
	 * ��ʾ������������
	 * */
	public void showbar(){
		topbar.setVisibility(View.VISIBLE);
		bottombar.setVisibility(View.VISIBLE);
		barshowing=1;
	}
	/**
	 * ����������������
	 * */
	public void hidebar(){
		topbar.setVisibility(View.GONE);
		bottombar.setVisibility(View.GONE);
		barshowing=0;
	}

	/**
	 * ��ʾĿ¼�б�
	 * */
	public void showContentDialog(List<BookContent> content){
		contentdialog=new ContentDialog(this,content,contentItemListener);
        contentdialog.show(); 
	}
	/**
	 * �첽�����ı��ļ���
	 * */
	class AsyncTextLoaderListener implements OnAsyncTextLoaderListener{
		private  ProgressDialog pdialog;
		public void onPreLoader() {
			pdialog=new ProgressDialog(BookReadOnLine.this);
			pdialog.setTitle("������...");
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
	 * �첽����Ŀ¼�ļ���
	 * */
	class AsyncContentLoaderListener implements OnAsyncContentLoaderListener{
		private  ProgressDialog pdialog;
		public void onPreLoader() {
			pdialog=new ProgressDialog(BookReadOnLine.this);
			pdialog.setTitle("��ʼ��Ŀ¼��...");
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
	 * ���Ŀ¼��Ӧ����
	 * */
	class  ContentItemListener  implements OnContentItemListener{
		public void onContentItemClickListener(int position) {
		  String urlstr=content.get(position).getChapterUrl();
		  turnToChapter(position+1,urlstr);
		  topbar.setTitleText("��"+pageid+"��");
		  contentdialog.dismiss();
		}	
	}
	
}