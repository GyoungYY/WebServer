package webserver.api;

public interface Response {

	void setStatus(int sta);

	void addHeader(String key, String value);
	
	void addBody(byte[] body);

}
