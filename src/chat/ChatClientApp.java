package chat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class ChatClientApp {
	private static final String SERVER_IP = "192.168.1.7";
	private static final int SERVER_PORT = 6000;
	
	public static void main(String[] args) {
		
	
		String name = null;
		Scanner scanner = new Scanner(System.in);
		Socket socket = new Socket();
		try {
		socket.connect( new InetSocketAddress(SERVER_IP, SERVER_PORT) );
		
		BufferedReader br =
			new BufferedReader( new InputStreamReader( socket.getInputStream(), "UTF-8" ) );
//		PrintWriter pw = 
//			new PrintWriter( new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true );
//	
		
		while( true ) {
			
			System.out.println("대화명을 입력하세요.");
			System.out.print(">>> ");
			name = scanner.nextLine();
			
			if (name.isEmpty() == false ) {
				break;
			}
			
			System.out.println("대화명은 한글자 이상 입력해야 합니다.\n");
		}
		} catch (IOException e) {
			log( "error:" + e );
		} finally {
//			try {
//				if( socket.isClosed() == false ) {
//					socket.close();
//				} 
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
			scanner.close();
		}
		new ChatWindow(name,socket).show();
	}
	
	public static void log( String log ) {
		System.out.println( "[client:" + Thread.currentThread().getId() + "] " + log );
	}	

}
