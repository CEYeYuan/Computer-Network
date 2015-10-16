import java.net.*;
import java.io.*;
public class go_back_N_server{
	public static void main(String[] args){
	System.out.println("Hello World from server");
		try{	
			ServerSocket server = new ServerSocket(2888);
			Socket socket=server.accept();

			BufferedReader socket_reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
			int lastAck=0;
			boolean data=true;
			String str,sequence;
			str="";
			sequence="";
			while(true){
		
				str = socket_reader.readLine();
				System.out.println("receiving packet:"+str);

				sequence=socket_reader.readLine();
				System.out.println("client have sent #"+sequence+" byte");
				if(lastAck+1==Integer.parseInt(sequence)){
					writer.writeBytes(sequence + "\r\n");
					lastAck++;
				}	

			if(str.equalsIgnoreCase("quit")){
				socket.close();
				break;
			}
				
		}}catch(Exception e){e.getStackTrace();}
	}	
}