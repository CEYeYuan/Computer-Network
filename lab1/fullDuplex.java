import java.net.*;
import java.io.*;
public class fullDuplex {
	public static void main(String[] args){
		try{
		
			ServerSocket server = new ServerSocket(3300);
			Socket serversocket=server.accept();
			BufferedReader socket_reader = new BufferedReader(new InputStreamReader(serversocket.getInputStream()));
			Thread listen=new Thread (new listen(socket_reader,serversocket));		
			

			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			DataOutputStream writer = new DataOutputStream(serversocket.getOutputStream());
			Thread send=new Thread (new send(reader,serversocket,writer));
			listen.run();	
			send.run();
			
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}
}