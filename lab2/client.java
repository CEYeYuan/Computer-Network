import java.net.*;
import java.io.*;

public class client {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Hello World, I'm client");
		try{

			Socket socket = new Socket("localhost",9870);
			BufferedReader socket_reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
			long time=0;
			while(true){
			
				String str = reader.readLine();
				writer.writeBytes(str + "\r\n");
				System.out.println("client: I'd like to get file \""+str+"\"\n");

			    str = socket_reader.readLine();
				System.out.println("server responese:\""+str+"\"\n");
			
					if(str.equalsIgnoreCase("File exists, everything works fine! The file transfer is about to begin")){
						System.out.println("about to accept the file");
						// read data from socket
						// define once:
						DataInputStream socket_dis = new DataInputStream(socket.getInputStream());
						File file=new File("test.txt");
						FileOutputStream fos= new FileOutputStream(file);
						byte[] buffer = new byte[1024];
						while (true){
							int len =socket_dis.read(buffer);
							if(len<=0){
								System.out.println("done receiving data.");
								break;
							}
							fos.write(buffer, 0, len); //writing a portion of buffer
						}	
						socket.close();
					}
			
			
			//"\r\n" means the end of a packet
			if(str.equalsIgnoreCase("quit"))
				break;
			}
			socket.close();
		}catch(Exception e){e.printStackTrace();}
	}

}
