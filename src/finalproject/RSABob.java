package finalproject;

import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Scanner;

import javax.crypto.*;

public class RSABob
{
	public static void main(String[] args) throws Exception 
	{
		int port = 7999;
		ServerSocket server = new ServerSocket(port);
		Socket s = server.accept();
		
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA"); //generate public/private pair using RSA algorithm
		keyGen.initialize(1024);
		KeyPair keypair = keyGen.genKeyPair();//generate a key pair
		PublicKey publickeyBob = keypair.getPublic(); //generate Bob's public key
		PrivateKey privatekeyB = keypair.getPrivate(); //generate Bob's private key
		
		ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());//send the public key of Bob to Alice
		out.writeObject(publickeyBob);

		
		ObjectInputStream in = new ObjectInputStream(s.getInputStream());// get the public key of Alice
		PublicKey publickeyA = (PublicKey) in.readObject(); 
		
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		int choice = in.readInt();
		
		switch(choice)
		{
		case 1:
			//decipher by Bob's own privatekey
			byte[] Mes = (byte[]) in.readObject();
			cipher.init(Cipher.DECRYPT_MODE, privatekeyB);
			byte[] plaintext = cipher.doFinal(Mes);
			System.out.println("The plaintext is:"+new String(plaintext));
			break;
			
		case 2:
			//decipher by Alice's public key
			byte[] Message = (byte[]) in.readObject();
			cipher.init(Cipher.DECRYPT_MODE,publickeyA);
			byte[] PlainText = cipher.doFinal(Message);
			System.out.println("The plaintext is:"+new String(PlainText));
			break;
		}
		server.close();
	}
}