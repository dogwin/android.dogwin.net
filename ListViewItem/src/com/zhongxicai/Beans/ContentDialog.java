package com.zhongxicai.Beans;

import java.util.ArrayList;
import java.util.List;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.zhongxicai.BookReadOnLine;
import com.zhongxicai.R;
import android.view.View.OnClickListener;

public class ContentDialog extends Dialog implements OnClickListener{
   List<BookContent> content;
   List<String> list;
   BookReadOnLine rookReadOnLine;
	ListView listview;
	Button backbtn;
	OnContentItemListener onContentItemListener;
	public ContentDialog(Context context) {
		super(context);
	}
	public ContentDialog(BookReadOnLine  rookReadOnLine, List<BookContent> content,OnContentItemListener onContentItemListener) {
		super(rookReadOnLine);
		this.content=content;
		this.rookReadOnLine=rookReadOnLine;
		this.onContentItemListener=onContentItemListener;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.contentdialog);
		listview=(ListView)findViewById(R.id.contentlistview);
		backbtn=(Button)findViewById(R.id.contentbackbtn);
		
		backbtn.setOnClickListener(this);
		listview.setOnItemClickListener(new contentItemOnClickListener());	
		list=new ArrayList<String>();
		
	   for(int i=0;i<content.size();i++){
		   list.add(content.get(i).getChapterName());
	   }
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(rookReadOnLine,android.R.layout.simple_list_item_1,list);
	   listview.setAdapter(adapter);

	}	

	class contentItemOnClickListener implements OnItemClickListener{
		public void onItemClick(AdapterView<?> arg0, View v, int position,long id) {
			onContentItemListener.onContentItemClickListener(position);
		}
	}

	public void onClick(View v) {
		ContentDialog.this.dismiss();	
	}
    
	public interface OnContentItemListener{
		public void onContentItemClickListener(int position);
	}
}
