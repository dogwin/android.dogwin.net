package com.zhongxicai;

import java.util.ArrayList;
import java.util.List;

import com.zhongxicai.AllListener.OnListStateViewListener;
import com.zhongxicai.Beans.DownloadInfo;
import com.zhongxicai.Beans.ListStateView;
import com.zhongxicai.ListViewAdapter.DownFileItemAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class DownlistActivity extends Activity implements OnListStateViewListener{
	private DownloadInfo downinfo;
	private List<DownloadInfo> downlist;
	private ListStateView currentdownview,overdownview;
	private ListView currentdownlistview,overdownlistview;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.downlist);
        currentdownview=(ListStateView)findViewById(R.id.currentdownview);
        overdownview=(ListStateView)findViewById(R.id.overdownview);
        currentdownlistview=(ListView)findViewById(R.id.currentdownlistview);
        overdownlistview=(ListView)findViewById(R.id.overdownlistview);
        currentdownview.setOnOpenListViewListener(this);
        overdownview.setOnOpenListViewListener(this);
        }

	public void onClickOpenView(View v) {
		switch(v.getId()){
		case R.id.currentdownview:
			showcurrentdown();
			break;
		case R.id.overdownview:
			showoverdown();
			break;
		}
	}

	public void onClickCloseView(View v) {
       switch(v.getId()){
       case R.id.currentdownview:
    	   hidecurrentdown();
			break;
		case R.id.overdownview:
			hideoverdown();
			break;
		}
	}
	
	
	
///////////////////////<-----------≤‚ ‘----------------°∑
public void showcurrentdown(){
if(downinfo==null){
downinfo=new DownloadInfo();
downinfo.setfilename("≤‚ ‘");
downlist=new ArrayList<DownloadInfo>();
downlist.add(downinfo);
downlist.add(downinfo);
downlist.add(downinfo);
downlist.add(downinfo);
}
DownFileItemAdapter adapter=new  DownFileItemAdapter(this, downlist);
currentdownlistview.setAdapter(adapter);    

currentdownlistview.setVisibility(View.VISIBLE);
}

public void hidecurrentdown(){
currentdownlistview.setVisibility(View.GONE);
}

public void showoverdown(){
if(downinfo==null){
downinfo=new DownloadInfo();
downinfo.setfilename("≤‚ ‘");
downlist=new ArrayList<DownloadInfo>();
downlist.add(downinfo);
downlist.add(downinfo);
downlist.add(downinfo);
downlist.add(downinfo);
}
DownFileItemAdapter adapter=new  DownFileItemAdapter(this, downlist);
overdownlistview.setAdapter(adapter);    

overdownlistview.setVisibility(View.VISIBLE);
}

public void hideoverdown(){
overdownlistview.setVisibility(View.GONE);
}

///////////////////////////
}
