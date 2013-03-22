package com.zhongxicai.Beans;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.zhongxicai.R;
/**
 * 汗--就一个文本控件---哎，只是为了今后扩展功能--勿怪勿怪
 * */
public class TopBar extends RelativeLayout{
	TextView titletext;
	
	public TopBar(Context context) {
		super(context);
	}
	public TopBar(Context context, AttributeSet attrs) {
		super(context, attrs);
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.topbar, this);
        titletext=(TextView)findViewById(R.id.titletext);
	}
   public void setTitleText(String title){
	   titletext.setText(title);
   }
	

}
