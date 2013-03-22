package com.zhongxicai;

import java.util.ArrayList;
import java.util.List;

import com.zhongxicai.UrlDownLoad.FileDownload;
import com.zhongxicai.UrlDownLoad.FileDownload.OnDownProgressListener;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class DownLoadService extends Service{
    private DownFileThread downFileThread;
    private List<DownFileThread>  threadlist;
    private MyBinder myBinder=new MyBinder();
    private List<String> waitlist;
    private int threadCountMax=3;//最多支持文件数量同时下载---可以自己定义

    //绑定成功时，调用该方法
	@Override
	public IBinder onBind(Intent intent) {
		Log.d("DownLoadService", "onBind");
		return myBinder;
	}
	//重新绑定时调用该方法
	@Override
	public void onRebind(Intent intent){
		Log.d("DownLoadService", "onRebind");
		super.onRebind(intent);
	}
	//解除绑定时调用该方法
	@Override
	public boolean onUnbind(Intent intent){
		Log.d("DownLoadService", "onUnbind");
		return super.onUnbind(intent);
	}
   public void onCreate(){
	   Log.i("DownLoadService", "onCreate");
	   super.onCreate();	
	   threadlist=new ArrayList<DownFileThread>();
	   waitlist=new ArrayList<String>();
   }
   public void onDestroy(){
	   Log.i("DownLoadService", "onDestroy");
	   super.onDestroy();
   }
   public void onStart(Intent intent, int startId){
	   super.onStart(intent, startId);
	   Log.i("DownLoadService", "onStart");
	   Bundle bundle=intent.getExtras();
	   String msg=bundle.getString("urlstr");
	   if(msg!=null){
		   waitlist.add(msg);
		   Log.i("等待下载", String.valueOf(waitlist.size()));
		   addThread();
	   }
	   }
   
   ////////
  public  class MyBinder extends Binder{
	   DownLoadService getService(){
		return DownLoadService.this;  
	   }
   }
  /**
   * 下载文件的线程
   * */
   class DownFileThread extends Thread{
      String urlstr;
  	 public  DownFileThread(String urlstr){
  		this.urlstr=urlstr;
  				}
     	public void run(){
  		     FileDownload filedownload=new FileDownload();
  		     filedownload.downLoadFile(urlstr, new DownProgressListener()); 
  		     Log.i("完成一个下载","完成一个下载");
  		     threadlist.remove(this);	
  		     Log.i("剩余下载线程", String.valueOf(threadlist.size()));
  		    addThread();//下载完成后，判断是否有等待的线程，如果没有，则结束，否则，添加一个下载线程
  		   
         	}
         }
   /**
    * 添加下载文件的线程
    * 在添加下载或者有文件下载完成时被调用执行
    * */
   public void addThread(){
	   Log.i("是否添加下载线程","是/否");
	  if(waitlist.size()!=0 && threadlist.size()<threadCountMax){
		  Log.i("是否添加下载线程", "是");
		  downFileThread=new  DownFileThread(waitlist.get(0));//添加第一个等待下载的文件
		  threadlist.add(downFileThread);//将线程添到下载文件线程list中
		  waitlist.remove(0);//把添加下载的文件从待下载中删除
		  Log.i("下载线程", String.valueOf(threadlist.size()));
		  Log.i("等待下载", String.valueOf(waitlist.size()));
		  downFileThread.start();//开始下载
	  } 
	  else{ 
		  Log.i("是否添加下载线程","否");
	   } 
   }
    /**
     * 下载进度监听重写方法-----
     * */
    class DownProgressListener implements  OnDownProgressListener{
	public void onProgressListener(float progress) {
		//Log.i("下载进度", String.valueOf(progress));
	}	   
   }
}











