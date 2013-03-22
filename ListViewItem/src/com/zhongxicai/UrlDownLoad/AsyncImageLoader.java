package com.zhongxicai.UrlDownLoad;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

public class AsyncImageLoader {
   private BookHttpDownload httpdownload=new BookHttpDownload();
		
	public Bitmap loadBitmap(final String imageUrl,final ImageCallback callback){
		
		final Handler handler=new Handler(){
			public void handleMessage(Message msg) {
				callback.imageLoaded(imageUrl,(Bitmap) msg.obj);
			}
		};		
		new Thread(){
			public void run() {
				System.out.println("º”‘ÿ∑‚√Ê");
				Bitmap bitmap=httpdownload.loadbitmap(imageUrl);
				handler.sendMessage(handler.obtainMessage(0,bitmap));
				
			};
		}.start();
		return null;
	}

	public interface ImageCallback{
		public void imageLoaded(String imageUrl,Bitmap bitmap);
	}
}
