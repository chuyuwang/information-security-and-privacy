package finalproject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class MessageHashing {

	public static String hashMD5(String message) throws Exception 
	{
		return hashMessage(message,"MD5"); //return to MD5 algorithm of message
	}
	public static String hashSHA(String message) throws Exception
	{
		return hashMessage(message,"SHA"); //return to SHA algorithm of message
	}
	private static String hashMessage(String message,String algorithm) throws Exception
	{
		try
		{
			MessageDigest messagedigest=MessageDigest.getInstance(algorithm);//define a message digest object that implements MD5 algorithm or SHA algorithm
			byte[] bytes=messagedigest.digest(message.getBytes("UTF-8")); //convert string message to bytes
			return ConvertBytestoHashResult(bytes); //return to calculate the message to MD5 and SHA results
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException ex)
		{
			 throw new Exception("Message cannot be transfered to MD5 or SHA",ex); //print out the error message
	        }
	}
	private static String ConvertBytestoHashResult(byte[] Bytes) //convert the bytes using MD5 algorithm or SHA algorithm to String results
	{
		StringBuffer stringbuffer=new StringBuffer();
		for(int i=0;i<Bytes.length;i++)
		{
			stringbuffer.append(Integer.toString((Bytes[i] & 0xff)+0x100,16).substring(1));
		}
		return stringbuffer.toString();
	}
	public static void main(String[] args) //main function
	{
		try
		{
			System.out.println("Input Message:");  //input the message
			Scanner scanner = new Scanner(System.in);
			String inputmessage=scanner.nextLine();

			
			String MD5message=hashMD5(inputmessage); //return the result of MD5
			System.out.println("MD5:"+MD5message);
			
			String SHAmessage=hashSHA(inputmessage); //return the result of SHA
			System.out.println("SHA:"+SHAmessage);
					
		} catch(Exception ex) //print out the error 
		{
			ex.printStackTrace();
		}
	}

}

