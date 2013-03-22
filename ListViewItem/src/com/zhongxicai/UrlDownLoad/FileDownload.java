package com.zhongxicai.UrlDownLoad;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import com.zhongxicai.Beans.BookRank;
import com.zhongxicai.Beans.DownloadInfo;
import com.zhongxicai.UrlDownLoad.BookHttpDownload;
import android.os.Environment;
import android.util.Log;

public class FileDownload {
	
	
	private  BookHttpDownload httpload;
	
	public FileDownload(){
		super();
	}

	//�жϴ洢�ļ���·���Ƿ���ڣ��������򴴽�
	public File createFilePath(String dirpath){
	   File saveDir=null;
	   File sdCardDir=null;
	   sdCardDir = Environment.getExternalStorageDirectory();// ��ȡ�ⲿ�洢Ŀ¼
	   saveDir=new File(sdCardDir.getAbsolutePath()+dirpath);
		if(!saveDir.exists()){
			try{
			saveDir.mkdirs();
		  }catch(Exception e){
				return null;	
			}
		}
		 Log.i("dirpath", "success");
		return saveDir;
	}
	//�����ļ�
	public File createlocalfile(String dirpath,String filename){
		 File saveDir=null;
		 File file=null;
		 saveDir=createFilePath(dirpath);
		 if(saveDir.exists()){
		    file=new File(saveDir.getAbsolutePath()+"/"+filename);
		    if(!file.exists()){
		    	Log.i("createfile", "start");
		    	try{
			        file.createNewFile();
				}catch(Exception e){
					Log.i("createfile", "failed");
					return null;
				}
				Log.i("createfile", "success");
				return file;
		    }		
		    else{
		    	Log.i("createfile", "failed--already_exist");
		    }
		}
	  return null;
	}
    /**
     * ��ȡ���ص������Ϣ
     * */
   public DownloadInfo getHttpFileInfo(String urlstr){  
	   DownloadInfo fileinfo=new DownloadInfo();
	   String filename=null;
	   String filetype=null;
	   String dirpath=null;
	   
	   filename=urlstr.substring(urlstr.lastIndexOf("/")+1,urlstr.length());
	   filetype=urlstr.substring(urlstr.lastIndexOf(".")+1,urlstr.length()).toLowerCase();
	   dirpath=getDirPath(filetype);
   
	   fileinfo.seturlstr(urlstr);
	   fileinfo.setdirpath(dirpath);
	   fileinfo.setfilename(filename);
	   fileinfo.setfiletype(filetype);
	   
	   return fileinfo;
   }
   /**
    * �����ļ����ͣ��µ���ͬ���ļ�����
    * */
   public String getDirPath(String filetype){
	   if(filetype.equals("txt")){
		   return  "/cmread/download/books";
	   }else if(filetype.equals("mp3")||filetype.equals("wav")||filetype.equals("ogg")||filetype.equals("mid")){
		   return  "/cmread/download/musics";
	   }else if(filetype.equals("mp4")||filetype.equals("rmvb")||filetype.equals("3gp")||filetype.equals("rm")){
		   return  "/cmread/download/videos";
	   }else if(filetype.equals("jpg")||filetype.equals("png")||filetype.equals("bmp")||filetype.equals("jepg")){
		   return  "/cmread/download/pictures";
	   }else {
		   return "/cmread/download/others";
	   }
   }
   
	// �����ļ���sdcard
	public void downLoadFile(String urlstr,OnDownProgressListener downlistener) {
	  DownloadInfo fileinfo=null;
	  float file_length=0;
	  float total_length=0;
	  InputStream is = null;
	  httpload=new BookHttpDownload();
	  URL infoUrl = null;
	  URLConnection connection=null;
	  HttpURLConnection httpConnection=null; 
	  FileOutputStream out = null;
	  int bufferLength = -1;
	  byte[] buffer = new byte[1024];
	  File saveFile=null;
	  //��ȡ���ص��ļ�����Ϣ
	  fileinfo=getHttpFileInfo(urlstr);
	  //��������
	  try{
	  infoUrl = new URL(fileinfo.geturlstr());
	  connection = infoUrl.openConnection();
	  httpConnection = (HttpURLConnection) connection;
	  total_length=httpConnection.getContentLength();
	  }
	  catch(Exception e){
		  Log.i("connect", "wrong");
	  }
	  //��ȡ�������ݲ����浽�����ļ���
	  try{
	       is=httpload.getInputStream(fileinfo.geturlstr());
	       saveFile=createlocalfile(fileinfo.getdirpath(),fileinfo.getfilename());
	       out = new FileOutputStream(saveFile);
	       while ((bufferLength = is.read(buffer)) > 0) {
	           out.write(buffer, 0, bufferLength);
	           file_length += bufferLength; 
	           downlistener.onProgressListener(file_length/total_length);//���ȼ���
               }
	  }catch(Exception e){
		  Log.i("write", "wrong");
	  }
	  //�ر�
	  try{
	    out.close();
	    is.close();
	   }catch(Exception e){	   
		   Log.i("close", "wrong");
	   }
	  }
	/**
	 * ���ؽ��ȼ����ӿ�---0-1.0
	 * */
	public interface OnDownProgressListener{
		public void onProgressListener(float progress);
	}
	
}