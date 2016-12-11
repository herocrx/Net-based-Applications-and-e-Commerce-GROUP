import java.security.NoSuchAlgorithmException;


public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Sender sender=new Sender(8001);
		System.out.print("Server listens to connections");
		try {
			
			sender.sendFile();
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
