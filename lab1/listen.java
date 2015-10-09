import java.net.*;
import java.io.*;
public class listen implements Runnable{
	BufferedReader socket_reader;
	Socket socket; 

	public listen(BufferedReader socket_reader,Socket socket){
			this.socket_reader=socket_reader;
			this.socket=socket;
	}
	public void run()  {
			System.out.println("Hello World");
		try{

			String str = socket_reader.readLine();
			System.out.println("Received: "+str);
			
			//"\r\n" means the end of a packet
			if(str.equalsIgnoreCase("quit")){
				socket.close();
			}
		}
		catch(Exception e){e.getStackTrace();}
	}
}

