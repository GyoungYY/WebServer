package webserver.api;

//import java.util.Map;

public interface Request {

	String getUrl();

//	Map<String, String> getHeaders();
	String getHeaders();
//	byte[] getContent();   可以不实现，当POST请求是用来获取内容
}
