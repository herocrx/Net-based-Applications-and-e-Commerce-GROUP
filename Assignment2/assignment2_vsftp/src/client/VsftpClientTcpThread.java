package client;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class VsftpClientTcpThread extends Thread {
	ServerSocket serverSocket;
	String requestedFile;
	
	public VsftpClientTcpThread(int port, String reqFile) {	
		this.requestedFile = reqFile;
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		try {
			Socket clientSocket = serverSocket.accept();
			
			// save file to disk
			DataInputStream inStream = new DataInputStream(clientSocket.getInputStream());
			FileOutputStream fileStream = new FileOutputStream("requested_" + new File(requestedFile).getName());
			byte[] buffer = new byte[4096];
			while (inStream.read(buffer) > 0) {
				fileStream.write(buffer);
			}
			inStream.close();
			fileStream.close();
			System.out.println("file saved to disk");
			
//			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//			System.out.println(in.readLine());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
