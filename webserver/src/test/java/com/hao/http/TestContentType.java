package com.hao.http;

import org.junit.Assert;
import org.junit.Test;

public class TestContentType {

	@Test
	public void getCorrectContentTypeByExtension(){
		Assert.assertEquals(ContentType.CSS, ContentType.valueOf("CSS"));
	}
	
}
