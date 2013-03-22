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
     * ��������
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
	 * ��ʼ���ؼ�
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
	 * �Ķ������ذ�ť������
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
   * ������ʾ�����а�����
   * */
 public void setranknumtext(int ranknum){
	 ranknumtext.setText(String.valueOf(ranknum));
  }
 /**
  * ������ʾ���鱾����
  * */
public void setimgviewBitmap(Bitmap bitmap){
	imgview.setImageBitmap(bitmap);
  }
/**
 * ������ʾ���鱾����
 * */
public void setimgviewDrawable(Drawable drawable){
	imgview.setImageDrawable(drawable);
 }
/**
 * ������ʾ������
 * */
public void setnametext(String name){
	nametext.setText("��  ��:"+name);
}
/**
 * ������ʾ��������
 * */
public void setauthortext(String author){
	authortext.setText("��  ��:"+author);
}
/**
 * ������ʾ�ĵ����
 * */
public void setclicknumtext(int clicknum){
	clicknumtext.setText("�����:"+String.valueOf(clicknum));
}
/**
 * ������ʾ��������
 * */
public void setdownnumtext(int downnum){
	downnumtext.setText("������:"+String.valueOf(downnum));
}
/**
 *����book����
 * */
public void setbook(BookRank book){
	this.book=book;
}	
/**
 *��ȡbook����
 * */
public BookRank gettbook(){
	return book;
}

}












