import java.io.IOException;

/**
*The program implements Server side of simple file transfer protocol 
*in the way that server first receives name of the requested file, its address and 
*the port on which TCP socket is going to listen for the requested file via UDP
*Server firstly sends a response if requested file has been found 
*If yes, sends file to client
*If not, sends list of available files to the client
*
*@author Ma³gorzata Kêska, Hubert Kuc, Beathe Rothmund
*@version 1.0
*@since 2016-11-06
*/

public class Main {

	public static void main(String[] args) throws IOException {
		UDPcontrolInfo udp=new UDPcontrolInfo(6009);
		while(true){
		System.out.println("Waiting for the FileRequest...");
		udp.ReceiveDataGram();
		System.out.println(udp.getHostName());
		TCPServer tcp=new TCPServer(udp.getHostName(),udp.getFileName(), udp.getPortNumber());
		tcp.SendFile();
		
		}
	}

}
