import java.net.*;
import java.io.*;
import java.util.*;
public class Listener implements Runnable{
	Socket socket;
	public Listener(Socket socket){
		this.socket=socket;
	}

	public void run(){
		try{
			BufferedReader socket_reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String ack;
			while(true){
				ack=socket_reader.readLine();
				if(ack!=null){
					System.out.println("received ack"+ ack);
					MainClient.setAckNum(Integer.parseInt(ack));
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
}