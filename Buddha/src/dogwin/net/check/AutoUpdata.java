package dogwin.net.check;


import android.app.Activity;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.EditTextPreference;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import dogwin.net.apps.Buddha;
import dogwin.net.apps.R;
import dogwin.net.check.DownLoadManager;
import dogwin.net.check.UpdataInfo;
import dogwin.net.check.UpdataInfoParser;
import dogwin.net.publics.Menus;

public class AutoUpdata extends Activity{
	private final String TAG = this.getClass().getName();

	private final int UPDATA_NONEED = 0;
	private final int UPDATA_CLIENT = 1;
	private final int GET_UNDATAINFO_ERROR = 2;
	private final int SDCARD_NOMOUNTED = 3;
	private final int DOWN_ERROR = 4;

	private UpdataInfo info;
	private String localVersion;
	
	int pid;
	public boolean IconFlag=false;
	Intent bintent;
	
	//check sharepreferences
	public EditTextPreference editText;
	public SharedPreferences preferences;
	static String uc_username,uc_password;
	static boolean uc_flag;
	
	Menus menus = new Menus();
	//private FunnyLifeApplication application;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.autoupdata);
		new Thread(new CheckVersionTask()).start();//执行检查服务器数据库版本号
		this.pid = android.os.Process.myPid();
		uc_flag = Rt_flag();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = new MenuInflater(this);
		if(DwClient.flag||uc_flag){
			inflater.inflate(R.menu.menu_option_main, menu);
		}else{
			inflater.inflate(R.menu.menu_unlogin, menu);
		}
		return super.onCreateOptionsMenu(menu);
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean menu_flag = false;
		IconFlag = false;
		if(DwClient.flag||uc_flag){
			menu_flag = true;
		}
		return super.onOptionsItemSelected(menus.select_menus(item, AutoUpdata.this, pid, menu_flag));
		
	}
	
	//获取当前程序版本号
	public String getVersionName() throws Exception{
		//获取packgaemanager的实例
		PackageManager packageManager=getPackageManager();
		//getPackageName()是当前包名，0代表获取版本信息
		PackageInfo packageInfo=packageManager.getPackageInfo(getPackageName(), 0);
		return packageInfo.versionName;
	}
	public int getVersionCode() throws Exception{
		//获取packgaemanager的实例
		PackageManager packageManager=getPackageManager();
		//getPackageName()是当前包名，0代表获取版本信息
		PackageInfo packageInfo=packageManager.getPackageInfo(getPackageName(), 0);
		return packageInfo.versionCode;
	}
	
	//从服务器获取xml解析并进行比对版本号
	public class CheckVersionTask implements Runnable{
		@Override
		
		public void run() {
			
			try {
				localVersion = String.valueOf(getVersionCode());
				// 从资源文件获取服务器地址
				String path=getResources().getString(R.string.serverurl);
				// 包装成url对象
				URL url=new URL(path);
				HttpURLConnection conn=(HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(5000);
				InputStream is=conn.getInputStream();
				info=UpdataInfoParser.getUpdataInfo(is);
				System.out.println("MenuActivity--->info="+info.getVersion()+"<<<==localV==>>>"+localVersion);
				if(info.getVersion().equals(localVersion)){
					Log.i(TAG, "版本号相同，无需升级");
					Message msg=new Message();
					msg.what=UPDATA_NONEED;
					LoginMain();
				}else if(!info.getVersion().equals(localVersion)){
					
					Log.i(TAG, "版本号不同，提示用户升级");
					Message msg=new Message();
					msg.what=UPDATA_CLIENT;
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				// 待处理
				Message msg = new Message();
				msg.what = GET_UNDATAINFO_ERROR;
				handler.sendMessage(msg);
				e.printStackTrace();
			}
		}
	}
	
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case UPDATA_NONEED:
				Toast.makeText(getApplicationContext(), "您安装的已经是最新版本", 1).show();
			case UPDATA_CLIENT:
				// 对话框通知用户升级程序
				// Toast.makeText(getApplicationContext(), "可以升级程序啦~",
				// 1).show();
				showUpdataDialog();
				break;
			case GET_UNDATAINFO_ERROR:
				// 服务器超时
				Toast.makeText(getApplicationContext(), "获取服务器更新信息失败", 1).show();
				LoginMain();
				break;
			case SDCARD_NOMOUNTED:
				// sdcard不可用
				Toast.makeText(getApplicationContext(), "SD卡不可用",1).show();
				LoginMain();
				break;
			case DOWN_ERROR:
				// 下载apk失败
				Toast.makeText(getApplicationContext(), "下载新版本失败", 1).show();
				LoginMain();
				break;
			}
		}
	};
	
	/**
	 * 弹出对话框通知用户更新程序
	 * 弹出对话框的步骤：
	 * 1.创建alertDialog的builder.
	 * 2.要给builder设置属性, 对话框的内容,样式,按钮
	 * 3.通过builder 创建一个对话框
	 * 4.对话框show()出来
	 */
	protected void showUpdataDialog() {
		AlertDialog.Builder builer = new Builder(this);
		builer.setTitle("版本升级");
		builer.setMessage(info.getDescription());
		// 当点确定按钮时从服务器上下载 新的apk 然后安装
		builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Log.i(TAG, "下载apk,更新");
				downLoadApk();
			}
		});
		// 当点取消按钮时进行登录
		builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				LoginMain();
				Log.i(TAG, "取消");
			}
		});
		AlertDialog dialog = builer.create();
		dialog.show();
	}
	
	/*
	 * 从服务器中下载APK
	 */
	protected void downLoadApk() {
		final ProgressDialog pd; // 进度条对话框
		pd = new ProgressDialog(AutoUpdata.this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("正在下载更新");
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			Message msg = new Message();
			msg.what = SDCARD_NOMOUNTED;
			handler.sendMessage(msg);
		} else {
			pd.show();
			new Thread() {
				@Override
				public void run() {
					try {
						File file = DownLoadManager.getFileFromServer(info.getUrl(), pd);
						sleep(1000);
						installApk(file);
						pd.dismiss(); // 结束掉进度条对话框

					} catch (Exception e) {
						Message msg = new Message();
						msg.what = DOWN_ERROR;
						handler.sendMessage(msg);
						e.printStackTrace();
					}
				}
			}.start();
		}
	}
	
	// 安装apk
	protected void installApk(File file) {
		Intent intent = new Intent();
		// 执行动作
		intent.setAction(Intent.ACTION_VIEW);
		// 执行的数据类型
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}
	/* 
	 * 进入程序的主界面 
	 */  
	private void LoginMain(){  
	    Intent intent = new Intent(AutoUpdata.this,Buddha.class);  
	    startActivity(intent);  
	    //结束掉当前的activity   
	    this.finish();  
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
