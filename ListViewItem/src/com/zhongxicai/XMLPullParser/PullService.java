package com.zhongxicai.XMLPullParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

import com.zhongxicai.Beans.BookRank;

public class PullService {
	/**
	 * ����inputstream���󣬷���List<BookRank>
	 * */
public static List<BookRank> getBookList(InputStream is) throws Exception{
	List<BookRank> booklist=null;
	BookRank book=null;
	XmlPullParser parser=Xml.newPullParser();
	parser.setInput(is,"UTF-8");
	int event=parser.getEventType();
	while(event!=XmlPullParser.END_DOCUMENT){
		switch(event){
		case XmlPullParser.START_DOCUMENT:
			booklist=new ArrayList<BookRank>();
			break;
		case XmlPullParser.START_TAG:
			if("rankNum".equals(parser.getName())){
				int id=Integer.parseInt(parser.getAttributeValue(0));
				book=new BookRank();
				book.setrankNum(id);
			}
			else if(book!=null){
				if("name".equals(parser.getName())){
					String name=parser.nextText();
					book.setname(name);
					System.out.println("<--"+name+"-->");
				}else if("author".equals(parser.getName())){
					String author=parser.nextText();
					book.setauthor(author);
				}else if("clickNum".equals(parser.getName())){
					int clickNum=Integer.parseInt(parser.nextText());
					book.setclickNum(clickNum);
				}else if("downNum".equals(parser.getName())){
					int downNum=Integer.parseInt(parser.nextText());
					book.setdownNum(downNum);
				}else if("downLoadUrl".equals(parser.getName())){
					String downLoadUrl=parser.nextText();
					book.setdownLoadUrl(downLoadUrl);
				}else if("readOnlineUrl".equals(parser.getName())){
					String readOnlineUrl=parser.nextText();
					book.setreadOnlineUrl(readOnlineUrl);
				}else if("imgUrl".equals(parser.getName())){
					String imgUrl=parser.nextText();
					book.setimgUrl(imgUrl);
				}else if("contentUrl".equals(parser.getName())){
					String contentUrl=parser.nextText();
					book.setcontentUrl(contentUrl);
				}
				
		      }
			break;
		case XmlPullParser.END_TAG:
			if("rankNum".equals(parser.getName())){
				booklist.add(book);
				book=null;
			}
			break;
		}
		event=parser.next();
	}
	return booklist;
}

     /**
	 * ����xml�ڣ����浽OutputStream����
	 * */
	public static void save(List<BookRank> booklist,OutputStream os) throws Exception, IllegalStateException, IOException {
		XmlSerializer serializer=Xml.newSerializer();
		serializer.setOutput(os,"UTF-8");
		serializer.startDocument("UTF-8", true);
		serializer.startTag(null, "ranks");
		for(BookRank book:booklist){
			serializer.startTag(null, "rankNum");
			serializer.attribute(null,"id", String.valueOf(book.getrankNum()));
			
			serializer.startTag(null, "name");
			serializer.text(book.getname());
			serializer.endTag(null, "name");
			
			  serializer.startTag(null, "author");
			  serializer.text(book.getauthor());
			  serializer.endTag(null, "author");
			
			 serializer.startTag(null, "clickNum");
			 serializer.text(String.valueOf(book.getclickNum()));
			 serializer.endTag(null, "clickNum");
			
			 serializer.startTag(null, "downNum");
			 serializer.text(String.valueOf(book.getdownNum()));
			 serializer.endTag(null, "downNum");
			
			 serializer.startTag(null, "downLoadUrl");
			 serializer.text(book.getdownLoadUrl());
			 serializer.endTag(null, "downLoadUrl");
			
			 serializer.startTag(null, "readOnlineUrl");
			 serializer.text(book.getreadOnlineUrl());
			 serializer.endTag(null, "readOnlineUrl");
			
			 serializer.startTag(null, "imgUrl");
			 serializer.text(book.getimgUrl());
			 serializer.endTag(null, "imgUrl");
			
	    	serializer.endTag(null, "rankNum");
		}
		serializer.endTag(null, "ranks");
		serializer.endDocument();
		os.flush();
		os.close();
	}
	
}









