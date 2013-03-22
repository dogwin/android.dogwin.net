package com.zhongxicai.UrlDownLoad;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;

import android.util.Log;

public class FileDownloadThread extends Thread{
	private static final int BUFFER_SIZE=1024;
	private URL url;
	private int startPosition;
	private int endPosition;
	private int curPosition;
	private boolean finished=false;
	private int downloadSize=0;
	private File file;
	
	public FileDownloadThread(URL url,File file,int startPosition,int endPosition){
		this.url=url;
		this.file=file;
		this.startPosition=startPosition;
		this.endPosition=endPosition;
		this.curPosition=startPosition;
	}
	@Override
	public void run(){
		BufferedInputStream bis=null;
		RandomAccessFile fos=null;
		byte[] buf=new byte[BUFFER_SIZE];
		URLConnection con=null;
		try{
		con=url.openConnection();
		con.setAllowUserInteraction(true);
		con.setRequestProperty("Range","bytes="+ startPosition+"-"+endPosition);
		fos=new RandomAccessFile(file,"rw");
		fos.seek(startPosition);
		bis=new BufferedInputStream(con.getInputStream());
	      while(curPosition<endPosition){
	    	  int len=bis.read(buf, 0, BUFFER_SIZE);
	    	  if(len==-1){
	    		  break;
	    	  }
	    	  fos.write(buf, 0, len);
	    	  curPosition=curPosition+len;
	    	  if( curPosition>endPosition){
	    		  downloadSize+=len-(curPosition-endPosition)+1;
	    	  }else{
	    		  downloadSize+=len;
	    	  }
	      }
	this.finished=true;
	bis.close();
	fos.close();
		}catch(IOException e){
			Log.d(getName() +" Error:", e.getMessage());
		}
	}
	
	public boolean isFinished(){
		return finished;
	}
	public int getDownloadSize(){
		return downloadSize;
	}
	
}

















