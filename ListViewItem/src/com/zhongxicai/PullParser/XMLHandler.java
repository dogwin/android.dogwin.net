package com.zhongxicai.PullParser;

import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.zhongxicai.Beans.BookRank;
/**SAX����*/
public class XMLHandler extends DefaultHandler{

	 BookRank book;
	 List<BookRank> booklist;
	 String preTag;
	
	public XMLHandler(){
		super();
	}

	public XMLHandler(List<BookRank> booklist){
		super();
		this.booklist= booklist;	
	}
	/**
	 * ��ʼ��ȡ�ļ�
	 * */
	public void startDocument(String uri, String localName, String qName,Attributes attributes) throws SAXException{
		super.startDocument();
	}
	
	/**
	 * ��ʼ��ȡԪ��
	 * */
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException{
		preTag=localName;
		if(localName.equals("rankNum")){
			book=new BookRank();
			book.setrankNum(Integer.parseInt(attributes.getValue(0)));
			for(int i=0;i<attributes.getLength();i++)
			{	
			}
		}
		super.startElement(uri, localName, qName, attributes);
	}
	/**
	 * ��ȡ�������
	 * */
	public void characters(char[] ch, int start,int length) throws SAXException{
		String dataString;
		if(preTag.equals("name")){
			dataString=new String(ch,start,length);
			book.setname(dataString);
		}
		else if(preTag.equals("author")){
			dataString=new String(ch,start,length);
			book.setauthor(dataString);
		}
       else if(preTag.equals("clickNum")){
    	   dataString=new String(ch,start,length);
			book.setclickNum(Integer.parseInt(dataString));
		}
       else if(preTag.equals("downNum")){
    	   dataString=new String(ch,start,length);
			book.setdownNum(Integer.parseInt(dataString));
		}
       else if(preTag.equals("imgurl")){
    	   dataString=new String(ch,start,length);
			book.setimgUrl(dataString);
		}
       else if(preTag.equals("downLoadUrl")){
    	   dataString=new String(ch,start,length);
			book.setdownLoadUrl(dataString);
		}
       else if(preTag.equals("readOnlineUrl")){
    	   dataString=new String(ch,start,length);
    	   book.setreadOnlineUrl(dataString);
		}
       else if(preTag.equals("contentUrl")){
    	   dataString=new String(ch,start,length);
    	   book.setcontentUrl(dataString);
		}
		 super.characters(ch, start, length);
	}
	/**
	 * ������ȡԪ��
	 * */
	public void endElement(String uri, String localName, String qName) throws SAXException{
		preTag="";
		if(localName.equals("rankNum")){
			booklist.add(book);
		}
		super.endElement(uri, localName, qName);
	}
	/**
	 * ������ȡ�ļ�
	 * */
	   public void endDocument() throws SAXException{
		   super.endDocument();
	   } 
	
   /**
    * ��ȡ booklist
    * */
	public List<BookRank> getBookRank(){
		return booklist;
	}
	/**
	 * ���� booklist
	 * */
	public void setBookRank(List<BookRank> booklist){
		this.booklist=booklist;
	}
	
}







