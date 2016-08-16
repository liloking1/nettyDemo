package com.demo.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClient {

	private String host;
	private int port;
	public int name;
	
	public static void main(String[] args) {
	//	for(int i=1;i<3;i++){
			SocketClient sc = new SocketClient("127.0.0.1",50000);
	//		sc.name = i;
			sc.start();
	//	}
		
	}
	
	public SocketClient(String host,int port) {
		this.host = host;
		this.port = port;
	}
	
	
	
	public void start(){
		Socket socket = null;
		try {
			socket = new Socket(this.host, this.port);
			//socket.setSoTimeout(50000);
			OutputStream out = socket.getOutputStream();
			Writer writer = new OutputStreamWriter(out, "utf-8");
			writer.write("wo shi client \r\n");
			writer.flush();
			
			
			
		//	for(String line = reader.readLine();line != null;line = reader.readLine()){
				
			//}
		//	while(true){
				InputStream in = socket.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
				String line = reader.readLine();
				System.out.println(name+"    "+line);
			//}
//			writer.close();
//			reader.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
//			try {
//				socket.close();
//			} catch (IOException e) {
//			}
		}
		
		
		
	}
}
