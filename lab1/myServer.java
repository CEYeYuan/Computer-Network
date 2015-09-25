import java.net.*;
import java.io.*;
public class myServer{
	public static void main(String[] args){
		System.out.println("Hello World");
		try{	
			ServerSocket server = new ServerSocket(9875);
			Socket socket=server.accept();

			//System.out.println("I'm running");
		
			BufferedReader socket_reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
			
			while(true){
			String str = socket_reader.readLine();
			System.out.println(str);
			
			System.out.println("I'm running");

			writer.writeBytes(str + "\r\n");
			if(str.equalsIgnoreCase("quit"))
				break;
			socket.close();}
		}catch(Exception e){e.getStackTrace();}
	}	
}