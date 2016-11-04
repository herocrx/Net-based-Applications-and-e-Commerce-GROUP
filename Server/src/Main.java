

public class Main {

	public static void main(String[] args) {
		UDPcontrolInfo udp=new UDPcontrolInfo(6009);
		udp.ReceiveDataGram();
		TCPServer tcp=new TCPServer(udp.getHostName(),udp.getFileName(), udp.getPortNumber());
		tcp.SendFile();
	}

}
