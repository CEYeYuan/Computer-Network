import java.net.*;
import java.io.*;
public class server{
	public static void main(String[] args){
		System.out.println("Hello World, I' server");
		try{	
			ServerSocket server = new ServerSocket(9870);
			Socket socket=server.accept();

			BufferedReader socket_reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
			
			while(true){
			String str = socket_reader.readLine();
			System.out.println("client sent:"+str);
			File file=new File(str);
			
			if (file.exists()){
				writer.writeBytes("File exists, everything works fine! The file transfer is about to begin"+ "\r\n");
				//read data from the file
				FileInputStream fin= new FileInputStream(file);
				byte[] buffer = new byte[1024];
			}
				
				//server responds YES
			else
				writer.writeBytes("The file you claim does not exist"+ "\r\n");



			

			//two way to close the socket:1.file transfer complete 2.client said "quit"
			if(str.equalsIgnoreCase("quit")){
				socket.close();
				break;
			}
		  }
		}
		catch(Exception e){e.printStackTrace();}
	}	
}