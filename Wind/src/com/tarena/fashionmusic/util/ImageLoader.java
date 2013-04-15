package com.tarena.fashionmusic.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

public class ImageLoader {
	//һ��map�����ڻ������ع���ͼƬ
	private HashMap<String, SoftReference<Bitmap>> caches;
	//�������
	private ArrayList<Task> taskQueue;
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			//���߳��з��ص�������ɵ�����
			Task task = (Task)msg.obj;
			//����callback�����loadedImage��������ͼƬ·����ͼƬ�ش���adapter
			task.callback.loadedImage(task.path, task.bitmap);
		}
		
	};
	//���������߳�
	private Thread thread = new Thread(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			//������ѯ
			while(true){
				//����������л���δ��������ʱ��ִ����������
				while(taskQueue.size()>0){
					//��ȡ��һ�����񣬲���֮����������Ƴ�
					Task task = taskQueue.remove(0);
//					Log.i("msg", "in thread: " + taskQueue.size() + ":" + task.path);
					try {
						//����ͼƬ
						InputStream in = HttpTool.getStream(task.path, null, null, HttpTool.GET);
						task.bitmap = BitmapTool.getBitmap(in, 2);
						FileUtils.save(task.path, task.bitmap);
						//�����ص�ͼƬ��ӵ�����
						caches.put(task.path, new SoftReference<Bitmap>(task.bitmap));
						//���handler����Ϊnull
						if(handler!=null){
							//������Ϣ���󣬲�����ɵ�������ӵ���Ϣ������
							Message msg = handler.obtainMessage();
							msg.obj = task;
							//������Ϣ�����߳�
							handler.sendMessage(msg);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				//����������Ϊ�գ������̵߳ȴ�
				synchronized (this) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
	};
	//��������ʱ����ʼ��map
	public ImageLoader(){
		caches =new  HashMap<String, SoftReference<Bitmap>>();
		taskQueue = new ArrayList<ImageLoader.Task>();
		//�������������߳�
		thread.start();
	}
	
	//ͼƬ���ط���
	public Bitmap loadImage(final String path,int scale,final ImageCallback callback){
		//���ͼƬ·�����ڻ����д�����������
		Bitmap bitmap=null;
		if(caches.containsKey(path)){
			//ȡ��������
			SoftReference<Bitmap> rf = caches.get(path);
			//ͨ�������ã���ȡͼƬ
			 bitmap = rf.get();
			//�����ͼƬ�Ѿ����ͷţ��򽫸�path��Ӧ�ļ�ֵ�Դ�map���Ƴ�
			if(bitmap==null){
				caches.remove(path);				
			}else{
				//�����ͼƬδ���ͷţ��򷵻ظ�ͼƬ
				return bitmap;
			}
		}else {
			bitmap=getBitmap(path);
			if(bitmap!=null){
				caches.put(path, new SoftReference<Bitmap>(bitmap));
				return bitmap;
			}			
		}
		if(!caches.containsKey(path)){
			//�����·����ͼƬδ�ڻ�����
			//�򴴽���������ӵ��������
			Task task = new Task();
			task.path = path;
			task.callback = callback;
			if(!taskQueue.contains(task)){
				taskQueue.add(task);
				//�������������߳�
				synchronized(thread){
					thread.notify();
				}
//				Log.i("msg", "in loadImage : " + taskQueue.size() + task.path);
			}
			
			
		}
		//���������û��ͼƬ�򷵻�null
		return null;
	}
	private Bitmap getBitmap(String path){
		return BitmapFactory.decodeFile(FileUtils.SAVE_PATH+path.substring(path.lastIndexOf("/")));
	}
	public interface ImageCallback{
		void loadedImage(String path,Bitmap bitmap);
	}
	//������
	class Task{
		String path;//�������������·��
		Bitmap bitmap;//���ص�ͼƬ
		ImageCallback callback;//�ص�����
		@Override
		public boolean equals(Object o) {
			return ((Task)o).path.equals(path);
		}
	}
}
