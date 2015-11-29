import java.io.*;
import java.net.*;
import java.util.*;
import javax.xml.ws.handler.MessageContext.Scope;

public class Routing{



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

}
