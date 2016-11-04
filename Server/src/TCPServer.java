import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPServer {
String ClientAddress;
String FileName;
int PortNumeber;

Socket serverSocket;

public TCPServer(String clientAddress, String fileName, int portNumber){

		FileName=fileName;
	
		try {
			serverSocket=new Socket(ClientAddress,portNumber);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

}
OutputStream out = null;
public Boolean SendFile(){
	try {
		File file=new File(FileName);
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream in = new BufferedInputStream(fis);
        byte[] buffer = new byte[(int)file.length()];
       // in.read(buffer,0,buffer.length);
        out = serverSocket.getOutputStream();
        out.flush();
        int count = 0;
        System.out.println(buffer.length);
        System.out.println(count);
        while ((count = in.read(buffer)) > 0){
            out.write(buffer,0,count);
       
        }
        out.close();
        in.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	

    
	return null;
	
}
}
