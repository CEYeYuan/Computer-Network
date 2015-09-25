import java.net.*;
import java.io.*;
public class myServer{
	public static void main(String[] args){
		System.out.println("Hello World");
		try{	
			ServerSocket server = new ServerSocket(9876);
			Socket socket=null;
			
			while(socket==null){
				socket=server.accept();
			}

			BufferedReader socket_reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
			
			while(true){
			String str = socket_reader.readLine();
			System.out.println(str);
			
			String str = reader.readLine();
			writer.writeBytes(str + "\r\n");
			if(str.equalsIgnoreCase("quit"))
				break;}
			socket.close();
		}catch(Exception e){e.getStackTrace();}
	}	
}