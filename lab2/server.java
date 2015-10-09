import java.net.*;
import java.io.*;
public class chatServer{
	public static void main(String[] args){
		System.out.println("Hello World, I' server");
		try{	
			ServerSocket server = new ServerSocket(9870);
			Socket socket=server.accept();

			BufferedReader socket_reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
			
			while(true){
			String str = socket_reader.readLine();
			System.out.println(str);
			if(str.equalsIgnoreCase("quit")){
				socket.close();
				break;
			}
			
			
			str = reader.readLine();
			writer.writeBytes(str + "\r\n");
			
		}}catch(Exception e){e.getStackTrace();}
	}	
}