package com.zhongxicai.XMLPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import android.util.Xml;
import com.zhongxicai.Beans.BookContent;
import com.zhongxicai.UrlDownLoad.BookHttpDownload;


public class PullServiceLoadContent {
	 
public static List<BookContent> getContentFromInput(InputStream is) throws Exception{
	List<BookContent> content=null;
	BookContent chapter=null;
	XmlPullParser parser=Xml.newPullParser();
	parser.setInput(is,"UTF-8");
	int event=parser.getEventType();
	while(event!=XmlPullParser.END_DOCUMENT){
		switch(event){
		case XmlPullParser.START_DOCUMENT:
			content=new ArrayList<BookContent>();
			break;
		case XmlPullParser.START_TAG:
			if("chapter".equals(parser.getName())){
				int id=Integer.parseInt(parser.getAttributeValue(0));
				chapter=new BookContent();
				chapter.setChapterId(id);
				System.out.println("读取到章节"+chapter.getChapterId());////////////
			}else if(chapter!=null){
				if("chaptername".equals(parser.getName())){
					String chaptername=parser.nextText();
					chapter.setChapterName(chaptername);
					System.out.println("<--"+chapter.getChapterName()+"-->");//////////////
				}else if("chapterurl".equals(parser.getName())){
					String chapterurl=parser.nextText();
					chapter.setChapterUrl(chapterurl);
					System.out.println("<--"+chapter.getChapterUrl()+"-->");//////////////
				}
		      }
			break;
		case XmlPullParser.END_TAG:
			if("chapter".equals(parser.getName())){
				content.add(chapter);
				System.out.println("获得一章"+content.size());
				chapter=null;
			}
			break;
		}
		event=parser.next();
	}
	System.out.println("获得所有章节数据"+content.size());
	return content;
}
public List<BookContent> getBookContent(String contenturlstr){
	List<BookContent> content=null;
	InputStream inputstream = null;
	BookHttpDownload httpload=new BookHttpDownload();
	System.out.println("开始读取网络数据");
	inputstream=httpload.getInputStream(contenturlstr);	
	try {
		content=getContentFromInput(inputstream);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	System.out.println("读取完毕---"+content.size());
	return content;
}



}