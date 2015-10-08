import java.net.*;
import java.io.*;
public class fullDuplexClient {
	public static void main(String[] args){
		try{
		
			Socket socket = new Socket("localhost",3300);
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
			Thread send=new Thread (new send(reader,socket,writer));
			

			BufferedReader socket_reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			Thread listen=new Thread (new listen(socket_reader,socket));		
				
			send.run();
			listen.run();
			
		
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}
}