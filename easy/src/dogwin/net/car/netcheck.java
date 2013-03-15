package dogwin.net.car;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

public class netcheck extends Activity{
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	public static boolean checkNet(Context context){
		//�峰�������杩��绠＄�瀵硅薄锛�����wi-fi绛���ョ�绠＄�锛�		
		try{
			ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if(connectivity != null){
				//�峰�缃��杩��绠＄����璞�				
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if(info != null && info.isConnected()){
					//�ゆ�褰��缃�����宸茶���					
					if(info.getState() == NetworkInfo.State.CONNECTED);
						return true;
				}
		   }
		}catch (Exception e){}
			return false;
	}
}
