import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class Listener implements Runnable {

	public Socket socket=null;
	
	public Listener(Socket socket) {
		this.socket=socket;
	}
	
	@Override
	public void run() {
		try {
			BufferedReader reader= new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while(true)
			{
				//read the acks from server and update the last ack number
				int ack=reader.read();
				CCClient.update(ack);
				//System.out.println("updating ack ="+ack);
				if(ack==-1)
					break;
			}
			socket.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
}
