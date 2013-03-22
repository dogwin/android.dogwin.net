package com.zhongxicai.Beans;

import com.zhongxicai.AllListener.OnReadOnlineBottonBarListener;
import com.zhongxicai.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;


public class ReadOnlineBottomBar extends RelativeLayout{
    Button prepagebtn,nextpagebtn,contentbtn;
    OnReadOnlineBottonBarListener buttonListener;
    public ReadOnlineBottomBar(Context context) {
		super(context);
    }
    
	public ReadOnlineBottomBar(Context context,AttributeSet attrs) {
		super(context,attrs);
		LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.readonlinebottombar, this);		
		prepagebtn=(Button)findViewById(R.id.prepagebtn);
		nextpagebtn=(Button)findViewById(R.id.nextpagebtn);
		contentbtn=(Button)findViewById(R.id.contentbtn);
		
	}
	
	public void setOnReadOnlineBottonBarListener(OnReadOnlineBottonBarListener buttonListener){
		this.buttonListener=buttonListener;
		prepagebtn.setOnClickListener(new BottomBarButtonOnClickListener());
		nextpagebtn.setOnClickListener(new BottomBarButtonOnClickListener());
		contentbtn.setOnClickListener(new BottomBarButtonOnClickListener());
	}
  class BottomBarButtonOnClickListener implements OnClickListener{
	
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.prepagebtn:
			buttonListener.onClickPrepagebtn(ReadOnlineBottomBar.this);
			break;
		case R.id.nextpagebtn:
			buttonListener.onCLickNextpagebtn(ReadOnlineBottomBar.this);
			break;
		case R.id.contentbtn:
			buttonListener.onClickContentbtn(ReadOnlineBottomBar.this);
			break;
		}
	}  
  }
  
}
