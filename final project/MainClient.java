import java.io.*;
import java.net.*;
import java.util.*;
import javax.xml.ws.handler.MessageContext.Scope;

public class MainClient{
	static String mode;
	static String host;
	static int port;
	static int noNodes;

	public static void main(String[] args) {
		
		init(args);
		//initialize the client with the proper port number
		Socket socket=null;
		BufferedReader reader=null;
		DataOutputStream writer=null;
		Scanner scr=null;
		
		try 
		{
			socket=new Socket(host, port);
			System.out.println("Connected to : "+ host+ ":"+socket.getPort());
			reader=new BufferedReader(new InputStreamReader(socket.getInputStream())); //for reading lines
			writer=new DataOutputStream(socket.getOutputStream());	//for writing lines.
			scr = new Scanner(System.in);		
			//initialize all these variables

			while(socket!=null && socket.isConnected() && !socket.isClosed()){
				//System.out.println("Enter number of nodes in the network, 0 to Quit: ");
				//noNodes = scr.nextInt();
				// Send noNodes to the server, and read a String from it containing adjacency matrix
				String CRLF="\r\n";
				//writer.writeBytes(noNodes+CRLF); 
				noNodes=Integer.parseInt(reader.readLine());
				String respond=reader.readLine();
				System.out.println("Adjacency Matrix:\n"+respond);
			
				double[][] matrix= new double[noNodes][noNodes];
				List<Node> nodeList = new ArrayList<Node>();
				
				construct_graph(matrix,respond,nodeList);
				Routing.adjacenyToEdges(matrix, nodeList);
				// construct the graph after reading the String from server
				
				// Finding shortest path starting from node 0
				Node start=nodeList.get(0);
				Routing.computePaths(start);
				print_shortestpath(nodeList);
				//print the shortest path tree rooted at node 0
			
				
			}
			socket.close();
			System.out.println("Quit");


		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	private static void print_shortestpath(List<Node> nodeList){
		for(Node end:nodeList){
			List<Integer> path=Routing.getShortestPathTo(end);
			StringBuffer buffer=new StringBuffer();
			buffer.append("[");
			boolean isInfinity=false;
			for(int node:path){
				buffer.append(node+" ");
			}
			buffer.append("]");
			System.out.print("Total time to reach node"+end.name+": "+end.minDistance+"ms, Path : "+buffer.toString()+"\n");
		}		
	}
	private static void init(String[] args){
		if(args.length<=0)
		{
			mode="client";
			host="localhost";
			port=3333;
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