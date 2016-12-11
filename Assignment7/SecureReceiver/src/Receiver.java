import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class Receiver {
	Socket socket;
	
	public void openConnection(String host, int port) throws UnknownHostException, IOException {
		socket = new Socket(InetAddress.getByName(host), port);
	}
	
	public void receiveFile() throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		DataInputStream inStream = new DataInputStream(socket.getInputStream());
		
		// read file length, file name and symmetric key
		int fileLength = Integer.parseInt(readLine(inStream).trim());
		String fileName = readLine(inStream).trim();
		SecretKey symmetricKey = decryptSymmetricKey(readLine(inStream));
		
		// save file to disk
		saveFile(inStream, symmetricKey, fileName, fileLength);
		
		
	}
	
	public void saveFile(DataInputStream inStream, SecretKey symmetricKey, String fileName, int fileLength) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		FileOutputStream fileStream = new FileOutputStream(fileName);
		
		Cipher aesAlgorithm = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		aesAlgorithm.init(Cipher.DECRYPT_MODE, symmetricKey);
		
		byte[] buffer = new byte[4096];
		int length;
		while ((length = inStream.read(buffer)) != -1) {
			byte[] decodedBytes = aesAlgorithm.doFinal(buffer, 0, length);
			fileStream.write(decodedBytes, 0, decodedBytes.length);
		}
	}
	
	// method reads a line from the byte stream and returns it as a string
	private String readLine(DataInputStream inStream) throws IOException {
		String line = "";
		int nextChar;
		while ((nextChar = inStream.read()) != -1) {
			line += (char) nextChar;
			if (nextChar == (int) '\n') {
				break;
			}
		}
		return line;
	}
	
	private SecretKey decryptSymmetricKey(String info) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		SecretKey symmetricKey = null;
		
		// convert String to byte[]
		byte[] infoBytes = info.getBytes();
		System.out.println("Received symmetric key (unencrypted): " + new String(infoBytes));
		// decrypt symmetric key (using own private key)
		Cipher rsaAlgorithm = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		rsaAlgorithm.init(Cipher.DECRYPT_MODE, readPrivateKey("keys/key_b.privat"));
		byte[] decryptedBytes = rsaAlgorithm.doFinal(infoBytes);
		// convert byte[] to SecretKey
		symmetricKey = new SecretKeySpec(decryptedBytes, 0, decryptedBytes.length, "AES");
		
		return symmetricKey;
	}
	
	private PrivateKey readPrivateKey(String privateKeyFile) {
		PrivateKey key = null;
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(privateKeyFile));
			key = (PrivateKey) in.readObject();
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return key;
	}

}
