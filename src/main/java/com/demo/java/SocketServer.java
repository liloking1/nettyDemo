package com.demo.java;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SocketServer {

	private int port;
	private String host;
	private ServerSocketChannel serverSocketChannel;
	private ServerSocket serverSocket;
	private Selector selector;
	private ConcurrentMap<String, SocketChannel> clientMap = new ConcurrentHashMap<String,SocketChannel>();
	
	public SocketServer(String host,int port){
		this.host = host;
		this.port = port;	
		
	}
	
	
	public void openServer() {
		try {
			serverSocketChannel = serverSocketChannel.open();
			serverSocket = serverSocketChannel.socket();
			InetSocketAddress inetSocketAddress = new InetSocketAddress(this.host,this.port);
			serverSocket.bind(inetSocketAddress);
			serverSocketChannel.configureBlocking(false);
			selector = Selector.open();
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("服务器以启动");
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		
		while (true) {
			try {
			//	System.out.println(4444);
				int select = selector.select();
				System.out.println(select);
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
			
			Set<SelectionKey> readyKeys = selector.selectedKeys();
			Iterator<SelectionKey> iterator = readyKeys.iterator();
			byte[] s = "欢迎连接服务器".getBytes();
			while(iterator.hasNext()){
				//System.out.println(33333);
				SelectionKey key = iterator.next();
				iterator.remove();
				try {
					if(key.isAcceptable()){
						System.out.println("accept");
						ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
						SocketChannel sc = ssc.accept();
						clientMap.put(sc.socket().getRemoteSocketAddress().toString(), sc);
						System.out.println("客户端连接是"+sc.socket().getRemoteSocketAddress());
						sc.configureBlocking(false);
						SelectionKey key2 = sc.register(selector, SelectionKey.OP_READ);
						
					}else if(key.isReadable()){
						System.out.println("read");
						SocketChannel channel = (SocketChannel) key.channel();
						ByteBuffer buffer = ByteBuffer.allocate(4096);
						while(buffer.hasRemaining()&&channel.read(buffer)!=-1){
							buffer.flip();
							System.out.println(Charset.forName("UTF-8").decode(buffer));
							
						}
						SelectionKey key2 = channel.register(selector, SelectionKey.OP_WRITE);
						
//						ByteBuffer buffer2 = ByteBuffer.allocate(channel.getRemoteAddress().toString().getBytes().length+2);
//						buffer2.put(channel.getRemoteAddress().toString().getBytes());
//						buffer2.put((byte)'\r');
//						buffer2.put((byte) '\n');
//						buffer2.flip();
//						key2.attach(buffer2);
						
					}else if (key.isWritable()) {
//						System.out.println("write");í
						SocketChannel client = (SocketChannel) key.channel();
//						ByteBuffer buffer2 = (ByteBuffer) key.attachment();
						
//						key2.attach(buffer2);
						for(Entry<String, SocketChannel> map : clientMap.entrySet()){
							ByteBuffer buffer2 = ByteBuffer.allocate(client.getRemoteAddress().toString().getBytes().length+2);
							buffer2.put(client.getRemoteAddress().toString().getBytes());
							buffer2.put((byte)'\r');
							buffer2.put((byte) '\n');
							buffer2.flip();
							map.getValue().write(buffer2);
						}
						client.register(selector, SelectionKey.OP_READ);
						
						
//						ByteBuffer buffer2 = ByteBuffer.allocate(s.length+2);
//						buffer2.put(s);
//						buffer2.put((byte)'\r');
//						buffer2.put((byte) '\n');
//						buffer2.flip();
						//key2.attach(buffer2);
//						if(!buffer.hasRemaining()){
//							buffer.rewind();
//							int first = buffer.get();
//							buffer.rewind();
//							int position = first - ' ' +1;
//							buffer.put(0)
//						}
//						client.write(buffer2);
					}
				} catch (IOException e) {
					key.cancel();
					try {
						key.channel().close();
					} catch (IOException e1) {
					}
				}
			}
		}
	}
}
