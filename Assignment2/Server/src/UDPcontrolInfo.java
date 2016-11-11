import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
*Class implements UDP server used for receiving control information
*
*@author Ma³gorzata Kêska, Hubert Kuc, Beathe Rothmund
*@version 1.0
*@since 2016-11-06
*/

public class UDPcontrolInfo {
	DatagramSocket serverSocket;
	byte[] data = new byte[256];

	
	//Constructor initialize the socket
	public UDPcontrolInfo(int portNumber){
		try {
			serverSocket=new DatagramSocket(portNumber);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public Boolean ReceiveDataGram(){
	DatagramPacket packet = new DatagramPacket(data,data.length);
		try {
		serverSocket.receive(packet);
	} catch (IOException e) {
		e.printStackTrace();
	}
	byte[] dataReceived =packet.getData();
	String stringReceived = null;
	try {
		stringReceived = new String(dataReceived, "UTF-8");
	} catch (UnsupportedEncodingException e) {
		e.printStackTrace();
	}
	System.out.println(stringReceived);
String [] receivedStrArray=stringReceived.split(";");
FileName=receivedStrArray[0];
HostName=receivedStrArray[1];
PortNumber=Integer.parseInt(receivedStrArray[2]);
	//Separates the part of the received message based on pattern
//	Pattern pattern=Pattern.compile("'(.*?);");
//	Matcher matcher = pattern.matcher(stringReceived);
//	if (matcher.find())
//	{
//	FileName=matcher.group(1);
//	}
//	pattern=Pattern.compile(";(.*?),");
//	matcher = pattern.matcher(stringReceived);
//	if (matcher.find())
//	{
//	HostName=matcher.group(1);
//	}
//	pattern=Pattern.compile(",(.*?),");
//	matcher = pattern.matcher(stringReceived);
//	if (matcher.find())
//	{
//	PortNumber=Integer.parseInt(matcher.group(1));
//	}
	System.out.println(FileName);
	return true;
}
	
	//Getters
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
