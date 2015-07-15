package webserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

//import webserver.api.Response;

public class ResponseImp {
	private static final int BUFFER_SIZE=1024;
	RequestImpl request;
	OutputStream output;
	PrintStream out;
	int status;
	public ResponseImp(OutputStream output){
		this.output=output;
		out=new PrintStream(output);
	}
	public void setRequest(RequestImpl request){
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
					String[] dir=file.list();
					file.mkdirs();
					out.println("<html>");
					out.println("<head>");
					out.println("<title>Files</title>");
					out.println("</head>");
					out.println("<body>");
					out.println("<p>");
					out.println("<h1>Files:<h1>");
					for(int i=0;i<dir.length;i++){
						File file2=new File(dir[i]);
						String localPath=file2.getPath();
						out.println("<a href="+localPath+">");
						out.println("<h1>file:"+dir[i]+"</h1>");
					}
					out.println("</body>");
					out.println("<html>");
				}
			}
		    else{                  //如果文件不存在，发送错误信息
				out.println("<html>");
				out.println("<head>");
				out.println("<title>File Not Found</title>");
				out.println("</head>");
				out.println("<body>");
				out.println("<h1>404 File Not Found!<h1>");
				out.println("</body>");
				out.println("<html>");
			}
		}catch(Exception e){
			System.out.println(e.toString());
		}finally{
			if(fis!=null)
				fis.close();
		}
	}
	/*void setStatus(int sta){
		status=sta;
	}
	void addHeader(String key, String value){
		
	}
	
	void setContent(byte[] content){
		
	}

	void write(){
		
	}*/
}
