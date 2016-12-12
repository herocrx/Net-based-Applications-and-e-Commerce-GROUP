import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class Receiver {
	Socket socket;
	
	public void openConnection(String host, int port) throws UnknownHostException, IOException {
		socket = new Socket(InetAddress.getByName(host), port);
	}
	
	public void receiveFile() throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		DataInputStream inStream = new DataInputStream(socket.getInputStream());
				
		// read and decrypt symmetric key
		int keyLength = Integer.parseInt(readLine(inStream).trim());		
		SecretKey symmetricKey = decryptSymmetricKey(inStream, keyLength);
		System.out.println("Symmetric Key Receiver: " + new String(symmetricKey.getEncoded()));
		
		// read file name
		String fileName = readLine(inStream).trim();
		
		// save file to disk
		saveFile(inStream, symmetricKey, fileName);
		
		
	}
	
	// reads AES encrypted file from socket input stream, decrypts it and saves it to disk 
	public void saveFile(DataInputStream inStream, SecretKey symmetricKey, String fileName) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		FileOutputStream fileStream = new FileOutputStream(fileName);
		byte[] iv = new byte[16];

		// get AES algorithm
		Cipher aesAlgorithm = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		//Cipher aesAlgorithm = Cipher.getInstance("AES/CBC/NoPadding");
		aesAlgorithm.init(Cipher.DECRYPT_MODE, symmetricKey, new IvParameterSpec(iv));
		CipherInputStream aesIn = new CipherInputStream(inStream, aesAlgorithm);
		
		// decrypt bytes and save them to disk
		byte[] buffer = new byte[4096];
		int length;
		//while ((length = inStream.read(buffer)) != -1) {
		while ((length = aesIn.read(buffer)) != -1) {
			//byte[] decodedBytes = aesAlgorithm.doFinal(buffer, 0, length);
			//fileStream.write(decodedBytes, 0, decodedBytes.length);
			fileStream.write(buffer, 0, length);
		}
		aesIn.close();
		fileStream.close();
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
	
	// reads RSA encrypted symmetric key from input stream, decrypts it and creates a new SecretKey that is returned
	private SecretKey decryptSymmetricKey(DataInputStream inStream, int keyLength) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
		SecretKey symmetricKey = null;
		
		// read encrypted key (as many bytes as specified by keyLength)
		byte[] encryptedKey = new byte[keyLength];
		inStream.readFully(encryptedKey);
		
		// get RSA decryption algrorithm using the private key
		PrivateKey privateKey = readPrivateKey("keys/key_b.privat");
		Cipher rsaAlgorithm = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		rsaAlgorithm.init(Cipher.DECRYPT_MODE, privateKey);
		
		// decrypt symmetric key
		byte[] decryptedKey = rsaAlgorithm.doFinal(encryptedKey);
		
		// convert byte[] to SecretKey
		symmetricKey = new SecretKeySpec(decryptedKey, 0, decryptedKey.length, "AES");
		
		return symmetricKey;
	}
	
	
	// reads private key from given file
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
