package com.zhongxicai.ReadLocalFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class ReadLocalText {
	/**
	 * 读取本地文本，返回InputStream
	 */
	public InputStream readFileToInputstream(String filepath)
	{
		InputStream inputStream = null;
		try
		{   
			File file=new File(filepath);
			inputStream = new FileInputStream(file);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return inputStream;
	}
	/**
	 * 读取本地文本，返回String
	 * */
		public  String readFileToString(String filepath){
				StringBuffer buffer=new StringBuffer();
				try{
					FileInputStream fis=new FileInputStream(filepath);
					//或者InputStream fis=new readFileToInputstream(filepath)
				    InputStreamReader isr=new InputStreamReader(fis,"UTF-8");
				    Reader reader=new BufferedReader(isr);
				    int ch;
				    while((ch=reader.read())>-1){
				    	buffer.append((char)ch);
				    }
				    reader.close();
				}
				catch(IOException e){
					e.printStackTrace();
				}
				
				return buffer.toString();

			}
}
