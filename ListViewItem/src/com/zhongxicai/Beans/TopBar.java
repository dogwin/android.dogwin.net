package com.zhongxicai.Beans;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.zhongxicai.R;
/**
 * ��--��һ���ı��ؼ�---����ֻ��Ϊ�˽����չ����--������
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
