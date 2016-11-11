package client;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;


public class VsftpClient {
	ServerSocket serverSocket;
	String requestedFile;
	
	public VsftpClient(String requestedFile) {
		this.requestedFile = requestedFile;
	}
	
	public void createServerSocket(int port) {
//		Thread t = new VsftpClientTcpThread(port);
//		t.run();
		System.out.println("Establishing TCP-socket on client");
		ServerSocket serverSocket;
		new VsftpClientTcpThread(port, requestedFile).start();
	}
	
	
	Socket tcpSocket;
	
	public void openStreamToServer(String server, int tcpPort) {
		try {
			tcpSocket = new Socket(server, tcpPort);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendDatagramToServer(String serverHost, int serverPort, String file, String clientHost, int clientPort) {
		
		try {
			DatagramSocket dSocket = new DatagramSocket();
			
			// create datagram
			String infoStr = file + ";" + clientHost + ";" + clientPort;
			byte[] info = infoStr.getBytes();
			DatagramPacket datagram = new DatagramPacket(info, info.length, InetAddress.getByName(serverHost), serverPort);
			// and send it
			dSocket.send(datagram);
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
