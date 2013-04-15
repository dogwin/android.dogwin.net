package com.tarena.fashionmusic.lrc.xml;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

import com.tarena.fashionmusic.entry.BaiduMusic;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;

public class PullParse {
		
	public static List<BaiduMusic> getBdMusics(InputStream inputStream,Handler handler) throws Exception{
			ArrayList<BaiduMusic> bdMusics = null;
			BaiduMusic bdMusic = null;
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(inputStream, "UTF-8");
			int event = parser.getEventType();//������һ���¼�
			while(event!=XmlPullParser.END_DOCUMENT){
				switch(event){
				case XmlPullParser.START_DOCUMENT://�жϵ�ǰ�¼��Ƿ����ĵ���ʼ�¼�
					bdMusics = new ArrayList<BaiduMusic>();//��ʼ��BdMusics����
					break;
				case XmlPullParser.START_TAG://�жϵ�ǰ�¼��Ƿ��Ǳ�ǩԪ�ؿ�ʼ�¼�
					if("url".equals(parser.getName())){//�жϿ�ʼ��ǩԪ���Ƿ���BdMusic
						bdMusic = new BaiduMusic();
					}
					if(bdMusic!=null){
						if ("encode".equals(parser.getName())) {
							bdMusic.setDownpath(parser.nextText());
						} else if ("decode".equals(parser.getName())) {
							bdMusic.setDowname(parser.nextText());
						} else if ("lrcid".equals(parser.getName())) {
							Log.i("lrc", parser.nextText() + "xmlpaser");
							bdMusic.setLrcid(parser.nextText());
						}
					}
					break;
				case XmlPullParser.END_TAG://�жϵ�ǰ�¼��Ƿ��Ǳ�ǩԪ�ؽ����¼�
					if("url".equals(parser.getName())){//�жϽ�����ǩԪ���Ƿ���BdMusic
						bdMusics.add(bdMusic);//��BdMusic��ӵ�BdMusics����
						bdMusic = null;
					}
					Message msg = handler.obtainMessage();
					msg.what = 1;
					msg.obj = bdMusics;
					handler.sendMessage(msg);
					break;
				}
				event = parser.next();//������һ��Ԫ�ز�������Ӧ�¼�
			}//end while
			return bdMusics;
		}

	
	
}
