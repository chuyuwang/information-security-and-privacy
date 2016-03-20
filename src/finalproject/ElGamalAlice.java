package finalproject;

import java.io.*;
import java.net.*;
import java.security.*;
import java.math.BigInteger;

public class ElGamalAlice
{
	private static BigInteger computeY(BigInteger p, BigInteger g, BigInteger d)
	{
		// IMPLEMENT THIS FUNCTION;
		BigInteger computeY = g.modPow(d, p); //compute y = g^d mod p
		return  computeY;
	}

	private static BigInteger computeK(BigInteger p)
	{
		// IMPLEMENT THIS FUNCTION;
		BigInteger pOne = BigInteger.valueOf(1); //define p-1
		BigInteger pMinusOne = p.subtract(pOne);
		SecureRandom rnd = new SecureRandom(); //generate a random number
		int numbits = 1024;
		BigInteger computeK = new BigInteger(numbits,rnd); //choose k that is relatively prime to (p-1)
		while(computeK.gcd(pMinusOne).equals(pOne)!=true)
		{
			computeK = new BigInteger(numbits,rnd);
		}
		return computeK;
		
	}
	
	private static BigInteger computeA(BigInteger p, BigInteger g, BigInteger k)
	{
		// IMPLEMENT THIS FUNCTION;
		BigInteger computeA = g.modPow(k, p); //compute a = g^k mod p
		return computeA;
	}

	private static BigInteger computeB(	String message, BigInteger d, BigInteger a, BigInteger k, BigInteger p) throws NoSuchAlgorithmException
	{
		// IMPLEMENT THIS FUNCTION;
		BigInteger pOne = BigInteger.valueOf(1);
		BigInteger pMinusOne = p.subtract(pOne); // define p-1
		BigInteger h = k.modInverse(pMinusOne); //compute h = k.modInverse(p-1)		
		MessageDigest messagedigest = MessageDigest.getInstance("SHA");//digest the message
		byte[] bytes = messagedigest.digest(message.getBytes());
		BigInteger m = new BigInteger(bytes);
		BigInteger num = d.multiply(a);//compute b=((m-da)*h) mod (p-1)
		BigInteger res1 = m.subtract(num);
		BigInteger res2 = res1.multiply(h);
		BigInteger computeB = res2.mod(pMinusOne);
		return computeB;
		
		
	}

	public static void main(String[] args) throws Exception 
	{
		String message = "The quick brown fox jumps over the lazy dog.";

		String host = "127.0.0.1";
		int port = 7999;
		Socket s = new Socket(host, port);
		ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());

		// You should consult BigInteger class in Java API documentation to find out what it is.
		BigInteger y, g, p; // public key
		BigInteger d; // private key

		int mStrength = 1024; // key bit length
		SecureRandom mSecureRandom = new SecureRandom(); // a cryptographically strong pseudo-random number

		// Create a BigInterger with mStrength bit length that is highly likely to be prime.
		// (The '16' determines the probability that p is prime. Refer to BigInteger documentation.)
		p = new BigInteger(mStrength, 16, mSecureRandom);
		
		// Create a randomly generated BigInteger of length mStrength-1
		g = new BigInteger(mStrength-1, mSecureRandom);
		d = new BigInteger(mStrength-1, mSecureRandom);

		y = computeY(p, g, d);

		// At this point, you have both the public key and the private key. Now compute the signature.

		BigInteger k = computeK(p);
		BigInteger a = computeA(p, g, k);
		BigInteger b = computeB(message, d, a, k, p);

		// send public key
		os.writeObject(y);
		os.writeObject(g);
		os.writeObject(p);

		// send message
		os.writeObject(message);
		
		// send signature
		os.writeObject(a);
		os.writeObject(b);
		
		s.close();
	}
}