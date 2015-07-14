package webserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Response {
	private static final int BUFFER_SIZE=1024;
	Request request;
	OutputStream output;
	public Response(OutputStream output){
		this.output=output;
	}
	public void setRequest(Request request){
		this.request=request;
	}
	//发送一个静态资源给客户端
	public void sendStaticResource()throws IOException{
		byte[] bytes=new byte[BUFFER_SIZE];
		FileInputStream fis=null;
		try{
			File file=new File(HttpServer.WEB_ROOT,request.getUrl());
			//如果文件存在
			if(file.exists()){
				if(file.isFile()){
					fis=new FileInputStream(file);
					int ch=fis.read(bytes, 0, BUFFER_SIZE);
					while(ch!=-1){
						output.write(bytes, 0, ch);
						ch=fis.read(bytes,0,BUFFER_SIZE);
					}
				}
				if(file.isDirectory()){
					File[] files=file.listFiles();
					String out=null;
					for(int i=0;i<files.length;i++){
						try{
							
							output.write(out.getBytes());
						}catch(IOException e){
							e.printStackTrace();
							System.out.println(e.toString());
						}
					}
				}
			}else{                  //如果文件不存在，发送错误信息
				String errorMessage="HTTP/1.1 404 File Not Found\r\n"+
				    "Content-Type:text/html\r\n"+
					"Content-Length:24\r\n"+
					"\r\n"+
				    "<html>\r\n"+
					"<head><title>\r\n"+
				    "Error\r\n"+
					"</head></title>\r\n"+
					"<h1>404 File Not Found!</h1>\r\n"+
					"<html>\r\n";
				    output.write(errorMessage.getBytes("utf-8"));
			}
		}catch(Exception e){
			System.out.println(e.toString());
		}finally{
			if(fis!=null)
				fis.close();
		}
	}
}
