import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
/**
*Class implements TCP part of the program
*Class is used to send requested via UDP file to the client
*
*@author Ma³gorzata Kêska, Hubert Kuc, Beathe Rothmund
*@version 1.0
*@since 2016-11-06
*/


public class TCPServer {

String FileName;
int PortNumeber;
Socket serverSocket;

//Constructor initialize the socket and the file name
public TCPServer(String clientAddress, String fileName, int portNumber){
	FileName=fileName;	
	try {
		
			serverSocket=new Socket(clientAddress,portNumber);
		} catch (UnknownHostException e) {
				e.printStackTrace();
		} catch (IOException e) {
				e.printStackTrace();
		}

}

OutputStream out = null;
//Method checks if requested file is on the server and if yes sends the file
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
