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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.PriorityQueue;
import java.util.Collections;
import java.util.Random;

import javax.xml.ws.handler.MessageContext.Scope;

// The network is represented by a graph, that contains nodes and edges
class Node implements Comparable<Node>
{
	public final int name;
	public Edge[] neighbors;
	public double minDistance = Double.POSITIVE_INFINITY;
	public Node previous;     // to keep the path
	public Node(int argName) 
	{ 
		name = argName; 
	}

	public int compareTo(Node other)
	{
		return Double.compare(minDistance, other.minDistance);
	}
}

class Edge
{
	public final Node target;
	public final double weight;
	public Edge(Node argTarget, double argWeight)
	{ 
		target = argTarget;
		weight = argWeight; 
	}
}

public class RoutingClient {

	static String mode;
	static String host;
	static int port;

	public static void adjacenyToEdges(double[][] matrix, List<Node> v)
	{
		for(int i = 0; i < matrix.length; i++)
		{
			v.get(i).neighbors = new Edge[matrix.length];
			for(int j = 0; j < matrix.length; j++)
			{
				v.get(i).neighbors[j] =  new Edge(v.get(j), matrix[i][j]);	
			}
		}
	}
	public static void computePaths(Node source)
	{
		PriorityQueue<Node> NodeQueue=new PriorityQueue<Node>();
		source.minDistance=0;
		NodeQueue.add(source);
		while(NodeQueue.isEmpty()==false){
			Node sourceNode=NodeQueue.poll();
			Edge[] edge=sourceNode.neighbors;
			for(Edge e:edge){
				Node targetNode=e.target;
				double distance=sourceNode.minDistance+e.weight;
				if(distance<targetNode.minDistance){
					NodeQueue.remove(targetNode);
					targetNode.minDistance=distance;
					targetNode.previous=sourceNode;
					NodeQueue.add(targetNode);
				}
			}
		}
	}

	public static List<Integer> getShortestPathTo(Node target)
	{
		List<Integer> path=new ArrayList<Integer>();
		while(target!=null){
			path.add(0,target.name);
			target=target.previous;
		}
		return path;
	}

	/**
	 * @param args
	 */

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
				adjacenyToEdges(matrix, nodeList);
				
				// Finding shortest path for all nodes
				
				for(Node start:nodeList){
					System.out.println("Node"+start.name);
					computePaths(start);
					for(Node end:nodeList){
						List<Integer> path=getShortestPathTo(end);
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
