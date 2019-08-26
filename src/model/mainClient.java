package model;

import java.io.*;
import java.net.Socket;

import common.Message;
import encryption.AES;
import encryption.AESEn;
import encryption.RSADEn;
import tools.OperationServer;

public class mainClient {
public static Socket s;
public static String IP = "127.0.0.1";
	
	public boolean getAESKey() {
		boolean b = false;

		try {
			s = new Socket(IP, 9999);
			//生成公钥和私钥 
			RSADEn RSA = new RSADEn();
			RSA.genKeyPair(); 
			System.out.println("连接服务器成功..."); 
			System.out.println("随机生成的RSA公钥为:" + RSA.keyMap.get(0)); 
			System.out.println("随机生成的RSA私钥为:" + RSA.keyMap.get(1)); 
			
			//发送公钥
			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(RSA.keyMap.get(0));

			//接收加密后AES秘钥
			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
			String messageEn = (String) ois.readObject();
			
			//解密加密后的AES秘钥
			String AESPrivateKey = RSA.decrypt(messageEn,RSA.keyMap.get(1)); 
			System.out.println("客户端接收到AES秘钥为:" + AESPrivateKey);
			
			AES.setAESPrivateKey(AESPrivateKey);
			
			Message m = (Message) ois.readObject();
			byte[] enmess = m.getContent();
	        for(int i=0;i<enmess.length;i++) {
	        	System.out.print(enmess[i]+" ");
	        }
	        System.out.println();
	        
	        byte[] decrypt = AESEn.decrypt(m.getContent(), AESPrivateKey);
			System.out.println("解密后的内容：" + new String(decrypt,"utf-8"));

			//建立对客户端的线程操作
			OperationServer operation = new OperationServer(s,IP,AESPrivateKey);
			//开启与客户端通讯的线程
			operation.start();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
			
		}
		return b;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		mainClient c = new mainClient();
		c.getAESKey();
	}
}
