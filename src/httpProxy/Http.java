package httpProxy;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Http extends Thread{
	protected final static String POST_TAG = "POST";
	protected Socket client;
	public Http(Socket _client) {
		this.client = _client;
		start();
	}
	protected ArrayList<String> request(){
		InputStream clientIn;
		ArrayList<String> list = new ArrayList<String>();
		try {
			clientIn = client.getInputStream();
			int t;
			StringBuilder line = new StringBuilder();
			do{
				line.setLength(0);
				while(((t=clientIn.read())!='\r' || (t=clientIn.read())!='\n') && t>0){ //13\r 10\n
					//TODO �ͻ��˿��ܲ��������ݣ��򲻹淶������
					line.append((char)t);
				}
				//System.out.println(line);
				list.add(line.toString());	//main function
			}while(line.length()>0); 
		}catch(IOException e) {
			
		}
		return list;
	}
}
