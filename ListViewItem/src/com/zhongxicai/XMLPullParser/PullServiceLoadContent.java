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
				System.out.println("��ȡ���½�"+chapter.getChapterId());////////////
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
				System.out.println("���һ��"+content.size());
				chapter=null;
			}
			break;
		}
		event=parser.next();
	}
	System.out.println("��������½�����"+content.size());
	return content;
}
public List<BookContent> getBookContent(String contenturlstr){
	List<BookContent> content=null;
	InputStream inputstream = null;
	BookHttpDownload httpload=new BookHttpDownload();
	System.out.println("��ʼ��ȡ��������");
	inputstream=httpload.getInputStream(contenturlstr);	
	try {
		content=getContentFromInput(inputstream);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	System.out.println("��ȡ���---"+content.size());
	return content;
}



}