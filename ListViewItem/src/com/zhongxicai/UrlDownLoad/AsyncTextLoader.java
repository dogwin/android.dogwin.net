package com.zhongxicai.UrlDownLoad;

import android.os.AsyncTask;

public class AsyncTextLoader extends AsyncTask<String, Void, String> {//变量类型，进度，返回类型
         
            private BookHttpDownload loadNetString;
 	         private  OnAsyncTextLoaderListener asyncTextLoaderListener;
            public AsyncTextLoader(OnAsyncTextLoaderListener asyncTextLoaderListener){
            	this.asyncTextLoaderListener=asyncTextLoaderListener;
            }
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				asyncTextLoaderListener.onPreLoader();
			}
			@Override
			protected String doInBackground(String... params) {
				String text=null;
				loadNetString=new BookHttpDownload();
				try{
				text=loadNetString.getUrlString(params[0]);
				}
				catch(Exception e){
					return null;
				}
				
				return text;	
			}

			@Override
			protected void onPostExecute(String result) {
				asyncTextLoaderListener.onOverLoader(result);
			    super.onPostExecute(result);
			}

			@Override
			protected void onProgressUpdate(Void... values) {
				super.onProgressUpdate(values);
			}
			
			public interface OnAsyncTextLoaderListener{
				/**
				 * 下载前准备
				 * */
				public void onPreLoader();
				/**
				 * 下载完毕时
				 * */
				public void onOverLoader(String result);
				/**
				 * 下载过程中
				 * */
				public void onProgressLoader();
			}
		}

