package server;


public class MainServer {

	public static void main(String[] args) {
		int udpPort = 3000;
		
		// start a new server that waits for datagrams
		VsftpServer server = new VsftpServer(udpPort);

	}

}
