package dogwin.net.apps;

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
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import dogwin.net.backRun.IconShow;
import dogwin.net.books.BooksApp;
import dogwin.net.buddha.BuddhaApp;
import dogwin.net.check.BitmapCreateNew;
import dogwin.net.check.Connectivity;
import dogwin.net.check.DwClient;
import dogwin.net.check.JSONParser;
import dogwin.net.check.LoadImages;
import dogwin.net.check.Login;
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
	//check sharepreferences
	public EditTextPreference editText;
	public SharedPreferences preferences;
	static String uc_username,uc_password;
	static boolean uc_flag;
	//load images
	LoadImages loadImages = new LoadImages();
	/**
	 * username&password
	 */
	
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
		edy_url = getResources().getString(R.string.EDW_Url);
		edw_content = (TextView)this.findViewById(R.id.edw_content);
		
	    //login
		final ImageButton logining = (ImageButton)this.findViewById(R.id.logining);
		
		uc_username = Rt_username();
		uc_password = Rt_password();
		uc_flag = Rt_flag();
		System.out.println("user current=>"+uc_username+":"+uc_password+":"+uc_flag);
		
		//set new image
		if(DwClient.flag||uc_flag){
			//get the user
			
			Bitmap bitmap = loadImages.loadimgs("http://stagingworkspace.com/darrenpage/data/fo.jpg");
			BitmapCreateNew bitmapCreateNew = new BitmapCreateNew(bitmap);
			System.out.println("bitmap=>"+bitmap);
			logining.setImageBitmap(bitmapCreateNew.newimage(80, 80));
		}
		
		logining.setOnClickListener(new View.OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!DwClient.flag&&!uc_flag){
					login();
					IconFlag = false;
					Buddha.this.finish();
				}else{
					IconFlag = false;
					System.out.println("Buddha flag=>"+DwClient.flag);
					//go to the profile page set
					
				}
			}
		});
	}
	protected void login(){
		Intent logIntent = new Intent(Buddha.this, Login.class);
		startActivity(logIntent);
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
			     Log.i("url",edy_url);
			     
			     JSONParser jParser = new JSONParser(context);
				 
				 // getting JSON string from URL
				 JSONObject json = jParser.getJSONFromUrl(edy_url);
				 //System.out.println("json=>"+json);
				 
					try {
					    // Getting Array of Contacts
						JSONArray contacts = json.getJSONArray("content");
					 
					    // looping through All Contacts
					    for(int i = 0; i < contacts.length(); i++){
					        JSONObject c = contacts.getJSONObject(i);
					        String msg = c.getString("msg");
					        System.out.println("msg=>"+msg);
					        edw_content.setText(msg);
					    }
					} catch (JSONException e) {
					    e.printStackTrace();
					}
			     
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
		    this.finish();
			break;
		case R.id.buddha://诸佛菩萨
			IconFlag = false;
			bintent = new Intent(Buddha.this,BuddhaApp.class);  
		    startActivity(bintent);
		    this.finish();
			break;
		case R.id.music://佛教音乐
			IconFlag = false;
			bintent = new Intent(Buddha.this,MusicApp.class);  
		    startActivity(bintent);
		    this.finish();
		    break;
		case R.id.books://佛教经典
			IconFlag = false;
			bintent = new Intent(Buddha.this,BooksApp.class);  
		    startActivity(bintent);
		    this.finish();
		    break;
		case R.id.story://佛教故事
			IconFlag = false;
			bintent = new Intent(Buddha.this,StoryApp.class);  
		    startActivity(bintent);
		    this.finish();
		    break;
		case R.id.master://祖师大德
			IconFlag = false;
			bintent = new Intent(Buddha.this,MasterApp.class);  
		    startActivity(bintent);
		    this.finish();
		    break;
		case R.id.setting://系统设置
			IconFlag = false;
			bintent = new Intent(Buddha.this,SettingApp.class);  
		    startActivity(bintent);
		    this.finish();
		    break;
		case R.id.quit://退出
			Log.v("TAG","menu");
			android.os.Process.killProcess(pid);
			super.finish();
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
		unregisterReceiver(mReceiver);
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
	//get user current
	public String Rt_username(){
		preferences = getSharedPreferences("usercurrent", Activity.MODE_PRIVATE);
	    return preferences.getString("username", null);
	}
	
	
	public String Rt_password(){
		preferences = getSharedPreferences("usercurrent", Activity.MODE_PRIVATE);
	    return preferences.getString("password", null);
	}
	
	public boolean Rt_flag(){
		preferences = getSharedPreferences("usercurrent", Activity.MODE_PRIVATE);
		return preferences.getBoolean("flag", false);
	}
}
