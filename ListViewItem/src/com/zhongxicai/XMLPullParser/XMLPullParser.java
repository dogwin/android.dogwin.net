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
 * pull����----�ؼ����ڰѽ�������תΪInputStream����
 * */
public class XMLPullParser {
	Context context;
	public XMLPullParser(Context context){
		this.context=context;
	}
	
/**
 * ����raw�е��ı�,����List<BookRank>
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
	 * ����assets�е��ı�,����List<BookRank>
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
		 * ����String����,����List<BookRank>
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
		 * �������ص��ı�--����1---InputStream,����List<BookRank>
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
			 * �������ص��ı�--����2---��ȡstring,�ٽ���,����List<BookRank>---�������һ�١���ֻ��Ϊ�˲���
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
		 * ��������--��1,����List<BookRank>
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
			 * ��������----��2,����List<BookRank>
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
