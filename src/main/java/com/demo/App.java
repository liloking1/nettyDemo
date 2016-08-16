package com.demo;

import com.demo.java.SocketServer;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        SocketServer socketServer = new SocketServer("127.0.0.1", 50000);
        socketServer.openServer();
    }
}
