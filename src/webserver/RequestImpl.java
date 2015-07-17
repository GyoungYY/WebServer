package webserver;

//import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.util.HashMap;

import org.slf4j.*;

import webserver.api.Request;

public class RequestImpl implements Request{
	private InputStream input;
	private String url;
	StringBuffer request=new StringBuffer(2048);
	final static Logger logger=LoggerFactory.getLogger(RequestImpl.class);
	public RequestImpl(InputStream input){
		this.input=input;
	}
	//按照HTTP协议对请求进行解析
	public void parse(){
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
		logger.info("request head:{}",request.toString());
		url=parseUrl(request.toString());
	}
	//解析出URL
	private String parseUrl(String requestString){
		int index1,index2;
		index1=requestString.indexOf(' ');
		if(index1!=-1){
			index2=requestString.indexOf(' ',index1+1);
			if(index2>index1)
				return requestString.substring(index1+1,index2);
		}
		return null;
	}
	public String getUrl(){
		return url;
	}
/*	public Map<String,String> getHeaders(){
		Map<String, String> map=new Map(String,String);
		InputStreamReader read=new InputStreamReader(input);
		BufferedReader reader=new BufferedReader(read);
		String line;
		try{
			while((line=reader.readLine())!=null){
//				hashmap.get(line);
			}
			reader.close();
			read.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}*/
	public String getHeaders(){
		return request.toString();
	}
	public boolean getConnection(){
		boolean flag=false;
		String req=request.toString();
		String connection;
		int index1,index2;
		index1=req.lastIndexOf("Connection");
		index2=req.indexOf(' ', index1);
		index2=index2+1;
		connection=req.substring(index2, index2+6);
		if(connection=="close"){
			flag=true;
		}
		return flag;			
	}
}
