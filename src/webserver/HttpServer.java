package webserver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 一个可以访问静态页面的web服务器
 * <p>Copyright: Copyright (c) 2015<p>
 * <p>succez<p>
 * @author YANGGUANG
 * @createdate 2015年7月14日
 */
public class HttpServer{
	public static final String WEB_ROOT=System.getProperty("user.dir")+File.separator+"webroot";
	private static final String SHUTDOWN_COMMAND="/SHUTDOWN";
	private boolean shutdown=false;
	public static void main(String[] args){
        HttpServer server=new HttpServer();
        server.await();
	}
	
	//启动服务器，并接受用户请求进行处理
	public void await(){
		ServerSocket serverSocket=null;
		int port=5050;
		try{
			serverSocket=new ServerSocket(port,1,InetAddress.getByName("192.168.13.178"));
		}catch(IOException e){
			e.printStackTrace();
			System.exit(1);
		}
	//若请求不是SHUTDOWN时，循环处理请求
		while(!shutdown){
			Socket socket=null;
			InputStream input=null;
			OutputStream output=null;
			try{
				//返回socket
				socket=serverSocket.accept();
				input=socket.getInputStream();
				output=socket.getOutputStream();
				//接收请求，用于接收socket发送过来的字节流
				Request request=new Request(input);
				request.parse();
				//处理请求并返回结果
				Response response=new Response(output);
				response.setRequest(request);
				response.sendStaticResource();
				socket.close();
				//若请求命令为SHUTDOWN时则关闭服务器
				shutdown=request.getUrl().equals(SHUTDOWN_COMMAND);
			}catch(Exception e){
				e.printStackTrace();
				continue;
			}
		}
	}
}



