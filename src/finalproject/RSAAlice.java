package finalproject;

import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Scanner;

import javax.crypto.*;

public class RSAAlice
{
	public static void main(String[] args) throws Exception 
	{
		String message = "The quick brown fox jumps over the lazy dog.";
		String host = "127.0.0.1";
		int port = 7999;
		Socket s = new Socket(host, port);
		
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA"); //generate public/private pair using RSA algorithm
		keyGen.initialize(1024);
		KeyPair keypair = keyGen.genKeyPair();//generate a key pair
		PublicKey publickeyAlice = keypair.getPublic(); //generate Alice's public key
		PrivateKey privatekeyA = keypair.getPrivate(); //generate Alice's private key
		
		ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());//send the public key to Bob
		out.writeObject(publickeyAlice);
		
		ObjectInputStream in = new ObjectInputStream(s.getInputStream());// get the public key of Bob
		PublicKey publickeyB = (PublicKey) in.readObject(); 
		
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		
		//choose two choices of RSA public-key system
		System.out.println("Confidentiality:Enter 1"); // Confidentiality press 1
		System.out.println("Integerity.authentication:Enter 2"); //Integrity/Authentication press 2
				
		Scanner scanner = new Scanner(System.in); //input the requirement of confidentiality or integrity/authentication
		int input = scanner.nextInt();
		
		switch(input)
		{
		case 1:
			//encipher by Bob's public key
			cipher.init(Cipher.ENCRYPT_MODE, publickeyB);
			byte[] Mes = cipher.doFinal(message.getBytes());
			System.out.println("The ciphered message is:"+Mes);
			out.writeInt(1); 
			out.writeObject(Mes);
			out.flush();
			out.close();
			break;
			
		case 2:
			//encipher by Alice's own private key
			cipher.init(Cipher.ENCRYPT_MODE,privatekeyA);
			byte[] Message = cipher.doFinal(message.getBytes());
			System.out.println("The ciphered message is:"+Message);
			out.writeInt(2);
			out.writeObject(Message);
			out.flush();
			out.close();
			break;
		}
		scanner.close();
		s.close();
		
		
	}
}