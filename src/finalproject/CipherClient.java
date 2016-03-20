package finalproject;

import java.io.*;
import java.net.*;
import java.security.*;

import javax.crypto.*;

public class CipherClient
{
	public static void main(String[] args) throws Exception 
	{
		String message = "The quick brown fox jumps over the lazy dog.";
		String host = "127.0.0.1";
		int port = 7999;
		Socket s = new Socket(host, port);

		// YOU NEED TO DO THESE STEPS:
		// -Generate a DES key.
		// -Store it in a file.
		// -Use the key to encrypt the message above and send it over socket s to the server.	
		
		KeyGenerator keygenerator = KeyGenerator.getInstance("DES"); //generate a DES key
		SecretKey myDesKey = keygenerator.generateKey();
		
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("KeyFile.txt")); //create an output file for restoring the DES key
		Key k = myDesKey;
		out.writeObject(k); // storing the key in the output file

		
		ObjectOutputStream outSocket = new ObjectOutputStream(s.getOutputStream()); //send the DES key to the server
		outSocket.writeObject(k);
		outSocket.flush();
		
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");//cipher object to encrypt mode with the specified key k
		cipher.init(Cipher.ENCRYPT_MODE, k); 
		CipherOutputStream cipherOut = new CipherOutputStream(s.getOutputStream(),cipher); //write the message to pass the encrypted value to the server
		byte[] m = message.getBytes();
		cipherOut.write(m);
		
		System.out.println("The message is:"+message); //print out the origin message
		System.out.println("The encryption message is:"+m); //print out the encrypted message
		
		cipherOut.close();
		s.close();
		out.close();
		
		
		
	}
}