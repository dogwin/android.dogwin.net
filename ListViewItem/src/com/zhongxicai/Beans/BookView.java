package com.zhongxicai.Beans;

import com.zhongxicai.R;
import com.zhongxicai.AllListener.OnClickBookViewListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BookView extends RelativeLayout{
	 BookRank book;
	 TextView ranknumtext;
	 TextView nametext;
	 TextView authortext;
	 TextView clicknumtext;
	 TextView downnumtext;
	 ImageView imgview;
	 Button readonlinebtn;
	 Button downloadbtn;
	 OnClickBookViewListener bookViewListener;
    /**
     * 监听方法
     * */
	public void setOnClickBookViewListener(OnClickBookViewListener bookViewListener){
		this.bookViewListener=bookViewListener;
		readonlinebtn.setOnClickListener(new ButtonOnClickListener());
		downloadbtn.setOnClickListener(new ButtonOnClickListener());
	}
	//////////////
	public BookView(Context context) {
		super(context);
	}
	/**
	 * 初始化控件
	 * */
	public BookView(Context context, AttributeSet attrs){
		super(context,attrs);
		 LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 inflater.inflate(R.layout.listviewitem, this);
		 
		ranknumtext=(TextView)findViewById(R.id.ranknumtext);
		nametext=(TextView)findViewById(R.id.nametext);
		authortext=(TextView)findViewById(R.id.authortext);
	    clicknumtext=(TextView)findViewById(R.id.clicknumtext);
		downnumtext=(TextView)findViewById(R.id.downnumtext);
		imgview=(ImageView)findViewById(R.id.imgview);
		readonlinebtn=(Button)findViewById(R.id.readonlinebtn);
		downloadbtn=(Button)findViewById(R.id.downloadbtn);
	}
	/**
	 * 阅读和下载按钮监听类
	 * */
  class ButtonOnClickListener implements OnClickListener{

	public void onClick(View v) {
	   switch(v.getId()){
	   case R.id.readonlinebtn:
		   bookViewListener.onClickReadonlinebtn(BookView.this);
		   break;
	   case R.id.downloadbtn:
		   bookViewListener.onClickDownloadbtn(BookView.this);
		   break;
	   }
	}	   
  }
  /**
   * 设置显示的排行榜名次
   * */
 public void setranknumtext(int ranknum){
	 ranknumtext.setText(String.valueOf(ranknum));
  }
 /**
  * 设置显示的书本封面
  * */
public void setimgviewBitmap(Bitmap bitmap){
	imgview.setImageBitmap(bitmap);
  }
/**
 * 设置显示的书本封面
 * */
public void setimgviewDrawable(Drawable drawable){
	imgview.setImageDrawable(drawable);
 }
/**
 * 设置显示的书名
 * */
public void setnametext(String name){
	nametext.setText("书  名:"+name);
}
/**
 * 设置显示的作者名
 * */
public void setauthortext(String author){
	authortext.setText("作  者:"+author);
}
/**
 * 设置显示的点击量
 * */
public void setclicknumtext(int clicknum){
	clicknumtext.setText("点击量:"+String.valueOf(clicknum));
}
/**
 * 设置显示的下载量
 * */
public void setdownnumtext(int downnum){
	downnumtext.setText("下载量:"+String.valueOf(downnum));
}
/**
 *设置book对象
 * */
public void setbook(BookRank book){
	this.book=book;
}	
/**
 *获取book对象
 * */
public BookRank gettbook(){
	return book;
}

}












