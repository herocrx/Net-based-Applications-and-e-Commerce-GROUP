import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.ServerSocket;
import java.net.Socket;

public class TCPpart {
ServerSocket socket;
Socket clientSocket=null;
String FileName;
public TCPpart(int portNumber, String filename){
	try {
		socket=new ServerSocket(portNumber);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	FileName=filename;

}
public Boolean ReceiveFile(){
	try {
		clientSocket=socket.accept();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		
		
		  InputStream is = clientSocket.getInputStream();
	       File file = new File(FileName);
	        // Get the size of the file
	       	file.createNewFile();
	        FileOutputStream fos = new FileOutputStream(file);
	        BufferedOutputStream out = new BufferedOutputStream(fos);
	        int byteread;
	        
	        
	     

	        byte[] buffer = new byte[16384];

	        while ((byteread = is.read(buffer, 0, buffer.length)) != -1) {
	          out.write(buffer, 0, byteread);
	          System.out.println(byteread);
	        }
	        
	        out.flush();
	        out.close();
		/*BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		String inputLine;
		if  ((inputLine = in.readLine()) != null) {
		System.out.println(inputLine);
		}*/
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	return null;
	
}
}
