package dogwin.net.apps;

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
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buddha);
		
		
		this.pid = android.os.Process.myPid();
		//check network
	
		
		IntentFilter mFilter = new IntentFilter();  
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);  
        registerReceiver(mReceiver, mFilter); 
        
        btone = (Button)this.findViewById(R.id.btone);
        
        btone.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				vibrator.vibrate(1000);
			}
		});
        
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
				Toast.makeText(Buddha.this, "connected!" , Toast.LENGTH_LONG).show();
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
}
