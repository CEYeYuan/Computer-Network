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
			int i=1;
			while(true){
		
				str = socket_reader.readLine();
				System.out.println("receiving packet:"+str);
				if(Integer.parseInt(str)==i){
					writer.writeBytes(str + "\r\n");
					i++;	
				}
				else{
					writer.writeBytes(i-1 + "\r\n");
				}
				
			}
			}catch(Exception e){e.getStackTrace();}
	}	
}