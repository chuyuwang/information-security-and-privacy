package finalproject;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.security.PrivateKey;

import javax.crypto.Cipher;

public class X509Server {
	public static void main(String[] args) throws Exception 
	{   
		String aliasname="myalias";
		char[] password = "123456".toCharArray();
		
		int port = 7999;
		ServerSocket server = new ServerSocket(port);
		Socket s = server.accept();
		
		//read the keystore and get the private key
		KeyStore keystore = KeyStore.getInstance("JKS");
		FileInputStream filein = new FileInputStream("chuyu.keystore");
		keystore.load(filein, password);
		PrivateKey privatekey = (PrivateKey) keystore.getKey(aliasname, password);
		
		//Decipher the message using the private key
		ObjectInputStream in = new ObjectInputStream(s.getInputStream());
		Cipher cipher = Cipher.getInstance("RSA");
		byte[] Mess = (byte[]) in.readObject(); //read ciphered message from client and transfer to bytes for deciphering
		cipher.init(Cipher.DECRYPT_MODE, privatekey);//decrypt message using private key
		byte[] plaintext = cipher.doFinal(Mess); //decrypt data
		System.out.println("The PlainText is:"+new String(plaintext));
		
		server.close();	
		
	}

}
