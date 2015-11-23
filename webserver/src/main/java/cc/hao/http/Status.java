package cc.hao.http;

public enum Status {

	_100("100 Continue"),
	_200("200 OK"),
	_400("400 Bad Request"),
	_404("404 Not found"),
	_500("500 Internal server error");
	
	private final String status;
	
	private Status(String status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return status;
	}
	
}
