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
    private int threadCountMax=3;//���֧���ļ�����ͬʱ����---�����Լ�����

    //�󶨳ɹ�ʱ�����ø÷���
	@Override
	public IBinder onBind(Intent intent) {
		Log.d("DownLoadService", "onBind");
		return myBinder;
	}
	//���°�ʱ���ø÷���
	@Override
	public void onRebind(Intent intent){
		Log.d("DownLoadService", "onRebind");
		super.onRebind(intent);
	}
	//�����ʱ���ø÷���
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
		   Log.i("�ȴ�����", String.valueOf(waitlist.size()));
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
   * �����ļ����߳�
   * */
   class DownFileThread extends Thread{
      String urlstr;
  	 public  DownFileThread(String urlstr){
  		this.urlstr=urlstr;
  				}
     	public void run(){
  		     FileDownload filedownload=new FileDownload();
  		     filedownload.downLoadFile(urlstr, new DownProgressListener()); 
  		     Log.i("���һ������","���һ������");
  		     threadlist.remove(this);	
  		     Log.i("ʣ�������߳�", String.valueOf(threadlist.size()));
  		    addThread();//������ɺ��ж��Ƿ��еȴ����̣߳����û�У���������������һ�������߳�
  		   
         	}
         }
   /**
    * ��������ļ����߳�
    * ��������ػ������ļ��������ʱ������ִ��
    * */
   public void addThread(){
	   Log.i("�Ƿ���������߳�","��/��");
	  if(waitlist.size()!=0 && threadlist.size()<threadCountMax){
		  Log.i("�Ƿ���������߳�", "��");
		  downFileThread=new  DownFileThread(waitlist.get(0));//��ӵ�һ���ȴ����ص��ļ�
		  threadlist.add(downFileThread);//���߳��������ļ��߳�list��
		  waitlist.remove(0);//��������ص��ļ��Ӵ�������ɾ��
		  Log.i("�����߳�", String.valueOf(threadlist.size()));
		  Log.i("�ȴ�����", String.valueOf(waitlist.size()));
		  downFileThread.start();//��ʼ����
	  } 
	  else{ 
		  Log.i("�Ƿ���������߳�","��");
	   } 
   }
    /**
     * ���ؽ��ȼ�����д����-----
     * */
    class DownProgressListener implements  OnDownProgressListener{
	public void onProgressListener(float progress) {
		//Log.i("���ؽ���", String.valueOf(progress));
	}	   
   }
}











