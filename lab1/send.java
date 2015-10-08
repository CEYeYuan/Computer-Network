import java.net.*;
import java.io.*;
public class send implements Runnable{
	BufferedReader reader;
	Socket socket; 
	DataOutputStream writer;

	public send(BufferedReader reader,Socket socket, DataOutputStream writer){
			this.reader=reader;
			this.socket=socket;
			this.writer=writer;
	}
	public void run()  {
			System.out.println("Hello World");
		try{
			while(true){
			String str = reader.readLine();
			System.out.println("sent: "+str);
			writer.writeBytes(str + "\r\n");
			
			//"\r\n" means the end of a packet
			if(str.equalsIgnoreCase("quit"))
				break;
		}
			socket.close();
		}
		catch(Exception e){e.getStackTrace();}
	}
}


