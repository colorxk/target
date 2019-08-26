package remote;

import java.io.DataOutputStream;
import java.net.Socket;

public class RemoteClient extends Thread{
/*  public static void main(String[] args) throws IOException {
	  Socket s = new Socket("127.0.0.1",8000);
      DataOutputStream dos = new DataOutputStream(s.getOutputStream());
      //将客户端与服务器端链接的输出流交个ImageThread处理
      ImageThread imageThread = new ImageThread(dos);
      new Thread(imageThread).start();
  }*/
  String IP;
  public RemoteClient(String IP) {
	  this.IP = IP;
  }
	
	
  public void run() {
	  try {
		  Socket s = new Socket(IP,8000);
		  System.out.println("连接远程服务器成功");
	      DataOutputStream dos = new DataOutputStream(s.getOutputStream());
	      //将客户端与服务器端链接的输出流交个ImageThread处理
	      ImageThread imageThread = new ImageThread(dos);
	      new Thread(imageThread).start();
	  }catch(Exception e) {
		  e.printStackTrace();
	  }
  }
}





