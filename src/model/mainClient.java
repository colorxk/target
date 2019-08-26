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
			//���ɹ�Կ��˽Կ 
			RSADEn RSA = new RSADEn();
			RSA.genKeyPair(); 
			System.out.println("���ӷ������ɹ�..."); 
			System.out.println("������ɵ�RSA��ԿΪ:" + RSA.keyMap.get(0)); 
			System.out.println("������ɵ�RSA˽ԿΪ:" + RSA.keyMap.get(1)); 
			
			//���͹�Կ
			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(RSA.keyMap.get(0));

			//���ռ��ܺ�AES��Կ
			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
			String messageEn = (String) ois.readObject();
			
			//���ܼ��ܺ��AES��Կ
			String AESPrivateKey = RSA.decrypt(messageEn,RSA.keyMap.get(1)); 
			System.out.println("�ͻ��˽��յ�AES��ԿΪ:" + AESPrivateKey);
			
			AES.setAESPrivateKey(AESPrivateKey);
			
			Message m = (Message) ois.readObject();
			byte[] enmess = m.getContent();
	        for(int i=0;i<enmess.length;i++) {
	        	System.out.print(enmess[i]+" ");
	        }
	        System.out.println();
	        
	        byte[] decrypt = AESEn.decrypt(m.getContent(), AESPrivateKey);
			System.out.println("���ܺ�����ݣ�" + new String(decrypt,"utf-8"));

			//�����Կͻ��˵��̲߳���
			OperationServer operation = new OperationServer(s,IP,AESPrivateKey);
			//������ͻ���ͨѶ���߳�
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
