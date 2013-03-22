package com.zhongxicai.Beans;

public class BookContent {
	int chapterid;
	String chaptername;
	String chapterurl;
	public BookContent(){
		super();
	}
    public BookContent(int chapterid,String chaptername,String chapterurl){
    	this.chaptername=chaptername;
    	this.chapterid=chapterid;
    	this.chapterurl=chapterurl;
    }
    public void setChapterId(int chapterid){
    	this.chapterid=chapterid;
    }
    public void setChapterName(String chaptername){
    	this.chaptername=chaptername;
    }
    public void setChapterUrl(String chapterurl){
    	this.chapterurl=chapterurl;
    }
    public int getChapterId(){
    	return chapterid;
    }
    public String getChapterName(){
    	return chaptername;
    }

    public String getChapterUrl(){
    	return chapterurl;
    }
    
}

