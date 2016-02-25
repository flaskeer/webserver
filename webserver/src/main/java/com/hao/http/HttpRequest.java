package com.hao.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class HttpRequest {

	private static Logger logger = Logger.getLogger(HttpRequest.class);
	
	List<String> header = new ArrayList<String>();
	Method method;
	String uri;
	String version;
	public HttpRequest(InputStream in) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line = reader.readLine();
		parseRequestLine(line);
		if(!line.equals("")){
			line = reader.readLine();
			addRequestHeader(line);
		}
	}
	private void addRequestHeader(String line) {
		logger.info(line);
		header.add(line);
		
	}
	private void parseRequestLine(String line) {
		logger.info(line);
		String[] strs = line.split("\\s+");
		try {
			method = Method.valueOf(strs[0]);
		} catch (Exception e) {
			method = method.UNRECONGIZED;
		}
		uri = strs[1];
		version = strs[2];
	}
	
}
