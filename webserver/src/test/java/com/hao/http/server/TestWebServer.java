package com.hao.http.server;

import org.junit.Assert;
import org.junit.Test;

public class TestWebServer {

	@Test
	public void parseCorrectPortParam(){
		String[] args = new String[1];
		args[0] = "1234";
		Assert.assertEquals(1234, ServerStart.getParameter(args));
	}
	@Test
	public void testdefaultport(){
		String[] args = new String[0];
		Assert.assertEquals(8080, ServerStart.getParameter(args));
	}
	
	
	
}
