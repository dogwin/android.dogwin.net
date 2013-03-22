package com.zhongxicai.Beans;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zhongxicai.R;
import com.zhongxicai.AllListener.OnReadOnlineTextBarListener;

public class TextBar extends RelativeLayout{

	TextView booktext;
	Button middlebtn;
	ScrollView scrollview;
	OnReadOnlineTextBarListener  textbarlistener;
	public TextBar(Context context) {
		super(context);
	}
	public TextBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       inflater.inflate(R.layout.textbar,this);
       middlebtn=(Button)findViewById(R.id.middlebtn);
       booktext=(TextView)findViewById(R.id.booktext);
       scrollview=(ScrollView)findViewById(R.id.scrollview);
	}
    public void setOnReadOnlineTextBarListener(OnReadOnlineTextBarListener  textbarlistener){
    	this.textbarlistener=textbarlistener;
    	middlebtn.setOnClickListener(new BarOnClickListener());
    	 booktext.setOnClickListener(new BarOnClickListener());
    	 booktext.setOnTouchListener(new BarOnTouchListener());
    } 
    public void setBarText(String text){
    	booktext.setText(text);
    	scrollview.scrollTo(0, 0);
    }
   //////////////
	class BarOnClickListener implements OnClickListener{
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.middlebtn:
				textbarlistener.onClickMiddlebtnListener(TextBar.this);
				break;
			case R.id.booktext:
				textbarlistener.onClickTextListener(TextBar.this);
				break;
			}
		}		
	}
    ////////////
	class BarOnTouchListener implements OnTouchListener{

		public boolean onTouch(View v, MotionEvent event) {
			textbarlistener.onTouchTextListener(TextBar.this);
			return true;
		}	
	}
}
