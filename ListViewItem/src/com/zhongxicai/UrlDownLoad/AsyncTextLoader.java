package com.zhongxicai.UrlDownLoad;

import android.os.AsyncTask;

public class AsyncTextLoader extends AsyncTask<String, Void, String> {//�������ͣ����ȣ���������
         
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
				 * ����ǰ׼��
				 * */
				public void onPreLoader();
				/**
				 * �������ʱ
				 * */
				public void onOverLoader(String result);
				/**
				 * ���ع�����
				 * */
				public void onProgressLoader();
			}
		}

