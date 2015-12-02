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
				print_shortestpath(nodeList);
				double timeoutInteval=2*nodeList.get(nodeList.size()-1).minDistance+200;
				//print the shortest path tree rooted at node 0

				/***************************************************************/
				/**************************file transfer************************/
				System.out.println("Enter the name of the file: \n");
				String file_name = reader.readLine();
				writer.writeBytes(file_name + "\r\n");
				System.out.println("File name: "+file_name+"\n");
				DataInputStream socket_dis = new DataInputStream(socket.getInputStream());
				File file=new File(file_name);
				FileOutputStream fos= new FileOutputStream(file);
				byte[] buffer = new byte[1004];
				int total=0;
				while (true){
					int len =socket_dis.read(buffer);
					total+=len;
					if(len<=0)
						break;
					fos.write(buffer, 0, len); //writing a portion of buffer
				}

					
				break;
				
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