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
	 * ��ȡString���󣬷���InputStream
	 * */
	public InputStream readStringToInputStream(String string){
		InputStream inputstream=null;
		byte   b[]= string.getBytes(); 
	    inputstream=new  ByteArrayInputStream(b);
		return inputstream;
	}
	/**
	 * ��ȡraw�е��ı��ļ�������InputStream
	 */
	public InputStream readRawToInputStream(int Id)
	{
		InputStream inputstream=null;
		inputstream=context.getResources().openRawResource(Id);
		return inputstream;
	}
	
	/**
	 * ��ȡassets�е��ı��ļ�������InputStream
	 */
	public  InputStream readAssetsToInputStream(String name){
		
		InputStream inputstream = null;//������
		try {
			inputstream= context.getAssets().open(name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//ע������ʽ
		return inputstream;
	}
	/**
	 * ��ȡassets�е��ı��ļ�������String
	 * */
	public  String readAssestToString(String name){
		
		InputStream in = null;//������
		BufferedReader reader = null;//����
		StringBuilder sb = new StringBuilder();//���ڴ�Ŷ�ȡ������
		try{
			in = readAssetsToInputStream(name);
			reader = new BufferedReader(new InputStreamReader(in));//����ȡ�������ݱ����ڻ�����
			String line = null;
			while((line = reader.readLine())!=null){//�����ȡû����
				sb.append(line);//�ڱ��ֵ����ݽ�β׷���¶�ȡ��һ������
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
