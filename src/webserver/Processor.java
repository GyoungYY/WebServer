package webserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Processor extends Thread{
	private static final int BUFFER_SIZE=1024;
	public Socket socket;
    InputStream input=null;
    OutputStream output=null;
    boolean flag;
	public Processor(Socket Soc){
		socket=Soc;
	}
	public void start(){
		try{
		    input=socket.getInputStream();
		    output=socket.getOutputStream();
		//接收请求，用于接收socket发送过来的字节流
		    RequestImpl request=new RequestImpl(input);
		    request.parse();
		    flag=request.getConnection();
		    
		//处理请求并返回结果
		    ResponseImp response=new ResponseImp(output);
		    File file=new File(HttpServer.WEB_ROOT,request.getUrl());
		    byte[] bytes=new byte[BUFFER_SIZE];
		    if(file.exists()){
		    	if(file.isFile()){
		    		try{
		    			FileInputStream fis=new FileInputStream(file);
		    			byte[] buffer=new byte[(int)file.length()];
		    			fis.read(buffer);						
		    			response.setStatus(200);
		    			response.addHeader("Content-Type", "text/html");		    			
		    			output.write(buffer);
		    			output.flush();
		    		    if(flag){
		    		    	output.close();
		                }
		    			fis.close();
		    		}catch(Exception e){
		    				System.out.println(e.toString());
		    		}
				}
		    	if(file.isDirectory()){
						String[] dir=file.list();
		    			response.setStatus(200);
		    			response.addHeader("Content-Type","text/html");
		    			for(int i=0;i<dir.length;i++){
							File file2=new File(dir[i]);
							String localPath=file2.getPath();
							bytes=("<a href="+localPath+">").getBytes();
							bytes=("<h1>file:"+dir[i]+"</h1>").getBytes();
						}
		    			response.addBody(bytes);
		    		}
		    }
		    else{
		    	response.setStatus(404);
		    	response.addHeader("Content-Type:","text/html");
		    	bytes=("<h1>404 File Not Found!<h1>").getBytes();
		    	response.addBody(bytes);
		    }
		  
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}