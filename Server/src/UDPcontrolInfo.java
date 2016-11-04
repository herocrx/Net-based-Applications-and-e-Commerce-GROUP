import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UDPcontrolInfo {
	DatagramSocket serverSocket;
	byte[] data = new byte[256];

	public UDPcontrolInfo(int portNumber){
		try {
			serverSocket=new DatagramSocket(portNumber);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Boolean ReceiveDataGram(){
	DatagramPacket packet = new DatagramPacket(data,data.length);
		try {
		serverSocket.receive(packet);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	byte[] dataReceived =packet.getData();
	//int bytesReceived =	packet.getLength();
	String stringReceived = null;
	try {
		stringReceived = new String(dataReceived, "UTF-8");
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	Pattern pattern=Pattern.compile("'(.*?);");
	Matcher matcher = pattern.matcher(stringReceived);
	if (matcher.find())
	{
	FileName=matcher.group(1);
	}
	pattern=Pattern.compile(";(.*?),");
	matcher = pattern.matcher(stringReceived);
	if (matcher.find())
	{
	HostName=matcher.group(1);
	}
	pattern=Pattern.compile(",(.*?),");
	matcher = pattern.matcher(stringReceived);
	if (matcher.find())
	{
	PortNumber=Integer.parseInt(matcher.group(1));
	}
	System.out.println(FileName);
	return true;
}
	String FileName;
	public String getFileName(){
		return FileName;
	}
	String HostName;
	public String getHostName(){
		return HostName;
	}
	int PortNumber;
	public int getPortNumber(){
		return PortNumber;
	}
	
}
