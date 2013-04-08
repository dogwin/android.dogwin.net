package dogwin.net.music;import dogwin.net.apps.Buddha;import dogwin.net.apps.R;import dogwin.net.books.BooksApp;import dogwin.net.buddha.BuddhaApp;import dogwin.net.check.AutoUpdata;import dogwin.net.master.MasterApp;import dogwin.net.setting.SettingApp;import dogwin.net.story.StoryApp;import android.annotation.SuppressLint;import android.annotation.TargetApi;import android.app.Activity;import android.app.Notification;import android.app.NotificationManager;import android.app.PendingIntent;import android.content.Intent;import android.os.Build;import android.os.Bundle;import android.util.Log;import android.view.Menu;import android.view.MenuInflater;import android.view.MenuItem;public class MusicApp extends Activity{	int pid;	public boolean IconFlag=true;	Intent bintent;		@Override	protected void onCreate(Bundle savedInstanceState) {		// TODO Auto-generated method stub		super.onCreate(savedInstanceState);				this.pid = android.os.Process.myPid();	}		@Override	public boolean onCreateOptionsMenu(Menu menu) {		MenuInflater inflater = new MenuInflater(this);		inflater.inflate(R.menu.menu_option_main, menu);		return super.onCreateOptionsMenu(menu);	}	@Override	public boolean onOptionsItemSelected(MenuItem item) {		switch (item.getItemId()) {		case R.id.liberation:			IconFlag = false;			bintent = new Intent(MusicApp.this,Buddha.class);  		    startActivity(bintent);		    //this.finish();			break;		case R.id.buddha://诸佛菩萨			IconFlag = false;			bintent = new Intent(MusicApp.this,BuddhaApp.class);  		    startActivity(bintent);		    //this.finish();			break;		case R.id.music://佛教音乐			IconFlag = false;			bintent = new Intent(MusicApp.this,MusicApp.class);  		    startActivity(bintent);			break;		case R.id.books://佛教经典			IconFlag = false;			bintent = new Intent(MusicApp.this,BooksApp.class);  		    startActivity(bintent);			break;		case R.id.story://佛教故事			IconFlag = false;			bintent = new Intent(MusicApp.this,StoryApp.class);  		    startActivity(bintent);			break;		case R.id.master://祖师大德			IconFlag = false;			bintent = new Intent(MusicApp.this,MasterApp.class);  		    startActivity(bintent);			break;		case R.id.setting://系统设置			IconFlag = false;			bintent = new Intent(MusicApp.this,SettingApp.class);  		    startActivity(bintent);			break;		case R.id.quit://退出			Log.v("TAG","menu");			android.os.Process.sendSignal(pid, android.os.Process.SIGNAL_KILL);		}				return super.onOptionsItemSelected(item);	}		@Override	protected void onStop() {		// TODO Auto-generated method stub		super.onStop();		Log.v("TAG", "back Run");		if(IconFlag){			createNotification();		}	}	@Override	protected void onRestart() {		// TODO Auto-generated method stub		super.onRestart();		IconFlag = false;	}	//Icon Notification		@TargetApi(Build.VERSION_CODES.JELLY_BEAN)	@SuppressLint("NewApi")	public void createNotification(){		Intent intent = new Intent(this, MusicApp.class);		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);		//title		String title = getResources().getString(R.string.app_name);		String subject = getResources().getString(R.string.subject);				Notification noti = new Notification.Builder(this)        .setContentTitle(title)        .setContentText(subject).setSmallIcon(R.drawable.ic_launcher)        .setContentIntent(pIntent).build();		 NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);		    // Hide the notification after its selected		 noti.flags |= Notification.FLAG_AUTO_CANCEL;		 notificationManager.notify(0, noti);	}}