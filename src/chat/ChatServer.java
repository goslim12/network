package chat;

import java.io.IOException;
import java.io.Writer;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import echo.EchoServerReceiveThread;

public class ChatServer {
	private static final int SERVER_PORT = 6000;
	
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		List<Writer> listWriters = new ArrayList<Writer>();
		try {
			//1. 서버소켓 생성
			serverSocket = new ServerSocket();
			
			//2. 바인딩(Binding)
			String hostAddress = InetAddress.getLocalHost().getHostAddress();
			serverSocket.bind( new InetSocketAddress(hostAddress, SERVER_PORT) );
			log( "binding " +  hostAddress + ":" + SERVER_PORT );
			
			while( true ) {
				//3. 연결 요청 기다림(accept)
				Socket socket = serverSocket.accept(); // blocking
				new ChatServerProcessThread( socket,listWriters).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if( serverSocket != null && serverSocket.isClosed() == false ) {
					serverSocket.close();
				}
			} catch( IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void log( String log ) {
		System.out.println( "[server:" + Thread.currentThread().getId() + "] " + log );
	}	

}
