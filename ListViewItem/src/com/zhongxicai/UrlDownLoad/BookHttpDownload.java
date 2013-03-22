package com.zhongxicai.UrlDownLoad;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BookHttpDownload {

	/**
   	 * 由url-----》获得InputStream----可行
   	 * */
   	public InputStream getInputStream(String xmlUrlPath)
	{
	
		URL infoUrl = null;
		InputStream inStream = null;
		try
		{
			infoUrl = new URL(xmlUrlPath);
			URLConnection connection = infoUrl.openConnection();
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			int responseCode = httpConnection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK)
			{
				inStream = httpConnection.getInputStream();
			}
		}
		catch (MalformedURLException e)
		{
			return null;
		}
		catch (IOException e)
		{
			return null;
		}
		return inStream;
	}
   	/**
   	 * 由inputstream-----获得 byte[]
   	 * */
	public byte[] getBytesFromStream(InputStream inputStream) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int len = 0;
		while (len != -1) {
			try {
				len = inputStream.read(b);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (len != -1) {
				baos.write(b, 0, len);
			}
		}

		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return baos.toByteArray();
	}
   	/**
   	 * 由url----》获得byte[]
   	 * */
   	public byte[] getBytesFromUrl(String urlstr) {
		BookHttpDownload httpload=new BookHttpDownload();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		InputStream inputStream = null;
		int len = 0;
		while (len != -1) {
			inputStream=httpload.getInputStream(urlstr);
				try {
					len = inputStream.read(b);
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (len != -1) {
				baos.write(b, 0, len);
			}
		}
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return baos.toByteArray();
	}
    /**
     * 获取URL输入流，转为String----可行
     * */
   	public String getUrlString(String urlstr){
   	   StringBuilder sb=new StringBuilder(512);
   		URL url = null;
   		try {
   			url = new URL(urlstr);
   		} catch (MalformedURLException e) {
   			e.printStackTrace();
   		}
   		InputStreamReader urlStream = null;
   		try {
   			urlStream = new InputStreamReader(url.openStream());
   		} catch (IOException e1) {
   			e1.printStackTrace();
   		}
   		BufferedReader in=new BufferedReader(urlStream);
   		String str;
   		try {
   			while((str=in.readLine())!=null){
   			    sb.append(str);
   			}
   			in.close();
   		} catch (IOException e) {
   			e.printStackTrace();
   		}
   		try {
   			urlStream.close();
   		} catch (IOException e) {
   			e.printStackTrace();
   		}
   		return sb.toString();	
   	}
   
    /**
	    * 通过URL获取图片流，转为bitmap----〉用于前台显示或后台处理
	    * */
	   public Bitmap loadbitmap(String urlstr){
		   Bitmap bitmap=null;
		   InputStream inputstream=null;
	        URL url=null;
	        URLConnection con=null;
			try {
				url = new URL(urlstr);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}   
			try {
				con = url.openConnection();
			} catch (IOException e) {
				e.printStackTrace();
			}
	        try {
				con.connect();
			} catch (IOException e) {
				e.printStackTrace();
			}
	        try {
				inputstream = con.getInputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}   
	        BufferedInputStream bis = new BufferedInputStream(inputstream);
	        bitmap = BitmapFactory.decodeStream(bis);      
	        try {
				bis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	        try {
				inputstream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		    return bitmap;
	     }
	 
   
}



















