import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Class implements UDP part used to tranfer control informatio
 * 
 *@author Ma³gorzata Kêska, Hubert Kuc, Beathe Rothmund
 *@version 1.0
 *@since 2016-11-06
 *
 */

public class UDPcontrolInfo {

	DatagramSocket clientSocket;
	byte [] data;
	
	//Constructor creates Datagram Socket
	public UDPcontrolInfo(int portNumber){
		try {
			clientSocket=new DatagramSocket(6008);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Method is use to convert string into byte array
	public Boolean InitializeByteArray(String FileName, String HostName, int TCPportNumber){
		String _2sendString="'"+FileName+';'+HostName+','+TCPportNumber+",";
		data=new byte[_2sendString.length()];
		try {
			data=_2sendString.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	
	}
	
	
	public Boolean SendDataGram(String ServerAddres){
		
		DatagramPacket packet =	new DatagramPacket(data,data.length);
		try {
			packet.setAddress(InetAddress.getByName(ServerAddres));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		packet.setPort(6009);
				try {
					clientSocket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
	
		return true;
				
	}
	
}
