package finalproject;

import java.io.*;
import java.net.*;
import java.security.*;

public class ProtectedServer
{
	public boolean authenticate(InputStream inStream) throws IOException, NoSuchAlgorithmException 
	{
		// IMPLEMENT THIS FUNCTION.
		DataInputStream in = new DataInputStream(inStream);
		String user = in.readUTF(); //read the user from the client
		String password = lookupPassword(user); //get the password from lookupPassword function
		long t1 = in.readLong(); //read the timestamps from the client
		long t2 = in.readLong();
		double d1 = in.readDouble(); //read the random numbers from the client
		double d2 = in.readDouble();
		byte[] recomputedDigest1 = Protection.makeDigest(user,password,t1,d1);//calculate the first digest result using SHA algorithm
		byte[] recomputedDigest2 = Protection.makeDigest(recomputedDigest1, t2, d2); //calculate the second digest result using SHA algorithm 
		int length=in.readInt();
		byte[] receivedDigest=new byte[length];
		in.readFully(receivedDigest); //get the digest value of client 
		return MessageDigest.isEqual(recomputedDigest2,receivedDigest); //compare whether two digests are equal


	}

	protected String lookupPassword(String user) { return "abc123"; }

	public static void main(String[] args) throws Exception 
	{
		int port = 7999;
		ServerSocket s = new ServerSocket(port);
		Socket client = s.accept();

		ProtectedServer server = new ProtectedServer();

		if (server.authenticate(client.getInputStream()))
		  System.out.println("Client logged in.");
		else
		  System.out.println("Client failed to log in.");

		s.close();
	}
}