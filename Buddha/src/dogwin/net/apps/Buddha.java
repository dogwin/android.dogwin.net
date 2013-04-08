package dogwin.net.apps;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import dogwin.net.backRun.IconShow;
import dogwin.net.books.BooksApp;
import dogwin.net.buddha.BuddhaApp;
import dogwin.net.check.Connectivity;
import dogwin.net.master.MasterApp;
import dogwin.net.music.MusicApp;
import dogwin.net.setting.SettingApp;
import dogwin.net.story.StoryApp;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class Buddha extends Activity {
	
	Button btone;
	IconShow iconShow;
	int pid;
	public boolean IconFlag=true;
	Intent bintent;
	String edy_url;
	TextView edw_content;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buddha);
		
		
		this.pid = android.os.Process.myPid();
		//check network
	
		
		IntentFilter mFilter = new IntentFilter();  
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);  
        registerReceiver(mReceiver, mFilter); 
        /*
        btone = (Button)this.findViewById(R.id.btone);
        
        btone.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				vibrator.vibrate(1000);
			}
		});*/
       
	}
/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.buddha, menu);
		return true;
	}*/
	
	/**
	 * network check
	 */
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(Connectivity.isConnected(context)){
				//Toast.makeText(Buddha.this, "connected!" , Toast.LENGTH_LONG).show();
				 edy_url = getResources().getString(R.string.EDW_Url);
			        
			     //edw_Json(edy_url);
			     Log.i("url",edy_url);
			}else{
				Toast.makeText(Buddha.this, "disconnected!" , Toast.LENGTH_LONG).show();
			}
		}
	};
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = new MenuInflater(this);
		inflater.inflate(R.menu.menu_option_main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.liberation:
			IconFlag = false;
			bintent = new Intent(Buddha.this,Buddha.class);  
		    startActivity(bintent);
		    //this.finish();
			break;
		case R.id.buddha://诸佛菩萨
			IconFlag = false;
			bintent = new Intent(Buddha.this,BuddhaApp.class);  
		    startActivity(bintent);
		    //this.finish();
			break;
		case R.id.music://佛教音乐
			IconFlag = false;
			bintent = new Intent(Buddha.this,MusicApp.class);  
		    startActivity(bintent);
			break;
		case R.id.books://佛教经典
			IconFlag = false;
			bintent = new Intent(Buddha.this,BooksApp.class);  
		    startActivity(bintent);
			break;
		case R.id.story://佛教故事
			IconFlag = false;
			bintent = new Intent(Buddha.this,StoryApp.class);  
		    startActivity(bintent);
			break;
		case R.id.master://祖师大德
			IconFlag = false;
			bintent = new Intent(Buddha.this,MasterApp.class);  
		    startActivity(bintent);
			break;
		case R.id.setting://系统设置
			IconFlag = false;
			bintent = new Intent(Buddha.this,SettingApp.class);  
		    startActivity(bintent);
			break;
		case R.id.quit://退出
			Log.v("TAG","menu");
			android.os.Process.sendSignal(pid, android.os.Process.SIGNAL_KILL);
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.v("TAG", "back Run");
		if(IconFlag){
			createNotification();
		}
	}
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		IconFlag = false;
	}
	//Icon Notification
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressLint("NewApi")
	public void createNotification(){
		Intent intent = new Intent(this, Buddha.class);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
		//title
		String title = getResources().getString(R.string.app_name);
		String subject = getResources().getString(R.string.subject);
		
		Notification noti = new Notification.Builder(this)
        .setContentTitle(title)
        .setContentText(subject).setSmallIcon(R.drawable.ic_launcher)
        .setContentIntent(pIntent).build();
		 NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		    // Hide the notification after its selected
		 noti.flags |= Notification.FLAG_AUTO_CANCEL;

		 notificationManager.notify(0, noti);
	}
	public void edw_Json(String url){
		/*try {
			//StringBuffer sb = new StringBuffer();
			//在测试过程中，经常是用本机做测试服务器，访问本机的IP地址要设置为10.0.2.2
			String body = getContent(url);
			Log.i("body",body);
			JSONArray array = new JSONArray(body);
			for(int i=0; i<array.length(); i++){
				JSONObject obj = array.getJSONObject(i);
				Log.i("obj",obj.getString("msg"));
				edw_content = (TextView)this.findViewById(R.id.edw_content);
		        edw_content.setText(obj.getString("msg"));
			}

			//TextView textView = (TextView)findViewById(R.id.tv);
			//textView.setText(sb.toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}*/
		String body;
		Log.i("body","here work");
		try {
			body = getContent(url);
			Log.i("body",body);
			JSONArray array = new JSONArray(body);
			for(int i=0; i<array.length(); i++){
				JSONObject obj = array.getJSONObject(i);
				Log.i("obj",obj.getString("msg"));
				edw_content = (TextView)this.findViewById(R.id.edw_content);
		        edw_content.setText(obj.getString("msg"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private String getContent(String url) throws Exception{
		StringBuilder sb = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpParams httpParams = client.getParams();
		//设置网络超时参数
		HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
		HttpConnectionParams.setSoTimeout(httpParams, 5000);
		HttpResponse response = client.execute(new HttpGet(url));
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"), 8192);
			String line = null;
			while ((line = reader.readLine())!= null){
				sb.append(line + "\n");
			}
			reader.close();
		}
		return sb.toString();
	}
	/**
	 * edw_content = (TextView)this.findViewById(R.id.edw_content);
	        edw_content.setText(msg);
	 */
	
}
