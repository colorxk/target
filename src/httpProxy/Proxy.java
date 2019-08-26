package httpProxy;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class Proxy {
	public static boolean b = true;
	int port = 808;
	Thread proxyt;
	public void startProxy() {
		this.b = true;
		proxyt =new Thread(new Runnable(){
			public void run(){ 
				try{
					ServerSocket proxyServer = getServer(port);
					String info = "PROXY SERVER START: ";
					info += "IP= "+InetAddress.getLocalHost();
					info += "\tPORT= "+proxyServer.getLocalPort();
					System.out.println(info);
					while(b) {
						new HttpProxy(proxyServer.accept());
						System.out.println("开启中...");
						Thread.sleep(500);
					}
					proxyServer.close();
					System.out.println("代理结束");
				}catch(Exception ex){ 
					ex.printStackTrace();
				}
			}
		});
		proxyt.start();
	}
	
	public void endProxy() {
		this.b = false;
		//proxyt.stop();
	}
	
	
/*	public static void main(String[] args) {
		Proxy p =new Proxy();
		p.startProxy();
	}*/
	
	public static ServerSocket getServer(int port){
		ServerSocket server = null;
		int c = 0;
		while(server==null && ++c<100){
			try {
				server = new ServerSocket(port);
				return server;
			} catch (IOException e) {
				port += 3*c + 1;
				//e.printStackTrace();
			}
		}
		return null;
	}
}
