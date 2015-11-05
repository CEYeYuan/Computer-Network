import java.awt.Color;
import java.awt.image.SampleModel;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;



public class CCClient {

	static String host;
	static int port;
	final static String CRLF="\r\n";
	public static int wstart;
	static long totalTime;
	static int timeOut;
	public static int lastAck = 0;
	static int sent = 1;
	static long[] send_timer;
	
	static long startTime;
	static long endTime;
	public static int EstimatedRTT;
	public static int DevRTT;
	public static int SampleRTT;
	public static final double alpha=0.125;
	public static final double beta=0.25;
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		host="localhost";
		port=9876;
		try 
		{
			Socket socket=new Socket("localhost",2888); 
			socket.setTcpNoDelay(true); 
			System.out.println("Connected to : "+ host+ ":"+socket.getPort());
			
			//reader and writer:
			BufferedReader reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			DataOutputStream writer=new DataOutputStream(socket.getOutputStream());
			Scanner scr = new Scanner(System.in);

			int cwnd=1;

			//define the thread and start it
			Thread thread=new Thread(new Listener(socket));		
			thread.start();	 

			System.out.println("Enter number of packets to be sent to the server [0-127], 0 to Quit: ");
			
			//the noPackets to the server
			int NoPackets=scr.nextInt();
			writer.write(NoPackets);
			EstimatedRTT=1200;
			DevRTT=0;
			timeOut = EstimatedRTT+4*DevRTT; //in milliseconds
			lastAck=0;
			sent=1;
			cwnd=3;
			send_timer=new long[NoPackets];
			int ssthresh=Integer.MAX_VALUE;
			int RTT_count=0;

			startTime=System.currentTimeMillis();
			try {
				while(sent<=NoPackets)
				{	//sent 1 to NoPackets,index is sent-1,ack is the last packet received
					//THE MAIN PART OF THE CODE!
					//send the packets with congestion control using the given instructions
					if(sent<=lastAck+cwnd&&send_timer[sent-1]==0){
							writer.write(sent);
							send_timer[sent-1]=System.currentTimeMillis();
							System.out.println("sending data: "+sent);
							sent++;	
					}
					else if(System.currentTimeMillis()-send_timer[lastAck]>timeOut){
						//say lastack=1, arr[1] means the sending time for 2, if timeout happens, 
						//we should check 2 first, since 2 is the first byte sent but receive no ack
						sent=lastAck+1;
						writer.write(sent);
						send_timer[sent-1]=System.currentTimeMillis();
						System.out.println("resending data: "+sent);
						//sent++;
					}
					
					else{
						Thread.sleep(10);
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				endTime = System.currentTimeMillis();
				totalTime = endTime - startTime;
			}
			
			
			//print the total taken time, number of sucessfully sent packets, etc. 
			//...
			
			writer.flush();
			socket.close();
			System.out.println("Quitting...");
			socket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void update(int ackNum)
	{
		//update lastAck here. note that last ack is accumulative, 
		//i.e., if ack for packet 10 is previously received and now ack for packet 7 is received, lastAck will remain 10
		if(ackNum>lastAck){
			System.out.println("update");
			lastAck=ackNum;
		}
	}

}
