package com.tarena.fashionmusic.util;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;


public class AlbumImageLoader {
	//һ��map�����ڻ������ع���ͼƬ
	private HashMap<String, SoftReference<Bitmap>> caches;
	Context mContext;
	//�������
	private ArrayList<Task> taskQueue;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			//���߳��з��ص�������ɵ�����
			Task task = (Task)msg.obj;
			//����callback�����loadedImage��������ͼƬ·����ͼƬ�ش���adapter
			task.callback.loadedImage(task.albumkey, task.bitmap);
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
					try {
//						task.bitmap=ContactsData.getphotobyalbumkey(mContext, task.albumkey);
						task.bitmap=BitmapTool.getbitBmBykey(mContext, task.albumkey);
						//�����ص�ͼƬ��ӵ�����
						caches.put(task.albumkey, new SoftReference<Bitmap>(task.bitmap));
						//���handler����Ϊnull
						if(handler!=null){
							//������Ϣ���󣬲�����ɵ�������ӵ���Ϣ������
							Message msg = handler.obtainMessage();
							msg.obj = task;
							//������Ϣ�����߳�
							handler.sendMessage(msg);
						}
					} catch (Exception e) {
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
	public AlbumImageLoader(Context context){
		caches =new  HashMap<String, SoftReference<Bitmap>>();
		mContext=context;
		taskQueue = new ArrayList<AlbumImageLoader.Task>();
		//�������������߳�
		thread.start();
	}
	
	//ͼƬ���ط���
	public Bitmap loadImage(final String albumkey,final ImageCallback callback){
		//���ͼƬ·�����ڻ����д�����������
		if(caches.containsKey(albumkey)){
			//ȡ��������
			SoftReference<Bitmap> rf = caches.get(albumkey);
			//ͨ�������ã���ȡͼƬ
			Bitmap bitmap = rf.get();
			//�����ͼƬ�Ѿ����ͷţ��򽫸�path��Ӧ�ļ�ֵ�Դ�map���Ƴ�
			if(bitmap==null){
				caches.remove(albumkey);				
			}else{
				//�����ͼƬδ���ͷţ��򷵻ظ�ͼƬ
				return bitmap;
			}
		}
		if(!caches.containsKey(albumkey)){
			//�����·����ͼƬδ�ڻ�����
			//�򴴽���������ӵ��������
			Task task = new Task();
			task.albumkey=albumkey;
			task.callback = callback;
			if(!taskQueue.contains(task)){
				taskQueue.add(task);
				//�������������߳�
				synchronized(thread){
					thread.notify();
				}
			}
		}
		//���������û��ͼƬ�򷵻�null
		return null;
	}
	
	public interface ImageCallback{
		void loadedImage(String albumkey,Bitmap bitmap);
	}
	//������
	class Task{
		String albumkey;
		Bitmap bitmap;//���ص�ͼƬ
		ImageCallback callback;//�ص�����
		
//		@Override
//		public boolean equals(Object o) {
//			return ((Task)o).albumkey==albumkey;
//		}
		
	}
	
}
