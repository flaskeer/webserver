package com.hao.http.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.hao.http.HttpRequest;
import org.apache.log4j.Logger;

import com.hao.http.HttpResponse;

public class RequestHandler implements Runnable{

	private static Logger logger = Logger.getLogger(RequestHandler.class);
	
	private Socket socket;
	
	
	
	public RequestHandler(Socket socket) {
		this.socket = socket;
	}



	public void run() {
		try {
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			HttpRequest request = new HttpRequest(in);
			HttpResponse response = new HttpResponse(request);
			response.write(out);
			socket.close();
		} catch (Exception e) {
			logger.error("runtime error:",e);
		}
	}

}
