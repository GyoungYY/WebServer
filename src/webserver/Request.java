package webserver;

import java.io.IOException;
import java.io.InputStream;

public class Request {
	private InputStream input;
	private String url;
	public Request(InputStream input){
		this.input=input;
	}
	//按照HTTP协议对请求进行解析
	public void parse(){
		StringBuffer request=new StringBuffer(2048);
		int i;
		byte[] buffer=new byte[2048];
		try{
			i=input.read(buffer);
		}catch(IOException e){
			e.printStackTrace();
			i=-1;
		}
		for(int j=0;j<i;j++){
			request.append((char)buffer[j]);
		}
		System.out.print(request.toString());
		url=parseUrl(request.toString());
	}
	//解析出URL
	private String parseUrl(String requestString){
		int index1,index2;
		index1=requestString.indexOf(' ');
		if(index1!=-1){
			index2=(requestString.indexOf(' ',index1+1)<requestString.indexOf('?',index1+1))?
					requestString.indexOf(' ',index1+1):requestString.indexOf('?',index1+1);
			if(index2>index1)
				return requestString.substring(index1+1,index2);
		}
		return null;
	}
	public String getUrl(){
		return url;
	}
}
