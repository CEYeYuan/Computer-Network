import java.net.*;
import java.io.*;
import java.util.*;

public class stop_and_wait_client {

	public static void main(String[] args) {
		System.out.println("Hello World from client");
		try{
			
			Socket socket = new Socket("localhost",2888);
			BufferedReader socket_reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
			Scanner scr=new Scanner(System.in);
			int numPackets;
			int sent=1;
			boolean data=true;
			String  str="";
			while(true){
			if(data){
				numPackets=scr.nextInt();
				writer.writeBytes(numPackets+ "\r\n");
				System.out.println("sending data: "+numPackets);
				data=!data;
			}else{
				writer.writeBytes(sent+ "\r\n");
				System.out.println("so far we have sent "+sent+" packets");
			    str = socket_reader.readLine();
				if(Integer.parseInt(str)==sent){
					sent++;
				}
				data=!data;
			}

			//"\r\n" means the end of a packet
			if(str.equalsIgnoreCase("quit"))
				break;
			}
			socket.close();
		}catch(Exception e){e.getStackTrace();}

	}

}
