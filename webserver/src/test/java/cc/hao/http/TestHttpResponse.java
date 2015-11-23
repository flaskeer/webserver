package cc.hao.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class TestHttpResponse {

	@Test
	public void doHeadRequest() throws IOException{
		HttpRequest request = new HttpRequest(new ByteArrayInputStream("HEAD / HTTP/1.1\n\n".getBytes()));
		HttpResponse response = new HttpResponse(request);
		System.out.println(response.header);
		Assert.assertTrue(response.header.size() > 0);
		Assert.assertEquals(HttpResponse.VERSION + " " + Status._200, response.header.get(0));
	}
	
	@Test
	public void doGetRequest() throws IOException{
		HttpRequest request = new HttpRequest(new ByteArrayInputStream("GET / HTTP/1.1\n\n".getBytes()));
		HttpResponse response = new HttpResponse(request);
		System.out.println(response.header);
		Assert.assertTrue(response.header.size() > 0);
		Assert.assertEquals(HttpResponse.VERSION + " " + Status._200, response.header.get(0));
		
	}
	@Test
	public void doGUnknowRequest() throws IOException{
		HttpRequest request = new HttpRequest(new ByteArrayInputStream("WHAT / HTTP/1.1\n\n".getBytes()));
		HttpResponse response = new HttpResponse(request);
		System.out.println(response.header);
		Assert.assertTrue(response.header.size() > 0);
		Assert.assertEquals(HttpResponse.VERSION + " " + Status._400, response.header.get(0));
		
	}
}
