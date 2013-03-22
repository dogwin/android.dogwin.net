package com.zhongxicai.ReadLocalFile;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;

public class ReadSourceText {
	
	private Context context;
	public ReadSourceText(){
		super();
	}
	
	public ReadSourceText(Context context){
		this.context=context;
	}
	/**
	 * 读取String对象，返回InputStream
	 * */
	public InputStream readStringToInputStream(String string){
		InputStream inputstream=null;
		byte   b[]= string.getBytes(); 
	    inputstream=new  ByteArrayInputStream(b);
		return inputstream;
	}
	/**
	 * 读取raw中的文本文件，返回InputStream
	 */
	public InputStream readRawToInputStream(int Id)
	{
		InputStream inputstream=null;
		inputstream=context.getResources().openRawResource(Id);
		return inputstream;
	}
	
	/**
	 * 读取assets中的文本文件，返回InputStream
	 */
	public  InputStream readAssetsToInputStream(String name){
		
		InputStream inputstream = null;//输入流
		try {
			inputstream= context.getAssets().open(name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//注意编码格式
		return inputstream;
	}
	/**
	 * 读取assets中的文本文件，返回String
	 * */
	public  String readAssestToString(String name){
		
		InputStream in = null;//输入流
		BufferedReader reader = null;//缓存
		StringBuilder sb = new StringBuilder();//用于存放读取的数据
		try{
			in = readAssetsToInputStream(name);
			reader = new BufferedReader(new InputStreamReader(in));//将读取到的数据保存在缓存中
			String line = null;
			while((line = reader.readLine())!=null){//如果读取没结束
				sb.append(line);//在保持的数据结尾追加新读取的一行数据
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				in.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	
}
