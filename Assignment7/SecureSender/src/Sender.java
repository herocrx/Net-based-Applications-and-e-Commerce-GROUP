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
	PublicKey key = null;
	private ServerSocket srvSock=null;
	private Socket innerSock=null;
	File file=null;
	int fileSize=0;
	String FileName="";
	OutputStream out = null;
	
	// create new TCP socket on the given port
	public Sender(int port){
		try {
			srvSock=new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	KeyPair kp = null;
	Cipher rsa = null;
	SecretKey secretKey=null;
	
	public byte[] EncodeSecretKey(){
		// read the public key of the receiver 
		ReadKey();
		// get an instance of a symmetric algorithm
		KeyGenerator keyGen = null;
		try {
			keyGen = KeyGenerator.getInstance("AES");
		} catch (NoSuchAlgorithmException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		keyGen.init(128);
		// generate a new symmetric key
		secretKey = keyGen.generateKey();
		// convert symmetric key to Base64-encoded string
		//String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
		
		try {
			rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			rsa.init(Cipher.ENCRYPT_MODE, key);
			//byte[] cipherBytes = rsa.doFinal((encodedKey.getBytes()));
			System.out.println("Symmetric key before encryption: " + new String(secretKey.getEncoded()));
			byte[] cipherBytes = rsa.doFinal(secretKey.getEncoded());
			
			return cipherBytes;
		} catch (IllegalBlockSizeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (BadPaddingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public byte[] Encryption(byte[] byteDataToEncrypt) throws NoSuchAlgorithmException{
		
//		final int AES_KEYLENGTH = 128;	// change this as desired for the security level you want
//		byte[] iv = new byte[AES_KEYLENGTH/8 ];	// Save the IV bytes or send it in plaintext with the encrypted data so you can decrypt the data later
//		SecureRandom prng = new SecureRandom();
//		prng.nextBytes(iv);
		
		
		try {
			Cipher aesCipherForEncryption = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			//aesCipherForEncryption.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
			aesCipherForEncryption.init(Cipher.ENCRYPT_MODE, secretKey);
				
			byte[] byteCipherText = aesCipherForEncryption
					.doFinal(byteDataToEncrypt);
			return byteCipherText;
		
	
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//		} catch (InvalidAlgorithmParameterException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public void sendFile() throws NoSuchAlgorithmException{
		try {
			// accept TCP connection from receiver
			innerSock=srvSock.accept();
		
			// read file
			String FilePath="video.mp4";
			file=new File(FilePath);
			
			// send file length and file name to receiver
			PrintWriter FileInfo =new PrintWriter(innerSock.getOutputStream(), true); 
			FileInfo.println(file.length());
			FileInfo.println(file.getName());
			
			// generate a new symmetric key, encrypt it using assymetric encryption and send it to the receiver
			FileInfo.println(new String(EncodeSecretKey()));
			FileInfo.flush();
			
			// read file
			FileInputStream fis = new FileInputStream(file);
		    BufferedInputStream in = new BufferedInputStream(fis);
		    byte[] buffer = new byte[(int)file.length()];
		    
		    
		    
		    try {
		    	out = innerSock.getOutputStream();
		    } catch (IOException e) {
		    	// TODO Auto-generated catch block
		    	e.printStackTrace();
		    } try {
		    	out.flush();
		    } catch (IOException e) {
		    	// TODO Auto-generated catch block
		    	e.printStackTrace();
		    }
		    int count = 0;

		    while ((count = in.read(buffer)) > 0){
		    	// and send it to the receiver (encrypted using symmetric encryption)
		    	byte[] encryptedInfo = Encryption(buffer);
		    	out.write(encryptedInfo,0,encryptedInfo.length);		
		    }
		    out.close();
		    in.close();  
		    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
		
		
	
	// reads the public key of the receiver
	public void ReadKey(){
		try {
			FileInputStream fis;
			fis = new FileInputStream(PUBLIC_KEY_PATH);
			ObjectInputStream in = new ObjectInputStream(fis);
			key = (PublicKey) in.readObject();
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
