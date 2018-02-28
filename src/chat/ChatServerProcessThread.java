package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ChatServerProcessThread extends Thread {
	private Socket socket;
	private String nickname=null;
	private List<Writer> listWriters=null;
	BufferedReader bufferedReader = null;
	PrintWriter	printWriter = null;
	public ChatServerProcessThread( Socket socket, List<Writer> listWriters) {
		this.socket = socket;
		this.listWriters = listWriters;
	}
//	public ChatServerProcessThread( Socket socket,String nickname ) {
//		this.socket = socket;
//		this.nickname = nickname;
//	}
	@Override
	public void run() {
		//4. 연결 성공
//		InetSocketAddress remoteSocketAddress = 
//				(InetSocketAddress)socket.getRemoteSocketAddress();
//		int remoteHostPort = remoteSocketAddress.getPort();
//		String remoteHostAddress = remoteSocketAddress.getAddress().getHostAddress();
//		consoleLog( "connected from " + remoteHostAddress + ":" + remoteHostPort );
		
		try {
			//5. I/O Stream 받아오기
			bufferedReader = 
				     new BufferedReader( new InputStreamReader( socket.getInputStream(), StandardCharsets.UTF_8 ) );
			printWriter = 
				     new PrintWriter( new OutputStreamWriter( socket.getOutputStream(), StandardCharsets.UTF_8 ), true );

			
			while( true ) {
				
			   String request = bufferedReader.readLine();

			   if( request == null ) {
//				   ChatServer.log( "클라이언트로 부터 연결 끊김" );
//				      break;
				    ChatServer.log( "클라이언트로 부터 연결 끊김" );
				    doQuit( printWriter );
				    break;

			   }
			   
			   String[] tokens = request.split( ":" );
			   System.out.println("message".equals( tokens[0] ));
			   if( "join".equals( tokens[0] ) ) {
			      doJoin( tokens[1], printWriter );
			   } else if( "message".equals( tokens[0] ) ) {
			      doMessage(tokens[1]);
			   } else if( "quit".equals( tokens[0] ) ) {
			      doQuit(printWriter);
			      break;
			   } else {
			      ChatServer.log( "에러:알수 없는 요청(" + tokens[0] + ")" );
			   }

			   
			   
			   
//				//6. 데이터 읽기(read)
//				byte[] buffer = new byte[256];
//				int readByteCount = is.read( buffer ); //Blocking
//				
//				if( readByteCount == -1 ) { //정상종료
//					consoleLog( "disconnected by client" );
//					break;
//				}
//				
//				String data = new String( buffer, 0, readByteCount, "utf-8" );
//				consoleLog( "received:" + data );
//			
//				//7. 데이터 쓰기
//				os.write( data.getBytes( "utf-8" ) );
			}
		} catch(SocketException e) {
			// 상대편이 정상적으로 소켁을 닫지 않고 종료 한 경우
			doQuit( printWriter );
			ChatServer.log( "sudden closed by client" );
		} catch( IOException e ) {
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

		
	private void addWriter( Writer writer ) {
	   synchronized( listWriters ) {
	      listWriters.add( writer );
	   }
	}
	

	private void doJoin( String nickName, Writer writer ) {
		   this.nickname = nickName;
			
		   String data = nickName + "님이 참여하였습니다."; 
		   broadcast( data );
		   /* writer pool에  저장 */
		   addWriter( writer );

		   // ack
		   printWriter.println( "join:ok" );
		   printWriter.flush();

	}
	private void broadcast( String data ) {
		   synchronized( listWriters ) {
		      for( Writer writer : listWriters ) {
				PrintWriter printWriter = (PrintWriter)writer;
				printWriter.println( data );
				printWriter.flush();
		      }
		   }
		}
	private void doMessage( String message ) {
		   /* 잘 구현 해보기 */	
		//일단 출력해보긔 
//		   printWriter.println(nickname+" : "+message);
//		   printWriter.flush();
		   
		   broadcast( nickname+" : "+message );


	}
	private void doQuit(  Writer writer ) {
	   removeWriter( writer );
			
	   String data = nickname + "님이 퇴장 하였습니다."; 
	   broadcast( data );
	}

	private void removeWriter( Writer writer ) {
	   /* 잘 구현 해보기 */	
	   synchronized( listWriters ) {
		      listWriters.remove( writer );
	   }
	}



}
