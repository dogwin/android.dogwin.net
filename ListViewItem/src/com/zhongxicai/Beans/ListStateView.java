package com.zhongxicai.Beans;

import com.zhongxicai.AllListener.OnListStateViewListener;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.zhongxicai.R;

public class ListStateView extends RelativeLayout{
	Button buttuon;
    TextView textview;
    OnListStateViewListener viewlistener;
 
    private int flag;
    public  ListStateView(Context context) {
		super(context);
	}
    
    public void setOnOpenListViewListener(OnListStateViewListener viewlistener){
    	this.viewlistener=viewlistener;
    	ListStateView.this.setOnClickListener(new ViewOnClickListener()); 
    	 buttuon.setOnClickListener(new ViewOnClickListener());
    }
    
	
	public ListStateView(Context context, AttributeSet attrs) {
		super(context,attrs);
		LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    inflater.inflate(R.layout.liststate, this);
	    buttuon=(Button)findViewById(R.id.openlistbtn);
	    textview=(TextView)findViewById(R.id.openlistviewtitle);
	    buttuon.setBackgroundResource(R.drawable.closed);
	    flag=0;
	}
	
	public void setTitleText(String title){
		 textview.setText(title);
	}
	public void setOpenState(int flag){
		this.flag=flag;
		switch(flag){
		case 0:
			buttuon.setBackgroundResource(R.drawable.closed);
			break;
		case 1:
			buttuon.setBackgroundResource(R.drawable.opened);
			break;
		}
		
	}
    class ViewOnClickListener implements OnClickListener{
		public void onClick(View v) {
			switch(flag){
			case 0:
				setOpenState(1);
				viewlistener.onClickOpenView(ListStateView.this);
				break;
			case 1:
				setOpenState(0);
				viewlistener.onClickCloseView(ListStateView.this);
				break;
			}
			
		}	
    }
}
