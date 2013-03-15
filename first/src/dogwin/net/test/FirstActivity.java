package dogwin.net.test;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;



public class FirstActivity extends Activity {
    /** Called when the activity is first created. */
	test t = new test();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //testConnectivityManager();
        //Log.i("通知", "GPRS网络已连接");
        //test();
        /*if(t.conn()){
        	Log.i("net", "connected");
        }else{
        	Log.i("net", "didn't connected");
        }*/
        /*
        if(t.a()){
        	Log.i("net", "connected");
        }*/
        if(t.checkNet(this)){
        	Log.i("net", "connected");
        }else{
        	Log.i("net", "didn't connected");
        }
    }
}