package com.zhongxicai;

import java.util.List;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;
import com.zhongxicai.ListViewAdapter.ListViewItemAdapter;
import com.zhongxicai.ListViewAdapter.ListViewItemAdapter.OnAdapterItemLisenter;
import com.zhongxicai.PullParser.*;
import com.zhongxicai.Beans.*;
import com.zhongxicai.UrlDownLoad.BookHttpDownload;
import com.zhongxicai.UrlDownLoad.FileDownload;
import com.zhongxicai.XMLPullParser.XMLPullParser;


public class ListViewItem extends Activity{

   ListView listview;
   final int DONE=1;
   BookHttpDownload urldownload;
   XMLParser xmlsaxparser;
   XMLPullParser xmlpullparser;
   List<BookRank> booklist;
   ListViewItemAdapter adapter;
   AdapterItemLisenter adapterItemLisenter;
   protected final static int MENU_REFRESH = Menu.FIRST; 
   protected final static int MENU_DOWNLOAD = Menu.FIRST + 1; 
   protected final static int MENU_SHUGUI = Menu.FIRST + 2; 
   String urlstr="http://10.0.2.2:8080/downloadfile/bookrank/rank.xml";
   String filepath="/mnt/sdcard/mybook/rank.xml";
   final int REFRESHLIST=1;
   final int SHOWIMAGE=2;
   private LoadRankThread loadRankThread;
   private DownLoadService downLoadService;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        listview=(ListView)findViewById(R.id.listview);  
        xmlsaxparser=new XMLParser(ListViewItem.this);
    	xmlpullparser=new  XMLPullParser(ListViewItem.this);
        urldownload=new  BookHttpDownload();
        adapterItemLisenter=new AdapterItemLisenter();
        
       }
    public void onStart(){
    	startDownFileService(null);
    	bindDownFileService();
    	super.onStart();
    }
    public void onStop(){
    	super.onStop();
    }
	/**
     * 创建Menu
     * */
    public boolean onCreateOptionsMenu(Menu menu){
    	super.onCreateOptionsMenu(menu);
    	menu.add(Menu.NONE,MENU_REFRESH,1,"刷新");
    	menu.add(Menu.NONE,MENU_DOWNLOAD,2,"下载");
    	menu.add(Menu.NONE,MENU_SHUGUI,3,"书柜");
		return true;
    	  }   
    /**
     * 创建service绑定对象
     * */
    private ServiceConnection serviceConnection=new ServiceConnection(){
		public void onServiceConnected(ComponentName name, IBinder service) {
			downLoadService=((DownLoadService.MyBinder)service).getService();
			Log.i("service","connected");
		}
		public void onServiceDisconnected(ComponentName name) {
			downLoadService=null;
			Log.i("service","failed");
		}  	
    };
    /**
     * 绑定service
     * */
    public void bindDownFileService(){
    	Intent serviceintent=new Intent();
		serviceintent.setClass(ListViewItem.this, DownLoadService.class);
	    bindService(serviceintent,serviceConnection,BIND_AUTO_CREATE );
    }
    /**
     * 解除绑定service
     * */
    public void unBindDownFileService(){
    	unbindService(serviceConnection);
    }
    /**
     * onstart-service
     * */
    public void startDownFileService(String urlstr){
    	Intent downfileintent=new Intent();
    	Bundle bundle=new Bundle();
    	bundle.putString("urlstr",urlstr);
    	downfileintent.putExtras(bundle);
    	downfileintent.setClass(ListViewItem.this, DownLoadService.class);
    	startService(downfileintent);
    }
    /**
     * Menu事件响应
     * */
    public boolean onOptionsItemSelected(MenuItem item){
    	super.onOptionsItemSelected(item);
    	switch(item.getItemId()){
    	case MENU_REFRESH:
    		loadRankThread=new LoadRankThread();
    		loadRankThread.start();
    		break;
    	case MENU_DOWNLOAD:
    		 turntodownload();
    		break;
    	}
    	return true;
    	
    }
    ////////////////////////
    /**
     * 接收---消息---操作
     * */
    Handler handler=new Handler(){
    	public void handleMessage(Message msg){
    		switch(msg.what){
    		case REFRESHLIST:
    			refreshListView(booklist);
    			break;
    		case SHOWIMAGE:
    			break;
    		
    		}
    	}
    };

    public void refreshListView(List<BookRank> booklist){
    	adapter=new ListViewItemAdapter(ListViewItem.this,booklist, adapterItemLisenter); 
		listview.setAdapter(adapter);
    }
    /**
     * 按钮响应
     * */
    class AdapterItemLisenter implements OnAdapterItemLisenter{

		public void onAdapterClickReadbtn(int position) {
			turntoreadonline(position);
		}

		public void onAdapterClickDownbtn(int position) {
			Toast.makeText( ListViewItem.this,"添加到下载列表", Toast.LENGTH_SHORT).show();
			startDownFileService(booklist.get(position).getdownLoadUrl());
		}
    }
/**
 * 下载排行信息
 * */
    class LoadRankThread extends Thread{
    	public void run() {
			booklist=xmlpullparser.getNetStrBookList(urlstr);	
			handler.sendEmptyMessage(REFRESHLIST);
		}
    }
  
    /////////////////////
	/**
     * 方法-->跳转到下载列表
     * */
    public void turntodownload(){
    	Intent intent=new Intent();
    	intent.setClass(ListViewItem.this, DownlistActivity.class);
    	startActivity(intent);
    }
 
    /**
     * 方法-->跳转到在线阅读
     * 把在线阅xml读网址阅的Activity,其根据网址，读取相应章节内容
     * */
    public void turntoreadonline(int position){
    	// pageid是章节进度缓存中取得值
    	int pageid=1;
    	Intent intent=new Intent();
    	Bundle bundle=new Bundle();
    	String urlstr=booklist.get(position).getreadOnlineUrl();
    	String contenturlstr=booklist.get(position).getcontentUrl();
    	bundle.putString("contenturl", contenturlstr);
    	bundle.putString("url",urlstr);
    	bundle.putInt("pageid",pageid);
        intent.putExtras(bundle);
    	intent.setClass(ListViewItem.this,BookReadOnLine.class);
    	startActivity(intent);
    	
    }
  

 /**
  * 删除点击的item----用于测试
  * */
  public void deletItemClicked(int position){
	  booklist.remove(position);
	  adapter=new ListViewItemAdapter(this,booklist, adapterItemLisenter); 
      listview.setAdapter(adapter);
  }
  
}










