import java.net.*;
import java.io.*;
public class myServer{
	public static void main(String[] args){
		System.out.println("Hello World");
		try{	
			ServerSocket server = new ServerSocket(9870);
			Socket socket=server.accept();

			//System.out.println("I'm running");
		
			BufferedReader socket_reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
			
			while(true){
			String str = socket_reader.readLine();
			System.out.println(str);
			if(str.equalsIgnoreCase("quit")){
				socket.close();
				break;
			}
			

			writer.writeBytes(str + "\r\n");
			
			}
		}catch(Exception e){e.getStackTrace();}
	}	
}