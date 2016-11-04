import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
		BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	
		String FileFound=in.readLine();
		
		//FileFound="No such file";
		if(FileFound.contains("No")){
			
			System.out.println("No such file");
			System.out.println("Available files: ");
			int counter=Integer.parseInt(in.readLine());
			for(int i=0;i<counter;i++)
			{
				System.out.println(in.readLine());
				
			}
			return false;
		}
		System.out.println("File found");
		
		  InputStream is = clientSocket.getInputStream();
	       File file = new File("ReceivedFiles\\"+FileName);
	        // Get the size of the file
	       	file.createNewFile();
	        FileOutputStream fos = new FileOutputStream(file);
	        BufferedOutputStream out = new BufferedOutputStream(fos);
	        int byteread;            
	        byte[] buffer = new byte[16384];

	        while ((byteread = is.read(buffer, 0, buffer.length)) != -1) {
	          out.write(buffer, 0, byteread);
	          
	        }
	        
	        out.flush();
	        out.close();
	
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	System.out.println("File succesfuly tranfered");
	return true;
	
}
}
