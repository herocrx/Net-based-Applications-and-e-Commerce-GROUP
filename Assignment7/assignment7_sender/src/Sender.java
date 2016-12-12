import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;


public class Sender {

	
	final String PUBLIC_KEY_PATH = "keys/key_b.public";
	private ServerSocket srvSock=null;
	private Socket innerSock=null;
	File file;
	
	// create new TCP socket on the given port
	public Sender(int port){
		try {
			srvSock=new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	// read a file from disk, encrypt it with AES using the given symmetric key and send it over the given output stream
	public void encryptFile(OutputStream out, SecretKey symmetricKey, File file) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidAlgorithmParameterException{
		
		byte[] iv = new byte[16];
		
		// get AES algorithm
		Cipher aesCipherForEncryption = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		//Cipher aesCipherForEncryption = Cipher.getInstance("AES/CBC/NoPadding");
		aesCipherForEncryption.init(Cipher.ENCRYPT_MODE, symmetricKey, new IvParameterSpec(iv));
		CipherOutputStream aesOut = new CipherOutputStream(out, aesCipherForEncryption);
		
		// read file, encrypt it and send it over TCP connection
		byte[] buffer = new byte[4096];
		BufferedInputStream fileIn = new BufferedInputStream(new FileInputStream(file));
		int count;
		while ((count = fileIn.read(buffer)) > -1){
	    	// and send it to the receiver (encrypted using symmetric encryption)
			//byte[] encryptedInfo = aesCipherForEncryption.doFinal(buffer);
	    	//out.write(encryptedInfo,0,encryptedInfo.length);
			aesOut.write(buffer, 0, buffer.length);
	    }
	    //out.close();
		aesOut.close();
	    fileIn.close();  
				
			
	
	}
	
	
	/* 
	 * implements all functionality of sending encrypted files:
	 * method generates symmetric key, encrypts symmetric key using RSA, sends encrypted
	 * symmetric key to receiver, encrypts file with AES and sends it to the receiver as well
	 */
	public void sendFile() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidAlgorithmParameterException{

		// accept TCP connection from receiver
		innerSock=srvSock.accept();

		// read file
		String FilePath="video.mp4";
		file=new File(FilePath);

		// send file length and file name to receiver
		PrintWriter infoStream = new PrintWriter(innerSock.getOutputStream(), true); 
		OutputStream byteOut = innerSock.getOutputStream();

		// generate a new symmetric key
		SecretKey symmetricKey = generateSecretKey();
		System.out.println("Symmetric Key Sender: " + new String(symmetricKey.getEncoded()));

		// send symmetric key RSA-encrypted
		byte[] encryptedSymmetricKey = encryptSymmetricKey(symmetricKey);
		infoStream.println(encryptedSymmetricKey.length);
		byteOut.write(encryptedSymmetricKey);
		
		// send filename
		infoStream.println(file.getName());
		
		// send encrypted file
		encryptFile(byteOut, symmetricKey, file);
		
	}
	
	// method encrypts symmetric key using public key of the receiver and RSA algorithm and returns encrypted byte[]
	private byte[] encryptSymmetricKey(SecretKey symmetricKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		PublicKey publicKey = readPublicKey(PUBLIC_KEY_PATH);
		Cipher rsaAlgorithm = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		rsaAlgorithm.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] encryptedKey = rsaAlgorithm.doFinal(symmetricKey.getEncoded());
		
		return encryptedKey;
	}
	
	// method generates a new symmetric key and returns it
	private SecretKey generateSecretKey() throws NoSuchAlgorithmException {
		SecretKey symmetricKey;
		
		// get an instance of a symmetric algorithm
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		keyGen.init(128);
		// generate a new symmetric key
		symmetricKey = keyGen.generateKey();
				
		return symmetricKey;
	}
		
	
	// reads the public key of the receiver
	public PublicKey readPublicKey(String publicKeyFile){
		PublicKey publicKey = null;
		try {
			FileInputStream fis;
			fis = new FileInputStream(publicKeyFile);
			ObjectInputStream in = new ObjectInputStream(fis);
			publicKey = (PublicKey) in.readObject();
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return publicKey;
	}
	
}
