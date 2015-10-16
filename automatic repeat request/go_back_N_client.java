import java.net.*;
import java.io.*;
import java.util.*;

public class go_back_N_client {
	private static int lastAck=0;
	public static void setAckNum(int ackNum){
		lastAck=ackNum;
	}
	public static void main(String[] args) {
		System.out.println("Hello World from client");
		try{
			Random rg=new Random();
			Socket socket = new Socket("localhost",2888);
			BufferedReader socket_reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
			Scanner scr=new Scanner(System.in);
			int numPackets,wSize,timeOut;

			int sent=1;
			String  str="";
			System.out.println("now please type in the total number of packets, error probablity, window size,timeout value");
			numPackets=scr.nextInt();
			System.out.println("we'll start sending: "+numPackets+" packets");
			int probError=scr.nextInt();
			System.out.println("The probablity of an error message is "+probError+"%");
			wSize=scr.nextInt();
			System.out.println("The windows size is "+wSize);
			timeOut=scr.nextInt();
			System.out.println("Time out is  "+timeOut);
			int[] timer=new int[wSize];
			Thread thread=new Thread(new Listener(socket));
			thread.start();
			int i=0;
			for(int i=0;i<wSize;i++){
				int rd=rg.nextInt(100)+1;
				numPackets=scr.nextInt();
				if(rd>probError)
					writer.writeBytes(numPackets+ "\r\n");
				timer[(sent-1)%wSize]=System.currentTimeMillis();
				System.out.println("sending data: "+numPackets);
				writer.writeBytes(sent+ "\r\n");
				sent++;
			}
			while(){
				int rd=rg.nextInt(100)+1;
				numPackets=scr.nextInt();
				if(rd>probError)
					writer.writeBytes(numPackets+ "\r\n");
				timer[(sent-1)%wSize]=System.currentTimeMillis();
				System.out.println("sending data: "+numPackets);
				writer.writeBytes(sent+ "\r\n");
				sent++;
				
				//"\r\n" means the end of a packet
				if(str.equalsIgnoreCase("quit"))
					break;
			}
			socket.close();
		}catch(Exception e){e.getStackTrace();}

	}

}
