import java.io.*;
import java.net.*;
import java.util.*;
import javax.xml.ws.handler.MessageContext.Scope;

public class MainClient{
	static String mode;
	static String host;
	static int port;
	public static void main(String[] args) {

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

		try 
		{
			Socket socket=null;
			if(mode.equalsIgnoreCase("client"))
			{
				socket=new Socket(host, port);
			}
			else if(mode.equalsIgnoreCase("server"))
			{
				ServerSocket ss=new ServerSocket(port);
				socket=ss.accept();
			}
			else
			{
				System.out.println("improper type.");
				return;
			}
			System.out.println("Connected to : "+ host+ ":"+socket.getPort());

			//reader and writer:
			BufferedReader reader=new BufferedReader(new InputStreamReader(socket.getInputStream())); //for reading lines
			DataOutputStream writer=new DataOutputStream(socket.getOutputStream());	//for writing lines.
			Scanner scr = new Scanner(System.in);
			
			while(socket!=null && socket.isConnected() && !socket.isClosed()){
				System.out.println("Enter number of nodes in the network, 0 to Quit: ");
				int noNodes = scr.nextInt();
				

				// Send noNodes to the server, and read a String from it containing adjacency matrix
				
				writer.write(noNodes); 
				String respond=reader.readLine();
				//System.out.println(respond);
				
				// Create an adjacency matrix after reading from server
				double[][] matrix = new double[noNodes][noNodes];
				
				// Use StringToenizer to store the values read from the server in matrix
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
				//The nodes are stored in a list, nodeList
				List<Node> nodeList = new ArrayList<Node>();
				for(int i = 0; i < noNodes; i++){
					nodeList.add(new Node(i));
				}
				
				// Create edges from adjacency matrix
				Routing.adjacenyToEdges(matrix, nodeList);
				
				// Finding shortest path for all nodes
				
				for(Node start:nodeList){
					System.out.println("Node"+start.name);
					Routing.computePaths(start);
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


					for(Node node:nodeList){
						node.previous=null;
						node.minDistance=Double.POSITIVE_INFINITY;
					}
				}						
				
				
				socket.close();
			}
			System.out.println("Quit");


		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}