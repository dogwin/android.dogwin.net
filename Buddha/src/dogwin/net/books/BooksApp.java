package dogwin.net.books;import dogwin.net.apps.R;import dogwin.net.check.DwClient;import dogwin.net.publics.Menus;import android.annotation.SuppressLint;import android.annotation.TargetApi;import android.app.Activity;import android.app.Notification;import android.app.NotificationManager;import android.app.PendingIntent;import android.content.Intent;import android.content.SharedPreferences;import android.os.Build;import android.os.Bundle;import android.preference.EditTextPreference;import android.util.Log;import android.view.Menu;import android.view.MenuInflater;import android.view.MenuItem;public class BooksApp extends Activity{		int pid;	public boolean IconFlag=true;	Intent bintent;		//check sharepreferences	public EditTextPreference editText;	public SharedPreferences preferences;	static String uc_username,uc_password;	static boolean uc_flag;				@Override	protected void onCreate(Bundle savedInstanceState) {		// TODO Auto-generated method stub		super.onCreate(savedInstanceState);		this.pid = android.os.Process.myPid();		uc_flag = Rt_flag();	}		@Override	public boolean onCreateOptionsMenu(Menu menu) {		MenuInflater inflater = new MenuInflater(this);		if(DwClient.flag||uc_flag){			inflater.inflate(R.menu.menu_option_main, menu);		}else{			inflater.inflate(R.menu.menu_unlogin, menu);		}		return super.onCreateOptionsMenu(menu);			}	@Override	public boolean onOptionsItemSelected(MenuItem item) {		boolean menu_flag = false;		IconFlag = false;		if(DwClient.flag||uc_flag){			menu_flag = true;		}		return super.onOptionsItemSelected(Menus.select_menus(item, BooksApp.this, pid, menu_flag));			}		@Override	protected void onStop() {		// TODO Auto-generated method stub		super.onStop();		Log.v("TAG", "back Run");		if(IconFlag){			createNotification();		}	}	@Override	protected void onRestart() {		// TODO Auto-generated method stub		super.onRestart();		IconFlag = false;	}	//Icon Notification		@TargetApi(Build.VERSION_CODES.JELLY_BEAN)	@SuppressLint("NewApi")	public void createNotification(){		Intent intent = new Intent(this, BooksApp.class);		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);		//title		String title = getResources().getString(R.string.app_name);		String subject = getResources().getString(R.string.subject);				Notification noti = new Notification.Builder(this)        .setContentTitle(title)        .setContentText(subject).setSmallIcon(R.drawable.ic_launcher)        .setContentIntent(pIntent).build();		 NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);		    // Hide the notification after its selected		 noti.flags |= Notification.FLAG_AUTO_CANCEL;		 notificationManager.notify(0, noti);	}		//get user current	public String Rt_username(){		preferences = getSharedPreferences("usercurrent", Activity.MODE_PRIVATE);	    return preferences.getString("username", null);	}			public String Rt_password(){		preferences = getSharedPreferences("usercurrent", Activity.MODE_PRIVATE);	    return preferences.getString("password", null);	}		public boolean Rt_flag(){		preferences = getSharedPreferences("usercurrent", Activity.MODE_PRIVATE);		return preferences.getBoolean("flag", false);	}}