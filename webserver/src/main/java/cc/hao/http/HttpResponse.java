package cc.hao.http;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cc.hao.util.FileUtil;

public class HttpResponse {

	private static Logger logger = Logger.getLogger(HttpResponse.class);
	
	public static final String VERSION = "HTTP/1.1";
	
	List<String> header = new ArrayList<String>();
	
	byte[] body;
	
	public HttpResponse(HttpRequest req) {
		switch (req.method) {
		case HEAD:
			fillHeaders(Status._200);
			break;
		case GET:
		try {
			fillHeaders(Status._200);
			File file = new File("d:\\markdown笔记\\collection.md");
			System.out.println(file.getAbsolutePath());
//			FileUtil.listAllFiles(file);
			if(file.isDirectory()){
				header.add(ContentType.HTML.toString());
				StringBuilder sb = new StringBuilder("<html><head><title>Index of");
				sb.append(req.uri);
				sb.append("</title></head><body><h1>Index of");
				sb.append(req.uri);
				sb.append("</h1><hr><pre>");
				File[] files = file.listFiles();
				for (File subflie : files) {
					sb.append(" <a href=\"" + subflie.getPath() + "\">" + subflie.getPath() + "</a>\n");
				}
				sb.append("<hr></pre></body></html>");
				fillResponse(sb.toString());
			}else if(file.exists()){
				setContentType(req.uri,header);
				fillResponse(getBytes(file));
			}else{
				logger.info("File not found:" + req.uri);
				fillHeaders(Status._404);
				fillResponse(Status._404.toString());
			}
		} catch (Exception e) {
			logger.error("Response Error" + e);
			fillHeaders(Status._400);
			fillResponse(Status._400.toString());
		}
			break;
		case UNRECONGIZED:
			fillHeaders(Status._400);
			fillResponse(Status._400.toString());
			break;
			
		default:
			fillHeaders(Status._500);
			fillResponse(Status._500.toString());
			break;
		}
	}

	private byte[] getBytes(File file) throws IOException{
		FileInputStream in = new FileInputStream(file);
		int length = (int)file.length();
		byte[] buffer = new byte[length];
		int offset = 0;
		while(offset < length){
			int count = in.read(buffer, offset, length);
			offset += count;
		}
		in.close();
		return buffer;
	}

	private void setContentType(String uri, List<String> list) {
		try {
			String str = uri.substring(uri.indexOf('.') + 1);
			System.out.println("str is :" + str);
			list.add(ContentType.valueOf(str.toUpperCase()).toString());
		} catch (Exception e) {
			logger.error("ContentType not found:" + e,e);
		}
	}

	private void fillHeaders(Status status) {
		header.add(HttpResponse.VERSION + " " + status.toString());
		header.add("Connection: close");
		header.add("Server:SimpleWebServer");
		
	}
	
	private void fillResponse(byte[] response) {
		body = response;
	}
	
	private void fillResponse(String response) {
		System.out.println("response is:" + response);
		body = response.getBytes();

	}
	
	public void write(OutputStream out) throws IOException{
		DataOutputStream outputStream = new DataOutputStream(out);
		for(String head: header){
			outputStream.writeBytes(head + "\r\n");
		}
		outputStream.writeBytes("\r\n");
		if(body != null){
			outputStream.write(body);
		}
		outputStream.writeBytes("\r\n");
		outputStream.flush();
	}
	
}
