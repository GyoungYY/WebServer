package webserver.api;

public interface Request {

	String getUrl();

	String getHeaders();

//	byte[] getContent();   可以不实现，当POST请求是用来获取内容
}
