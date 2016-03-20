package finalproject;

import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Random;

public class ProtectedClient
{
	public void sendAuthentication(String user, String password, OutputStream outStream) throws IOException, NoSuchAlgorithmException 
	{

		// IMPLEMENT THIS FUNCTION.
		DataOutputStream out = new DataOutputStream(outStream);
		
		long t1=System.nanoTime(); // generate timestamp1
		long t2=System.nanoTime(); // generate timestamp2
		
		Random r1=new Random(); // generate one random number
		double d1=r1.nextDouble();
		Random r2=new Random();//generate another random number
		double d2=r2.nextDouble();
		
		byte[] digestValue1 = Protection.makeDigest(user,password,t1,d1); // make digest of user and password
		byte[] digestValue2 = Protection.makeDigest(digestValue1, t2, d2); //make the second digest algorithm
		
		out.writeUTF(user); //pass the value to server
		out.writeLong(t1);
		out.writeLong(t2);
		out.writeDouble(d1);
		out.writeDouble(d2);
		out.writeInt(digestValue2.length); //pass the length of digest value
		out.write(digestValue2); //pass the digestValue
		out.flush();
	}

	public static void main(String[] args) throws Exception 
	{
		String host = "127.0.0.1";
		int port = 7999;
		String user = "George";
		String password = "abc";
		Socket s = new Socket(host, port);

		ProtectedClient client = new ProtectedClient();
		client.sendAuthentication(user, password, s.getOutputStream());

		s.close();
	}
}