package cc.hao.http;

public enum Method {

	GET("GET"),
	POST("POST"),
	PUT("PUT"),
	HEAD("HEAD"),
	UNRECONGIZED("UNRECONGIZED"),
	DELETE("DELETE");
	
	private final String method;
	private Method(String method) {
		this.method = method;
	}
	
}
