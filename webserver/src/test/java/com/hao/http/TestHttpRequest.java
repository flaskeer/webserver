package com.hao.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class TestHttpRequest {

	@Test
	public void doHeadRequest() throws IOException{
		HttpRequest request = new HttpRequest(new ByteArrayInputStream("HEAD / HTTP/1.1\n\n".getBytes()));
		System.out.println(request.method + "\n" + request.uri + "\n" + request.version);
		Assert.assertEquals(Method.HEAD, request.method);
	}
	
	@Test
	public void doGetRequest() throws IOException{
		HttpRequest request = new HttpRequest(new ByteArrayInputStream("GET / HTTP/1.1\n\n".getBytes()));
		System.out.println(request.method + "\n" + request.uri + "\n" + request.version);
		Assert.assertEquals(Method.GET, request.method);
	}
	@Test
	public void doUnknowRequest() throws IOException{
		HttpRequest request = new HttpRequest(new ByteArrayInputStream("WHAT / HTTP/1.1\n\n".getBytes()));
		System.out.println(request.method + "\n" + request.uri + "\n" + request.version);
		Assert.assertEquals(Method.UNRECONGIZED, request.method);
	}
	
	@Test
	public void testHeader() throws IOException{
		HttpRequest request = new HttpRequest(new ByteArrayInputStream("GET / HTTP/1.1\n\n".getBytes()));
		System.out.println(request.header);
	}
	
	
}
