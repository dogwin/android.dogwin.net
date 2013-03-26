package com.baidu.locTest;

import java.util.regex.Pattern;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;


import android.app.Activity;
import android.app.Service;
import android.os.Bundle;
import android.os.Process;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class mainActivity extends Activity {
	private TextView mTv = null;
	private EditText mSpanEdit;
	private EditText mCoorEdit;
	private CheckBox mGpsCheck;
	private CheckBox mPriorityCheck;
	private Button   mStartBtn;
	private Button	 mSetBtn;
	private Button 	 mLocBtn;
	private Button 	 mPoiBtn;
	private Button 	 mOfflineBtn;
	private CheckBox mIsAddrInfoCheck;
	private boolean  mIsStart;
	private static int count = 1;
	private Vibrator mVibrator01 =null;
	private LocationClient mLocClient;

	public static String TAG = "LocTestDemo";


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		mTv = (TextView)findViewById(R.id.textview);
		mSpanEdit = (EditText)findViewById(R.id.edit);
		mCoorEdit = (EditText)findViewById(R.id.coorEdit);
		mGpsCheck = (CheckBox)findViewById(R.id.gpsCheck);
		mPriorityCheck = (CheckBox)findViewById(R.id.priorityCheck);
		mStartBtn = (Button)findViewById(R.id.StartBtn);
		mLocBtn = (Button)findViewById(R.id.locBtn);
		mSetBtn = (Button)findViewById(R.id.setBtn);       
		mPoiBtn = (Button)findViewById(R.id.PoiReq);
		mOfflineBtn  = (Button)findViewById(R.id.offLineBtn);
		mIsAddrInfoCheck = (CheckBox)findViewById(R.id.isAddrInfocb);
		mIsStart = false;

		mLocClient = ((Location)getApplication()).mLocationClient;
		((Location)getApplication()).mTv = mTv;
		mVibrator01 =(Vibrator)getApplication().getSystemService(Service.VIBRATOR_SERVICE);
		((Location)getApplication()).mVibrator01 = mVibrator01;
		
		
		
		//��ʼ/ֹͣ��ť 
		mStartBtn.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!mIsStart) {
					setLocationOption();
					mLocClient.start();
					mStartBtn.setText("ֹͣ");
					mIsStart = true;

				} else {
					mLocClient.stop();
					mIsStart = false;
					mStartBtn.setText("��ʼ");
				} 
				Log.d(TAG, "... mStartBtn onClick... pid="+Process.myPid()+" count="+count++);
			}
		});

		//��λ��ť
		mLocBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mLocClient != null && mLocClient.isStarted()){
					setLocationOption();
					mLocClient.requestLocation();	
				}				
				else 
					Log.d(TAG, "locClient is null or not started");
				Log.d(TAG, "... mlocBtn onClick... pid="+Process.myPid()+" count="+count++);
				Log.d(TAG,"version:"+mLocClient.getVersion());
			}
		});

		//���ð�ť
		mSetBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setLocationOption();
			}
		});

		mPoiBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mLocClient.requestPoi();
			}
		});  
		
		//���߻�վ��λ��ť
		mOfflineBtn.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				int req = mLocClient.requestOfflineLocation();
				Log.d("req","req:"+req);
			}
		});
	}   

	@Override
	public void onDestroy() {
		mLocClient.stop();
		((Location)getApplication()).mTv = null;
		super.onDestroy();
	}

	//������ز���
	private void setLocationOption(){
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(mGpsCheck.isChecked());				//��gps
		option.setCoorType(mCoorEdit.getText().toString());		//������������
		option.setServiceName("com.baidu.location.service_v2.9");
		option.setPoiExtraInfo(mIsAddrInfoCheck.isChecked());	
		if(mIsAddrInfoCheck.isChecked())
		{
			option.setAddrType("all");
		}		
		if(null!=mSpanEdit.getText().toString())
		{
			boolean b = isNumeric(mSpanEdit.getText().toString());
			 if(b)
			{
				option.setScanSpan(Integer.parseInt(mSpanEdit.getText().toString()));	//���ö�λģʽ��С��1����һ�ζ�λ;���ڵ���1����ʱ��λ
			}
		}
//		option.setScanSpan(3000);
		
		if(mPriorityCheck.isChecked())
		{
			option.setPriority(LocationClientOption.NetWorkFirst);      //������������
		}
		else
		{
			option.setPriority(LocationClientOption.GpsFirst);        //�����ã�Ĭ����gps����
		}

		option.setPoiNumber(10);
		option.disableCache(true);		
		mLocClient.setLocOption(option);
	}

	protected boolean isNumeric(String str) {   
		Pattern pattern = Pattern.compile("[0-9]*");   
		return pattern.matcher(str).matches();   
	}  


}