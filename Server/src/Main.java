import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		UDPcontrolInfo udp=new UDPcontrolInfo(6009);
		while(true){
		System.out.println("Waiting for the FileRequest...");
		udp.ReceiveDataGram();
		TCPServer tcp=new TCPServer(udp.getHostName(),udp.getFileName(), udp.getPortNumber());
		tcp.SendFile();
		
		}
	}

}
