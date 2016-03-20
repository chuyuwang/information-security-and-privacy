package finalproject;

import java.io.*;
import java.net.*;
import java.security.*;
import java.math.BigInteger;

public class ElGamalBob
{
	private static boolean verifySignature(	BigInteger y, BigInteger g, BigInteger p, BigInteger a, BigInteger b, String message) throws NoSuchAlgorithmException
	{
		// IMPLEMENT THIS FUNCTION;
		BigInteger computea = y.modPow(a, p); //calculate (y^a) mod p
		BigInteger computeb = a.modPow(b, p); //calculate (a^b) mod p
		BigInteger AmultiplyB = computea.multiply(computeb); // calculate [(y^a) mod p][(a^b) mod p]
		BigInteger receivedSingature = AmultiplyB.mod(p);
		
		MessageDigest messagedigest = MessageDigest.getInstance("SHA");//digest the message
		byte[] bytes = messagedigest.digest(message.getBytes());
		BigInteger m = new BigInteger(bytes);
		BigInteger checkSingature = g.modPow(m, p); // calculate g^m mod p
		
		if(receivedSingature.equals(checkSingature))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static void main(String[] args) throws Exception 
	{
		int port = 7999;
		ServerSocket s = new ServerSocket(port);
		Socket client = s.accept();
		ObjectInputStream is = new ObjectInputStream(client.getInputStream());

		// read public key
		BigInteger y = (BigInteger)is.readObject();
		BigInteger g = (BigInteger)is.readObject();
		BigInteger p = (BigInteger)is.readObject();

		// read message
		String message = (String)is.readObject();

		// read signature
		BigInteger a = (BigInteger)is.readObject();
		BigInteger b = (BigInteger)is.readObject();

		boolean result = verifySignature(y, g, p, a, b, message);

		System.out.println(message);

		if (result == true)
			System.out.println("Signature verified.");
		else
			System.out.println("Signature verification failed.");

		s.close();
	}
}