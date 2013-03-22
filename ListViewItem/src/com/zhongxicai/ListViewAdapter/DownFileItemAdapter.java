package com.zhongxicai.ListViewAdapter;

import java.util.List;

import com.zhongxicai.DownlistActivity;
import com.zhongxicai.R;
import com.zhongxicai.Beans.DownloadInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DownFileItemAdapter extends BaseAdapter{


    private  DownlistActivity context;
	private List<DownloadInfo> downlist;
	private LayoutInflater inflater;
	public DownFileItemAdapter(DownlistActivity context,List<DownloadInfo> downlist){
		this.context=context;
		this.downlist=downlist;
		inflater=LayoutInflater.from(context);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return downlist.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return downlist.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	

	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		
		if(convertView==null){
			holder=new ViewHolder();
			convertView=inflater.inflate(R.layout.down_item,null);
			holder.filenametext=(TextView)convertView.findViewById(R.id.filenametext);
			holder.progresstext=(TextView)convertView.findViewById(R.id.progresstext);
			holder.idnumtext=(TextView)convertView.findViewById(R.id.idtext);
			convertView.setTag(holder);
		}
		else{
			holder=(ViewHolder)convertView.getTag();
		}
		holder.filenametext.setText(downlist.get(position).getfilename());
		holder.idnumtext.setText(""+(position+1));
	//	holder.progresstext.setText(""+downlist.get(position).getprogress());
		return convertView;
	}
	 private class ViewHolder{
		  private TextView filenametext;
		  private TextView idnumtext;
		  private TextView progresstext;
	  }

}

















