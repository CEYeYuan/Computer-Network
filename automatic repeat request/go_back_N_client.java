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
			long[] timer=new long[wSize];
			Thread thread=new Thread(new Listener(socket));
			thread.start();

			for(sent=1;sent<=wSize;sent++){
				int rd=rg.nextInt(100)+1;
				if(rd>probError){
					writer.writeBytes(sent+ "\r\n");
					timer[(sent-1)%wSize]=System.currentTimeMillis();
					System.out.println("sending data: "+sent+" success");
				}
				else{
					timer[(sent-1)%wSize]=System.currentTimeMillis();
					System.out.println("sending data: "+sent+" failed");
				}
				
				
				
			}
			while(lastAck<numPackets){

				if(sent<=lastAck+wSize&&sent<=numPackets){
					int rd=rg.nextInt(100)+1;
					if(rd>probError){
					writer.writeBytes(sent+ "\r\n");
					timer[(sent-1)%wSize]=System.currentTimeMillis();
					System.out.println("sending data: "+sent+" success");
					}
					else{
						timer[(sent-1)%wSize]=System.currentTimeMillis();
						System.out.println("sending data: "+sent+" failed");
					}
					timer[(sent-1)%wSize]=System.currentTimeMillis();
					sent++;
				}
				else if(System.currentTimeMillis()-timer[(sent-1)%wSize]>timeOut){
					sent=lastAck+1;
					int rd=rg.nextInt(100)+1;
					if(rd>probError){
					writer.writeBytes(sent+ "\r\n");
					timer[(sent-1)%wSize]=System.currentTimeMillis();
					System.out.println("sending data: "+sent+" success");
					}
					else{
						timer[(sent-1)%wSize]=System.currentTimeMillis();
						System.out.println("sending data: "+sent+" failed");
					}
					sent++;
				}
				else{
					Thread.sleep(10);
				}
			}
			
		}catch(Exception e){e.getStackTrace();}

	}

}
