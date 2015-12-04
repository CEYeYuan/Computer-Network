import java.io.*;
import java.net.*;
import java.util.*;
import javax.xml.ws.handler.MessageContext.Scope;
import java.nio.*;

public class MainClient{
	static String mode;
	static String host;
	static int port;
	static int noNodes;
	static int lastAck;


	public static void main(String[] args) {
		
		init(args);
		try 
		{
			Socket socket=new Socket(host, port);
			System.out.println("Connected to : "+ host+ ":"+socket.getPort());
			BufferedReader socket_reader=new BufferedReader(new InputStreamReader(socket.getInputStream())); //for reading lines
			DataOutputStream writer=new DataOutputStream(socket.getOutputStream());	//for writing lines.
			Scanner scr = new Scanner(System.in);	
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));	
			//initialize all these variables

			while(socket!=null && socket.isConnected() && !socket.isClosed()){
				
				/***************************************************************/
				/*****construct the grahp and calculate shortest path **********/	
				String CRLF="\r\n";
				noNodes=Integer.parseInt(socket_reader.readLine());
				String respond=socket_reader.readLine();
				System.out.println("Adjacency Matrix:\n"+respond);
			
				double[][] matrix= new double[noNodes][noNodes];
				List<Node> nodeList = new ArrayList<Node>();
				
				construct_graph(matrix,respond,nodeList);
				Routing.adjacenyToEdges(matrix, nodeList);
				// construct the graph after reading the String from server
				
				// Finding shortest path starting from node 0
				Node start=nodeList.get(0);
				Routing.computePaths(start);
				List<Integer> path=print_shortestpath(nodeList);
				double timeoutInteval=2*nodeList.get(nodeList.size()-1).minDistance+200;
				//print the shortest path tree rooted at node 0

				/***************************************************************/
				/**************************file transfer************************/
				writer.writeBytes(path+ "\r\n");
				System.out.println("Enter the name of the file:");
				String file_name = reader.readLine();
				writer.writeBytes(file_name + "\r\n");
				File file=new File(file_name);
			    if (file.exists()){
			    	System.out.println("File length: "+file.length());
			    	long noPackets=file.length()%1000==0?file.length()/1000:file.length()/1000+1;
			    	writer.writeBytes(noPackets + "\r\n");
			    	FileInputStream fin= new FileInputStream(file);
					DataOutputStream socket_dos = new DataOutputStream(socket.getOutputStream());
					Thread listener=new Thread(new Listener(socket));
					listener.start();
					send(file,fin,socket_dos,timeoutInteval);
					System.out.println("Quiting");
					socket_dos.close();
					socket.close();
					break;
				}else{	
					System.out.println("File does not exist");
					socket.close();
					break;
				}
		}		
	} catch (Exception e) {
			e.printStackTrace();
	}
}

	public static void setAckNum(int ack){
		if(ack>lastAck)
			lastAck=ack;
	}
	private static void send(File file,FileInputStream fin,DataOutputStream socket_dos,double timeOut) throws Exception{
		
		long length=file.length();
		int  NoPackets=(int)(length%1000==0?length/1000:length/1000+1);
		byte[][] buffer = new byte[NoPackets+1][1004];
		long[] send_timer=new long[NoPackets+1];
		lastAck=0;
		int sent=1;
		int index=0;
		int cwnd=1;
		int ssthresh=Integer.MAX_VALUE;
		long startTime=System.currentTimeMillis();
		while(lastAck<NoPackets){
			//THE MAIN PART OF THE CODE!
			//send the packets with congestion control using the given instructions
			if (index < cwnd && sent <= NoPackets){
				//if window size isn't used up, go ahead and send a packets
				System.out.println("Sending packet " + sent);
				fin.read(buffer[sent],4,1000);
				ByteBuffer buf = ByteBuffer.allocate(4);
				buf.putInt(sent);				
				for(int j=0;j<4;j++){
					buffer[sent][j] = buf.get(j);
				}
				socket_dos.write(buffer[sent], 0, 1000); //writing a portion of buffer
				send_timer[sent] = System.currentTimeMillis();
				sent++;
				index++;		
			}
			if (sent==lastAck+1){
				//after use up all the window, wait for all the ack comes back
				if (cwnd < ssthresh)//slow start 
					cwnd *= 2;
				else//congestion avoidance
					cwnd += 1;	
				index = 0;
					System.out.println("window size = "+cwnd);
				}
			else{
				//use up window size, but not all ack come back
				if (System.currentTimeMillis()-send_timer[lastAck+1]>timeOut){
					System.out.println("Timed out");
					ssthresh = cwnd/2;
					cwnd = 1;
					index = 0;
					sent = lastAck + 1;
					//go back n
				}
				else{
					Thread.sleep(10);
				}
			}
		}
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("total time ="+totalTime);
		System.out.println("# of RTT ="+totalTime/timeOut);	
	}
	private static List<Integer> print_shortestpath(List<Node> nodeList){
		List<Integer> path=null;
		for(Node end:nodeList){
			path=Routing.getShortestPathTo(end);
			StringBuffer buffer=new StringBuffer();
			buffer.append("[");
			boolean isInfinity=false;
			for(int node:path){
				buffer.append(node+" ");
			}
			buffer.append("]");
			System.out.print("Total time to reach node"+end.name+": "+end.minDistance+"ms, Path : "+buffer.toString()+"\n");
		}		
		return path;
	}
	private static void init(String[] args){
		if(args.length<=0)
		{
			mode="client";
			host="localhost";
			port=9999;
		}
		else if(args.length==1)
		{
			mode=args[0];
			host="localhost";
			port=9999;
		}
		else if(args.length==3)
		{
			mode=args[0];
			host=args[1];
			port=Integer.parseInt(args[2]);
		}
		else
		{
			System.out.println("improper number of arguments.");
			return;
		}
	}

	private static void construct_graph(double matrix[][],String respond,List<Node> nodeList){
		String delims=" ";
		StringTokenizer st=new StringTokenizer(respond,delims);
		for(int i=0;i<noNodes;i++){
			for(int j=0;j<noNodes;j++){
				if(st.hasMoreElements()){
					String tmp=(String)st.nextElement();
					if(tmp.equals("Infinity"))
						matrix[i][j]=Double.POSITIVE_INFINITY;
					else
						matrix[i][j]=Double.parseDouble(tmp);		
				}
			}
		}
	    for(int i = 0; i < noNodes; i++){
			nodeList.add(new Node(i));
		}		
	}
}