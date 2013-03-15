package dogwin.net.test;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Bundle;

public class test extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	public boolean a(){
		//Log.i("通知", "GPRS网络已连接");
		//return connectd();
		return true;
	}
	public boolean conn(){
    	ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    	//mobile
    	State mobile = conMan.getNetworkInfo(0).getState();
    	//wifi
    	State wifi = conMan.getNetworkInfo(1).getState();
    	if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING) {
    	    //mobile
    		//Log.i("mobile", "connected");
    		return true;
    	}else if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
    	    //wifi
    		//Log.i("wifi", "connected");
    		return true;
    	}else{
    		//return true;Log.i("sorry", "please check the net");
    		return false;
    	}
    }
	public static boolean checkNet(Context context){
		//获得手机所有连接管理对象（包括对wi-fi等连接的管理）
		try{
			ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if(connectivity != null){
				//获得网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if(info != null && info.isConnected()){
					//判断当前网络是否已连接
					if(info.getState() == NetworkInfo.State.CONNECTED);
						return true;
				}
		   }
		}catch (Exception e){}
			return false;
	}
}
