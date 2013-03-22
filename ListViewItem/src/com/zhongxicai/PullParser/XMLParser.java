package com.zhongxicai.PullParser;

import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.zhongxicai.Beans.BookRank;
import com.zhongxicai.ReadLocalFile.ReadLocalText;
import com.zhongxicai.ReadLocalFile.ReadSourceText;
import com.zhongxicai.UrlDownLoad.BookHttpDownload;

import android.content.Context;
/**
 * SAX����----�ؼ����ڰѽ�������תΪString �� Inputstream ����
 * InputSource(StringReader reader);תΪInputSource����--->���ɽ�����֪��String����
 * InputSource(Inputstream inputstream);תΪInputSource����--->���ɽ���xml�ı������߻�õ�����Inputstream
 * */
public class XMLParser {
    private Context context;
	
	public XMLParser (){
		super();
	}
	public XMLParser (Context context){
		this.context=context;
	}
	
	/**
	 * ����String����return  booklist;
	 * */
    public List<BookRank> getStringBookRank(String string){
    	SAXParserFactory factory = SAXParserFactory.newInstance();
    	SAXParser parser;
    	List<BookRank> booklist=new ArrayList<BookRank>();
    	BookRank book=null;
    	try{
    		parser=factory.newSAXParser();
    		XMLReader reader=parser.getXMLReader();
    		booklist=new ArrayList<BookRank>();
    		XMLHandler XMLhandler=new XMLHandler(booklist);
    		reader.setContentHandler(XMLhandler);
    		StringReader strreader=new StringReader(string); 
    		InputSource inputsource=new InputSource(strreader);
    		reader.parse(inputsource);
    	    for(Iterator<BookRank> iterator = booklist.iterator();iterator.hasNext();){ 	
    	    	book=(BookRank)booklist.iterator();
    	    }
    	   booklist.add(book);
    	}
    	catch(Exception e){
    	}
     	return  booklist;
    }   
    
    /**
	 * ����raw�ı� ,return  booklist;
	 * */
    public List<BookRank> getRawBookRank(int Id){
    	SAXParserFactory factory = SAXParserFactory.newInstance();
    	List<BookRank> booklist=null;
    	BookRank book=null;
    	try{
    		XMLReader reader=factory.newSAXParser().getXMLReader();
    		booklist=new ArrayList<BookRank>();
    		reader.setContentHandler(new XMLHandler(booklist));
    		InputStream inputstream=context.getResources().openRawResource(Id);
    		InputSource inputsource=new InputSource(inputstream);
    		reader.parse(inputsource);
    	    for(Iterator<BookRank> iterator = booklist.iterator();iterator.hasNext();){ 	
    	    	book=(BookRank)booklist.iterator();
    	    }
    	    booklist.add(book);
    	}
    	catch(Exception e){
    	}  	
    	return  booklist;
    }
    
    
    /**
   	 * ����assets���ı�  ,return  booklist;
   	 * */
       public List<BookRank> getAssetsBookRank(String name){
       	SAXParserFactory factory = SAXParserFactory.newInstance();
       	List<BookRank> booklist=null;
       	BookRank book=null;
       	ReadSourceText readsourcetext=new ReadSourceText(context);
       	try{
       		XMLReader reader=factory.newSAXParser().getXMLReader();
       		booklist=new ArrayList<BookRank>();
       		reader.setContentHandler(new XMLHandler(booklist));
       		InputStream inputstream=readsourcetext.readAssetsToInputStream(name);
       		InputSource inputsource=new InputSource(inputstream);
       		reader.parse(inputsource);
       	    for(Iterator<BookRank> iterator = booklist.iterator();iterator.hasNext();){ 	
       	    	book=(BookRank)booklist.iterator();
       	    }
       	    booklist.add(book);
       	}
       	catch(Exception e){
       	}   	
       	return  booklist;
       }
       /**
      	 * ��������InputStream���� ---��1-->ֱ�ӽ���InputStream ,return  booklist;
      	 * */
          public List<BookRank> getNetInputBookRank(String urlstr){
          	SAXParserFactory factory = SAXParserFactory.newInstance();
          	List<BookRank> booklist=null;
          	BookRank book=null;
          	BookHttpDownload httpload=new BookHttpDownload(); 
          	try{
          		XMLReader reader=factory.newSAXParser().getXMLReader();
          		booklist=new ArrayList<BookRank>();
          		reader.setContentHandler(new XMLHandler(booklist));
          		InputStream inputstream=httpload.getInputStream(urlstr);
          		InputSource inputsource=new InputSource(inputstream);
          		reader.parse(inputsource);
          	    for(Iterator<BookRank> iterator = booklist.iterator();iterator.hasNext();){ 	
          	    	book=(BookRank)booklist.iterator();
          	    }
          	    booklist.add(book);
          	}
          	catch(Exception e){
          	}   	
          	return  booklist;
          }
          /**
        	 * ��������InputStream���󡣡���2---��ת��Ϊstring�����ٽ���string����  ,return  booklist;
        	 * */
            public List<BookRank> getNetStrBookRank(String urlstr){       	
            	BookHttpDownload httpload=new BookHttpDownload(); 
            	List<BookRank> booklist=new ArrayList<BookRank>();
            	String  string=null;
            	string=httpload.getUrlString(urlstr);
            	booklist=getStringBookRank(string);
             	return  booklist;
            }   
            /**
          	 * ���������ı� ---��1-->��ȡInputStream ,return  booklist;
          	 * */
              public List<BookRank> getLocalInputBookRank(String filepath){
              	SAXParserFactory factory = SAXParserFactory.newInstance();
              	List<BookRank> booklist=null;
              	BookRank book=null;
              	ReadLocalText readlocaltext=new ReadLocalText();
              	try{
              		XMLReader reader=factory.newSAXParser().getXMLReader();
              		booklist=new ArrayList<BookRank>();
              		reader.setContentHandler(new XMLHandler(booklist));
              		InputStream inputstream=readlocaltext.readFileToInputstream(filepath);
              		InputSource inputsource=new InputSource(inputstream);
              		reader.parse(inputsource);
              	    for(Iterator<BookRank> iterator = booklist.iterator();iterator.hasNext();){ 	
              	    	book=(BookRank)booklist.iterator();
              	    }
              	    booklist.add(book);
              	}
              	catch(Exception e){
              	}   	
              	return  booklist;
              }
              /**
            	 * ���������ı�---��2---��ת��Ϊstring�����ٽ���string����  ,return  booklist;
            	 * */
                public List<BookRank> geLocalStrBookRank(String filepath){       	
                	ReadLocalText readlocaltext=new ReadLocalText();
                	List<BookRank> booklist=new ArrayList<BookRank>();
                	String  string=null;
                	string=readlocaltext.readFileToString(filepath);
                	booklist=getStringBookRank(string);
                 	return  booklist;
                }   
         
}
