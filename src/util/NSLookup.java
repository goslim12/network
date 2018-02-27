package util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NSLookup {

	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		String domain = null;
		while(true) {
			System.out.print("> ");
			domain = scanner.nextLine();
			if("exit".equals(domain))
				return;
			try {
				InetAddress[] address = InetAddress.getAllByName(domain);
				for(InetAddress ad :address)	
					System.out.println(domain+ " : "+ad.getHostAddress());
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		try {
//			System.out.println("================");
//			System.out.println(InetAddress.getByName(domain));
//			System.out.println("================");
//			
//			InetAddress inetAddress = InetAddress.getLocalHost();
//
//			String hostname = inetAddress.getHostName();
//			String hostAddress = inetAddress.getHostAddress();
//			byte[] addresses = inetAddress.getAddress();
//			
//			System.out.println( hostname );
//			System.out.println( hostAddress );
//			for( int i = 0; i < addresses.length; i++ ) {
//				System.out.print( addresses[ i ] & 0x000000ff );
//				if( i < 3 ) {
//					System.out.print( "." );
//				}
//			}
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		}
		
	}

}
