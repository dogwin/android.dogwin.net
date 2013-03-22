package com.zhongxicai.UrlDownLoad;

import java.util.List;
import android.os.AsyncTask;
import com.zhongxicai.Beans.BookContent;
import com.zhongxicai.XMLPullParser.PullServiceLoadContent;

public class AsyncContentLoader extends AsyncTask<String, Void, List<BookContent>> {//�������ͣ����ȣ���������
     
    private OnAsyncContentLoaderListener asyncContentLoaderListener;
    public AsyncContentLoader(OnAsyncContentLoaderListener asyncContentLoaderListener){
    	this.asyncContentLoaderListener=asyncContentLoaderListener;
    }
	@Override
	protected void onPreExecute() {
		super.onPreExecute();	
		asyncContentLoaderListener.onPreLoader();
	}
	@Override
	protected List<BookContent> doInBackground(String... params) {
		List<BookContent> content;
		PullServiceLoadContent pullloadcontent;
		pullloadcontent=new PullServiceLoadContent();
		content=pullloadcontent.getBookContent(params[0]);	    
		return content;	
	}

	@Override
	protected void onPostExecute(List<BookContent> result) {
	    super.onPostExecute(result);
	    asyncContentLoaderListener.onOverLoader(result);
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		super.onProgressUpdate(values);
	}
	
	public interface OnAsyncContentLoaderListener{
		/**
		 * ����ǰ׼��
		 * */
		public void onPreLoader();
		/**
		 * �������ʱ
		 * */
		public void onOverLoader(List<BookContent> result);
		/**
		 * ���ع�����
		 * */
		public void onProgressLoader();
	}
}

