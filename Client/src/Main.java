import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
/**
*The program implements Client of simple file transfer protocol 
*in the way that Client first sends name of the requested file, its address and 
*the port on which TCP socket is going to listen for the requested file
*Server firstly sends a response if requested file has been found 
*If yes, client receives the file
*If not, client receives list of available files
*
*@author Ma³gorzata Kêska, Hubert Kuc, Beathe Rothmund
*@version 1.0
*@since 2016-11-06
*/

public class Main {

	//Gets file name from user input
	private static String GetFileName(){
		BufferedReader filename = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Insert file name");
		String FileName=null;
		try {
			FileName=filename.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return FileName;
	}
	//Gets host name from user input
	private static String GetHostName(){
		BufferedReader hostName = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Insert host name");
		String HostName=null;
	
		try {
			HostName=hostName.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return HostName;
	}
	public static void main(String[] args) throws UnknownHostException {
	

		//Create TCPsocket on which server listen
		TCPpart tcp=new TCPpart(2001);
		//Create UDPsoctec to send control information
		UDPcontrolInfo udp=new UDPcontrolInfo(6000);
		
		while(true){
		String FileName=GetFileName();
		String HostName=null;
		if(args.length==0){
		HostName="localhost";
		}
		else{
		 HostName=GetHostName();	
		}
		
	
		System.out.println(InetAddress.getLocalHost().toString());
		udp.InitializeByteArray(FileName, InetAddress.getLocalHost().toString(), 2001);
		udp.SendDataGram(HostName);
	
		tcp.SetFileName(FileName);
		tcp.ReceiveFile();
	}
	}
	

}
