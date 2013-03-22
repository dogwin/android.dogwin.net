package com.zhongxicai.XMLPullParser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import android.content.Context;
import com.zhongxicai.Beans.BookRank;
import com.zhongxicai.ReadLocalFile.ReadLocalText;
import com.zhongxicai.ReadLocalFile.ReadSourceText;
import com.zhongxicai.UrlDownLoad.BookHttpDownload;
/**
 * pull解析----关键在于把解析对象转为InputStream对象
 * */
public class XMLPullParser {
	Context context;
	public XMLPullParser(Context context){
		this.context=context;
	}
	
/**
 * 解析raw中的文本,返回List<BookRank>
 * */
	public List<BookRank> getRawBookList(int Id){
		List<BookRank> booklist=null;
		InputStream inputstream=null;
		ReadSourceText readsourtext= new ReadSourceText(context);
		inputstream=readsourtext.readRawToInputStream(Id);
		try {
			booklist=PullService.getBookList(inputstream);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return booklist;
		
	}
	/**
	 * 解析assets中的文本,返回List<BookRank>
	 * */
		public List<BookRank> getAssetsBookList(String name){
			List<BookRank> booklist=null;
			InputStream inputstream=null;
			ReadSourceText readsourtext= new ReadSourceText(context);
			inputstream=readsourtext.readAssetsToInputStream(name);
			try {
				booklist=PullService.getBookList(inputstream);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			return booklist;
			
		}
		/**
		 * 解析String对象,返回List<BookRank>
		 * */
			public List<BookRank> getStringBookList(String string){
				List<BookRank> booklist=null;
				InputStream inputstream=null;
				byte   b[]   =   string.getBytes(); 
			    inputstream  =   new   ByteArrayInputStream(b);
				try {
					booklist=PullService.getBookList(inputstream);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
				return booklist;		
			}
		/**
		 * 解析本地的文本--方法1---InputStream,返回List<BookRank>
		 * */
			public List<BookRank> getLocalInputBookList(String filepath){
				List<BookRank> booklist=null;
				InputStream inputstream=null;
				ReadLocalText readlocaltext=new ReadLocalText();
				inputstream=readlocaltext.readFileToInputstream(filepath);
				try {
					booklist=PullService.getBookList(inputstream);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
				return booklist;
				
			}
			/**
			 * 解析本地的文本--方法2---读取string,再解析,返回List<BookRank>---汗，多此一举。。只是为了测试
			 * */
				public List<BookRank> getLocalStrBookList(String filepath){
					List<BookRank> booklist=null;
					String string=null;
					ReadLocalText readlocaltext=new ReadLocalText();
					string=readlocaltext.readFileToString(filepath);
					
					booklist=getStringBookList(string);
					
					return booklist;
					
				}
		
		/**
		 * 解析网络--法1,返回List<BookRank>
		 * */
			public List<BookRank> getNetInputBookList(String urlstr){
				List<BookRank> booklist=null;
				InputStream inputstream=null;
				BookHttpDownload httpload=new BookHttpDownload();
				inputstream=httpload.getInputStream(urlstr);
				try {
					booklist=PullService.getBookList(inputstream);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
				return booklist;
				
			}
			/**
			 * 解析网络----法2,返回List<BookRank>
			 * */
				public List<BookRank> getNetStrBookList(String urlstr){
					List<BookRank> booklist=null;
					String string=null;
					BookHttpDownload httpload=new BookHttpDownload();
					string=httpload.getUrlString(urlstr);
					booklist=getStringBookList(string);
					return booklist;
					
				}
			
		
}
