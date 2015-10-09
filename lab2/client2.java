import java.net.*;
import java.io.*;

public class client2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Hello World, I'm client");
		try{

			Socket socket_control = new Socket("localhost",9100);
			BufferedReader socket_reader = new BufferedReader(new InputStreamReader(socket_control.getInputStream()));
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			DataOutputStream writer = new DataOutputStream(socket_control.getOutputStream());
			long time=0;
			while(true){
			
				String str = reader.readLine();
				writer.writeBytes(str + "\r\n");
				System.out.println("client: I'd like to get file \""+str+"\"\n");

			    str = socket_reader.readLine();
				System.out.println("server responese:\""+str+"\"\n");


					if(str.equalsIgnoreCase("File exists, everything works fine! The file transfer is about to begin")){
						System.out.println("about to accept the file, please type in the name of the file to be stored localy");
						// read data from socket_control
						// define once:
						
						str = socket_reader.readLine();
						//System.out.println("server port_num:\""+str+"\"\n");
						int port_num=Integer.parseInt(str);
						Socket socket_data = new Socket("localhost",port_num);
					  
					
						DataInputStream socket_dis = new DataInputStream(socket_data.getInputStream());


						str = reader.readLine();
						File file=new File(str);
						FileOutputStream fos= new FileOutputStream(file);
						byte[] buffer = new byte[1024];
						int total=0;
						long time_start=System.currentTimeMillis();
						int len;
						while (true){
							len =socket_dis.read(buffer);
							total+=len;
							if(len<=0){
								long time_end=System.currentTimeMillis();
								System.out.println("start time: "+time_start+"  end time: "+time_end);
								System.out.println("time used: "+(time_end-time_start));
								System.out.println("done receiving data.");
								System.out.println("file.length()="+file.length());
								System.out.println("total character="+total);

								break;
							}
							fos.write(buffer, 0, len); //writing a portion of buffer
						}	
						fos.close();
						//socket_control.close();
						socket_data.close();
						if(socket_data.isClosed()){
								System.out.println("data socket is closed and the length of last input stream is "+len);
						}
					}
			
			
			//"\r\n" means the end of a packet
			if(str.equalsIgnoreCase("quit"))
				break;
			}
			socket_control.close();
		}catch(Exception e){e.printStackTrace();}
	}

}
