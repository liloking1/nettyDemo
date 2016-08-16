package com.demo.netty.server;

import com.demo.netty.server.init.ServerInit;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {

	private int port = 40000;
	
	public void start(){
		EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup);
            b.channel(NioServerSocketChannel.class);
            b.childHandler(new ServerInit());
            System.out.println("netty 服务器已启动11");
			// 服务器绑定端口监听
			ChannelFuture f = b.bind(port).sync();
			// 监听服务器关闭监听
			f.channel().closeFuture().sync();
			System.out.println("netty 服务器已启动");
            // 可以简写为
            /* b.bind(portNumber).sync().channel().closeFuture().sync(); */
        } catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
	}
	
	public static void main(String[] args) {
		new NettyServer().start();
	}
}
