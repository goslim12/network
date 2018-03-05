package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class TCPClient {
	private static final String SERVER_IP = "192.168.73.1";
	private static final int SERVER_PORT = 5000;
	
	public static void main(String[] args) {
		Socket socket = null;
		
		try {
			//1. 소켓 생성
			socket = new Socket();
			
			//1-1. socket beffer size 확인 
			int receiveBufferSize = socket.getReceiveBufferSize();
			int sendBufferSize = socket.getSendBufferSize();
			System.out.println(receiveBufferSize + ":" + sendBufferSize);
			
			
			
			//1-2 socket beffer size 변경
			socket.setReceiveBufferSize(1024*10);
			socket.setSendBufferSize(1024*10);
			receiveBufferSize = socket.getReceiveBufferSize();
			sendBufferSize = socket.getSendBufferSize();
			System.out.println(receiveBufferSize + ":" + sendBufferSize);
			
			
			//1-3 SO_TIMEOUT
//			socket.setSoTimeout(1*1);
			
			//1-4 SO_NODELAY (Nagle Algorithm Off)
			socket.setTcpNoDelay(true);
			//2. 서버연결
			socket.connect( new InetSocketAddress(SERVER_IP, SERVER_PORT ) );
		
			//3. I/O Stream 받아 오기
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			
			//4. 쓰기/읽기
			String data = "hello";
			os.write( data.getBytes( "utf-8" ) );
			
			byte[] buffer = new byte[256];
			int readByteCount = is.read( buffer );
			
			if( readByteCount == -1 ) {
				System.out.println( "[client] disconnected by Server" );
				return;
			}
			
			data = new String( buffer, 0, readByteCount, "utf-8" );
			System.out.println( "[client] received:" + data );
			
		} catch(ConnectException e ) {
			System.out.println( "[client] Not Connected");
		}catch(SocketTimeoutException e ) {
			System.out.println( "[client] ReadTime Out");
		}catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if( socket != null && socket.isClosed() == false ) {
					socket.close();
				}
			} catch( IOException e ) {
				e.printStackTrace();
			}
		}
		
	}

}
