import java.security.NoSuchAlgorithmException;


public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Sender SS=new Sender(1000);
		System.out.print("Server listens to connections");
		try {
			
			SS.sendFile();
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
