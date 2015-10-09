import java.net.*;
import java.io.*;
public class server2{
	public static void main(String[] args){
		System.out.println("Hello World, I' server");
		try{	
			ServerSocket server = new ServerSocket(9879);
			Socket socket_control=server.accept();
			

			BufferedReader socket_reader = new BufferedReader(new InputStreamReader(socket_control.getInputStream()));
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			DataOutputStream writer = new DataOutputStream(socket_control.getOutputStream());
			
			while(true){
			String str = socket_reader.readLine();
			System.out.println("client sent:"+str);
			File file=new File(str);
			
			if (file.exists()){
				long length=file.length();
						System.out.println("file.length() = "+length);
				
				writer.writeBytes("File exists, everything works fine! The file transfer is about to begin"+ "\r\n");
				//read data from the file
				FileInputStream fin= new FileInputStream(file);
				byte[] buffer = new byte[1024];
				// define once:

				//new connection send port num to client
				int port_num=9899;
				ServerSocket server2 = new ServerSocket(9899);
				Socket socket_data=server2.accept();
				writer.writeBytes(port_num+"\r\n");

				DataOutputStream socket_dos = new DataOutputStream(
						socket_data.getOutputStream());
				String CRLF = "\r\n";
				int len;
				while(true){
					//reads up to len bytes of data from input stream to buffer; return the # of bytes
					len=fin.read(buffer,0,1024);
					// write lines to socket_control:
					if(len<=0)	{	
						break;
					}
					//use everytime:
					socket_dos.write(buffer, 0, len); //writing a portion of buffer
				}
				fin.close();
				//socket_control.close();
				socket_data.close();
				
				
			}
				
				//server responds YES
			else
				writer.writeBytes("The file you claim does not exist"+ "\r\n");


			//two way to close the socket_control:1.file transfer complete 2.client said "quit"
			if(str.equalsIgnoreCase("quit")){
				socket_control.close();
				break;
			}
		  }
		}
		catch(Exception e){e.printStackTrace();}
	}	
}