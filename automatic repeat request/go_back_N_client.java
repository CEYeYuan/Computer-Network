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
			System.out.println("please type in the total number of packets");
			numPackets=scr.nextInt();
			System.out.println("error probablity is ");
			int probError=scr.nextInt();
			System.out.println("window size is");
			wSize=scr.nextInt();
			System.out.println("timeout is ");
			timeOut=scr.nextInt();
			int[] timer=new int[wSize];
			Thread thread=new Thread(new Listener(socket));
			thread.start();
			int i=1;
			for(i=1;i<=wSize;i++){
				int rd=rg.nextInt(100)+1;
				if(rd>probError){
					writer.writeBytes(i+ "\r\n");
				}
				timer[(sent-1)%wSize]=System.currentTimeMillis();
				System.out.println("sending data: "+i);
				writer.writeBytes(sent+ "\r\n");
				sent++;
			}
			while(lastAck<numPackets){
				if(System.currentTimeMillis()-timer[0]>timeOut){
					i=lastAck+1;
				}
				int rd=rg.nextInt(100)+1;
				if(rd>probError){
					writer.writeBytes(numPackets+ "\r\n");
				}
				timer[(sent-1)%wSize]=System.currentTimeMillis();
				System.out.println("sending data: "+i);
				writer.writeBytes(sent+ "\r\n");
				sent++;
				if(str.equalsIgnoreCase("quit"))
					break;
				i++;
			}
			socket.close();
		}catch(Exception e){e.getStackTrace();}

	}

}
