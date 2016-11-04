import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPcontrolInfo {

	DatagramSocket clientSocket;
	byte [] data;
	
	public UDPcontrolInfo(int portNumber){
		try {
			clientSocket=new DatagramSocket(6006);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}
	public Boolean InitializeByteArray(String FileName, String HostName, int TCPportNumber){
		String _2sendString="'"+FileName+';'+HostName+','+TCPportNumber+",";
		System.out.println(_2sendString);
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
	public Boolean SendDataGram(){
		
		DatagramPacket packet =	new DatagramPacket(data,data.length);
		try {
			packet.setAddress(InetAddress.getByName("localhost"));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		packet.setPort(6009);
				try {
					clientSocket.send(packet);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		
		return true;
				
	}
	
}
