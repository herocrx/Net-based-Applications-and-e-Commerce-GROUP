package client;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class MainClient {

	public static void main(String[] args) {
		String host = "localhost";
		int tcpPort = 2000;
		int udpPort = 3000;
		String reqFile = "Lighthouse.jpg";
		
		// client starts a server socket that listens for the incoming file on port 2000
		VsftpClient client = new VsftpClient(reqFile);
		client.createServerSocket(tcpPort);
			
		System.out.println("sending datagram");
		// client sends a datagram with the requested file to the server
		client.sendDatagramToServer(host, udpPort, "Lighthouse.jpg", host, tcpPort);


	}

}
