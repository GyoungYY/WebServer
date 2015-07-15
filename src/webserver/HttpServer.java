package webserver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 一个可以访问静态页面的web服务器
 * <p>Copyright: Copyright (c) 2015<p>
 * <p>succez<p>
 * @author YANGGUANG
 * @createdate 2015年7月14日
 */
public class HttpServer{
    public static final String WEB_ROOT=System.getProperty("user.dir")+File.separator+"webroot";
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
		    serverSocket=new ServerSocket(port,1,InetAddress.getByName("192.168.8.28"));
	    }catch(IOException e){
		    e.printStackTrace();
		    System.exit(1);
	    }
	    while(!shutdown){
		    Socket socket=null;
		    try{
			    //返回socket
			    socket=serverSocket.accept();
			    ExecutorService pool=Executors.newFixedThreadPool(30);
			    ConnectionThread t=new ConnectionThread(socket);
                t.start();
                pool.execute(t);
		    }catch(Exception e){
			    e.printStackTrace();
			    continue;
		    }
	    }
    }
}
class ConnectionThread extends Thread{
	public Socket socket;
    InputStream input=null;
    OutputStream output=null;
	public ConnectionThread(Socket Soc){
		socket=Soc;
	}
	public void start(){
		try{
		    input=socket.getInputStream();
		    output=socket.getOutputStream();
		//接收请求，用于接收socket发送过来的字节流
		    RequestImpl request=new RequestImpl(input);
		    request.parse();
		//处理请求并返回结果
		    ResponseImp responseImp=new ResponseImp(output);
		    responseImp.setRequest(request);
		    responseImp.sendStaticResource();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}


