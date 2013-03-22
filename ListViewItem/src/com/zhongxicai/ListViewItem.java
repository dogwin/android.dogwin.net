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
     * ����Menu
     * */
    public boolean onCreateOptionsMenu(Menu menu){
    	super.onCreateOptionsMenu(menu);
    	menu.add(Menu.NONE,MENU_REFRESH,1,"ˢ��");
    	menu.add(Menu.NONE,MENU_DOWNLOAD,2,"����");
    	menu.add(Menu.NONE,MENU_SHUGUI,3,"���");
		return true;
    	  }   
    /**
     * ����service�󶨶���
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
     * ��service
     * */
    public void bindDownFileService(){
    	Intent serviceintent=new Intent();
		serviceintent.setClass(ListViewItem.this, DownLoadService.class);
	    bindService(serviceintent,serviceConnection,BIND_AUTO_CREATE );
    }
    /**
     * �����service
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
     * Menu�¼���Ӧ
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
     * ����---��Ϣ---����
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
     * ��ť��Ӧ
     * */
    class AdapterItemLisenter implements OnAdapterItemLisenter{

		public void onAdapterClickReadbtn(int position) {
			turntoreadonline(position);
		}

		public void onAdapterClickDownbtn(int position) {
			Toast.makeText( ListViewItem.this,"��ӵ������б�", Toast.LENGTH_SHORT).show();
			startDownFileService(booklist.get(position).getdownLoadUrl());
		}
    }
/**
 * ����������Ϣ
 * */
    class LoadRankThread extends Thread{
    	public void run() {
			booklist=xmlpullparser.getNetStrBookList(urlstr);	
			handler.sendEmptyMessage(REFRESHLIST);
		}
    }
  
    /////////////////////
	/**
     * ����-->��ת�������б�
     * */
    public void turntodownload(){
    	Intent intent=new Intent();
    	intent.setClass(ListViewItem.this, DownlistActivity.class);
    	startActivity(intent);
    }
 
    /**
     * ����-->��ת�������Ķ�
     * ��������xml����ַ�ĵ�Activity,�������ַ����ȡ��Ӧ�½�����
     * */
    public void turntoreadonline(int position){
    	// pageid���½ڽ��Ȼ�����ȡ��ֵ
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
  * ɾ�������item----���ڲ���
  * */
  public void deletItemClicked(int position){
	  booklist.remove(position);
	  adapter=new ListViewItemAdapter(this,booklist, adapterItemLisenter); 
      listview.setAdapter(adapter);
  }
  
}










