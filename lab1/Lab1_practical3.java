import java.net.*;
import java.io.*;
public class Lab1_practical3 {

	public static void main(String[] args) throws UnknownHostException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Hello World");
		try{
			
			Socket socket = new Socket("localhost",9870);
			//127.0.0.1  == local host
			BufferedReader socket_reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
			long time=0;
			while(true){
			
			String str = reader.readLine();
			time=System.currentTimeMillis();
			System.out.println(time+"Time sent");
			writer.writeBytes(str + "\r\n");

			str = socket_reader.readLine();
			System.out.println(System.currentTimeMillis()+"Time Received");
			System.out.println(System.currentTimeMillis()-time+"Round trip time");
			System.out.println(str);
			
			//"\r\n" means the end of a packet
			if(str.equalsIgnoreCase("quit"))
				break;}
			socket.close();
		}catch(Exception e){e.getStackTrace();}
	}
}


