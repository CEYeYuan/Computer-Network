import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class Listener implements Runnable {

	public Socket socket;
	
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
				String ack=socket_reader.readLine();
				System.out.println("received "+ack);
				CCClient.update(Integer.parseInt(ack));
			
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
}
