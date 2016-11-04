import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	Boolean initSocket=true;
		UDPcontrolInfo udp=new UDPcontrolInfo(6000);

		BufferedReader filename = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Podaj nazwê pliku");
		String FileName=null;
		try {
			FileName=filename.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		udp.InitializeByteArray(FileName, "JA", 2001);
		udp.SendDataGram();
		TCPpart tcp=null;
		if(initSocket){
		tcp=new TCPpart(2001,FileName);
		initSocket=false;}
		
		tcp.ReceiveFile();
	}
	

}
