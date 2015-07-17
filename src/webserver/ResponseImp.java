package webserver;

import java.io.OutputStream;
import java.io.PrintStream;

import webserver.api.Response;

public class ResponseImp implements Response{

	PrintStream output;
	public ResponseImp(OutputStream out){
		output=new PrintStream(out);
	}
	public void setStatus(int sta){
		switch(sta){
			case 404:output.println("HTTP/1.1 404 File Not Found!");
			         break;
			case 200:output.println("HTTP/1.1 200 OK!");
			         break;
			case 400:output.println("HTTP/1.1 400 server cannot understand the request!");
			         break;
			case 500:output.println("HTTP/1.1 500 server error!");
			         break; 
			case 503:output.println("HTTP/1.1 503 server cannot use!");
			         break;
		}
	}
	public void addHeader(String key, String value){
		output.println(key+": "+value);
		output.println("\n");
	}
	public void addBody(byte[] body){
		try{
		output.println("<html>");
		output.println("<head>");
		output.println("<title>yangguang</title>");
		output.println("</head>");
		output.println("<body>");
		output.println(body.toString());
		output.println("</body>");
		output.println("<html>");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
