import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPServer {
String ClientAddress;
String FileName;
int PortNumeber;
Socket serverSocket;

//Contructor
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
public Boolean SendFile() throws IOException{
	File file=null;

	Boolean noException=true;
		String FilePath="FilesToTranfer"+"\\"+FileName;
		file=new File(FilePath);
	    FileInputStream fis = null;
	    PrintWriter FileFoundAnswer =new PrintWriter(serverSocket.getOutputStream(), true); 
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("No such File");
			
			
			FileFoundAnswer.println("No");
			String [] AvialiableFiles=ListFiles();
			FileFoundAnswer.println(AvialiableFiles.length);
			for(int i=0; i<AvialiableFiles.length; i++){
				FileFoundAnswer.println(AvialiableFiles[i]);
			}
			
			return false;
		}
		FileFoundAnswer.println("File found... Sending");
	    BufferedInputStream in = new BufferedInputStream(fis);
	    byte[] buffer = new byte[(int)file.length()];
        try {
			out = serverSocket.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        int count = 0;
        try {
			while ((count = in.read(buffer)) > 0){
			    out.write(buffer,0,count);		
}
	out.close();
	in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
if(noException){
	System.out.println("File "+FileName+"has been succesfuly tranfered");
}
	return true;
	
}

private String[] ListFiles(){
	File folder = new File("FilesToTranfer");
	File[] listOfFiles = folder.listFiles();
	String [] File=new String[listOfFiles.length];
	    for (int i = 0; i < listOfFiles.length; i++) {
	          System.out.println("File " + listOfFiles[i].getName());
	          File[i]=listOfFiles[i].getName();    
	    }
	    return File;
}

}
