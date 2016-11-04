import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		UDPcontrolInfo udp=new UDPcontrolInfo(6000);
		Scanner filename=new Scanner(System.in);
		System.out.println("Podaj nazwê pliku");
		String FileName=filename.nextLine();
		filename.close();
		udp.InitializeByteArray(FileName, "JA", 2001);
		udp.SendDataGram();
		TCPpart tcp=new TCPpart(2001,FileName);
		tcp.ReceiveFile();
	}

}
