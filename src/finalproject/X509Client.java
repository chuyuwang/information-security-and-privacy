package finalproject;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.crypto.Cipher;

public class X509Client {
	
	public static void main(String[] args) throws Exception 
	
	{
	String message = "The quick brown fox jumps over the lazy dog.";
	String host = "127.0.0.1";
	int port = 7999;
	Socket s = new Socket(host, port);
	
	InputStream inStream = new FileInputStream("chuyu.cer");//read the chuyu.cer file and take it into X509Certificate in the program
	CertificateFactory cf = CertificateFactory.getInstance("X.509");
	X509Certificate cert = (X509Certificate)cf.generateCertificate(inStream);
	inStream.close();
	System.out.println("The content of the certificate is:"+cert.toString());//print out the content of the certificate
	
	//Checking the expiration date and verifying
	Date date = cert.getNotAfter(); //get the notAfter date from the validity period of the certificate.
	if(date.after(new Date())) //check the whether the date is the expiration date
	{
		System.out.println("The certificate is present.");
	}
	else
	{
		System.out.println("The certificate is expired.");
	}

	//verify the validity
	try
	{
		cert.checkValidity(date);
		System.out.println("The certificate is valid.");
	} catch(Exception e)
	{
		System.out.println("The certificate is invalid.");
	}
	
	PublicKey publickey = (PublicKey) cert.getPublicKey(); //get the public key from certificate
	
	//cipher the message
	Cipher cipher = Cipher.getInstance("RSA");
	cipher.init(Cipher.ENCRYPT_MODE, publickey);//encrypt the message using publickey
	byte[] ciphertext = cipher.doFinal(message.getBytes());
	System.out.println("The message has already ciphered.");
	ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
	out.writeObject(ciphertext); //write the ciphered message and send to the server
	
	out.flush();
	out.close();
	s.close();
	
	}

}
