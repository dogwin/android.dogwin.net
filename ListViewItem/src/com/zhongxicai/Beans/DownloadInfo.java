package com.zhongxicai.Beans;

public class DownloadInfo {

	String filename;
	String urlstr;
	String dirpath;
	String filetype;
	
	public DownloadInfo(String urlstr,String dirpath,String filename,String filetype){
		this.filename=filename;
		this.urlstr=urlstr;
		this.dirpath=dirpath;
		this.filetype=filetype;
	}
	
	public DownloadInfo(){
		super();
	}
	public void setfiletype(String filetype){
		this.filetype=filetype;
	}
	
	public void setfilename(String filename){
		this.filename=filename;
	}
	public void seturlstr(String urlstr){
		this.urlstr=urlstr;
	}
	public void setdirpath(String dirpath){
		this.dirpath=dirpath;
	}
	public String getfilename(){
		return filename;
	}
	public String geturlstr(){
		return urlstr;
	}
	public String getdirpath(){
		return dirpath;
	}
	public String getfiletype(){
		return filetype;
	}
	
}
