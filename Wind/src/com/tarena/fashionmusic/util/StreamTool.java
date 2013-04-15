package com.tarena.fashionmusic.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Handler;
import android.os.Message;

public class StreamTool {
	public static final int MSG_PROGRESS = 5;
	public static void save(InputStream in,String path,Handler handler,long fileLength) throws IOException{
		if(in !=null && path!=null){
			FileOutputStream out = new FileOutputStream(path);
			readData(in, out,handler,fileLength);
		}
	}
	public static byte[] read(InputStream in) throws IOException{
		byte[] bytes = null;
		if(in!=null){
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			readData(in, out,null,0);
			bytes = out.toByteArray();
		}
		return bytes;
	}
	/**
	 * @param in
	 * @param out
	 * @param handler
	 * @throws IOException
	 */
	public static void readData(InputStream in,OutputStream out,Handler handler,long fileLength) throws IOException{
		if(in!=null && out!=null){
			BufferedInputStream bis = new BufferedInputStream(in);
			BufferedOutputStream bos = new BufferedOutputStream(out);
			int len = -1;
			byte[] bytes = new byte[1024];
			int loadedLength = 0;//�����صĳ��ȣ�kb��
			while((len = bis.read(bytes))!=-1){
				bos.write(bytes,0,len);
				loadedLength++;//ÿ����1kb����ֵ+1
				//��������س�����200����������������Ϣ�����߳�
				if(loadedLength%12==0 && handler!=null){
					Message msg = handler.obtainMessage(MSG_PROGRESS);
					msg.arg1 = loadedLength;
					if (fileLength>0&&loadedLength<fileLength) {
						handler.sendMessage(msg);
					}
				}
				bos.flush();
			}
			bos.close();
			out.close();
			bis.close();
			in.close();
		}
	}
}
