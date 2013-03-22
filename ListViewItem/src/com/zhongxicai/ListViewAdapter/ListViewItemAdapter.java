package com.zhongxicai.ListViewAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.zhongxicai.ListViewItem;
import com.zhongxicai.R;
import com.zhongxicai.AllListener.OnClickBookViewListener;
import com.zhongxicai.Beans.BookRank;
import com.zhongxicai.Beans.BookView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.zhongxicai.UrlDownLoad.AsyncImageLoader;
import com.zhongxicai.UrlDownLoad.AsyncImageLoader.ImageCallback;


public class ListViewItemAdapter extends BaseAdapter{
	 ListViewItem context;
	List<BookRank>  booklist;
    LayoutInflater inflater;
    OnAdapterItemLisenter onAdapterItemLisenter;
    AsyncImageLoader  imageLoader=new AsyncImageLoader();
    
	public ListViewItemAdapter(){
		super();
	}

	
	public ListViewItemAdapter(ListViewItem context,List<BookRank> booklist,OnAdapterItemLisenter onAdapterItemLisenter){
		this.context=context;
		this.booklist=booklist;
		this.onAdapterItemLisenter=onAdapterItemLisenter;
		inflater=LayoutInflater.from(context);
	}


	public int getCount() {
		return booklist.size();
	}

	
	public Object getItem(int position) {
		return booklist.get(position);
	}

	
	public long getItemId(int position) {
		return  position;
	}
	private Map<Integer, View> viewMap = new HashMap<Integer, View>();
	public View getView(final int position, View convertView, ViewGroup parent) {	
		View rowview=this.viewMap.get(position);;
		if(rowview==null){
			rowview=inflater.inflate(R.layout.item, null);  
			final BookRank book=booklist.get(position);
			final BookView	bookview=(BookView)rowview.findViewById(R.id.bookview); 
		    bookview.setnametext(book.getname());
			bookview.setauthortext(book.getauthor());
			bookview.setranknumtext(book.getrankNum());
			bookview.setclicknumtext(book.getclickNum());
			bookview.setdownnumtext(book.getdownNum()); 
		    bookview.setimgviewBitmap(book.getbitmap());
			if(book.getbitmap()==null){
			    imageLoader.loadBitmap(book.getimgUrl(), new ImageCallback(){
					public void imageLoaded(String imageUrl, Bitmap bitmap) {
						book.setbitmap(bitmap);
	                   bookview.setimgviewBitmap(book.getbitmap());
					}		
			    });
			}
			bookview.setOnClickBookViewListener(new OnClickBookViewListener(){
					public void onClickReadonlinebtn(View v) {
						
						onAdapterItemLisenter.onAdapterClickReadbtn(position);
					}
					public void onClickDownloadbtn(View v) {
						onAdapterItemLisenter.onAdapterClickDownbtn(position);
					}					
				});    
			viewMap.put(position, rowview);
		}
		return rowview;
	}
	 
  public interface OnAdapterItemLisenter{
	  /**
		 * ÔÚÏßÔÄ¶Á
		 * */
	  public void onAdapterClickReadbtn(int position);
	  /**
		 * ÏÂÔØ
		 * */
	  public void onAdapterClickDownbtn(int position);
  }
	
	
}








