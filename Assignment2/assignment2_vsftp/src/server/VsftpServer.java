package server;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;


public class VsftpServer {
	DatagramSocket dSocket;
	
	public VsftpServer(int port) {
		try {
			System.out.println("starting new Datagram socket");
			dSocket = new DatagramSocket(port);
			
			int filesSent = 0;
			while (filesSent < 5) {
				// wait for requests
				DatagramPacket datagram = new DatagramPacket(new byte[256], 256);
				System.out.println("waiting for file request");
				dSocket.receive(datagram);

				// find out witch file to fetch and the clients address and port
				String[] info = new String(datagram.getData(),0, datagram.getLength()).split(";");
				String filepath = info[0];
				String clientHost = info[1];
				int clientPort = Integer.parseInt(info[2]);

				sendFileToClient(filepath, clientHost, clientPort);
				filesSent++;
			}
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void sendFileToClient(String filepath, String clientHost, int clientPort) {
		try {
			// open stream to client
			System.out.println(clientHost + " - " + clientPort);
			Socket socket = new Socket(InetAddress.getByName(clientHost), clientPort);
			
			// read file from disk
			FileInputStream fileStream = new FileInputStream(filepath);
			
			// send file to client
			DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());
			byte[] buffer = new byte[4096];
			while (fileStream.read(buffer) > 0) {
				outStream.write(buffer);
			}
			fileStream.close();
			outStream.flush();
			outStream.close();
//			OutputStreamWriter writer = new OutputStreamWriter(out);
//			writer.write(filepath + (char) 13);
//			writer.flush();
			
			System.out.println("sent file");
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	

}
