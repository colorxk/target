package httpProxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

//TODO https://,HTTPЭ��
//TODO �ͻ��˺�Ŀ��������������ķ�ʽ�ж����ݴ���
//CARE Keep-Alive���ӣ�Ҳ����Socket�Ϸ�http����
//CARE:Scoket����Ϊ��ʱ�����ӱ���
public class HttpProxy extends Http {
	private final static int TIME_OUT = 10000;
	private static Integer failedCount = new Integer(0);
	private static Integer succeedCount = new Integer(0);
	private static Integer requestCount = new Integer(0);
	private static PrintStream log = System.out;
	
	private String method;
	private String domain;
	private int port;
	private ArrayList<String> list;
	public HttpProxy(Socket _client){
		super(_client);
	}
	
	public void run(){
		ArrayList<String> list = request();
		if(list.size()<=1)
			return ;
		synchronized(requestCount){ requestCount++; }
		String requestLine = list.get(0);
		//System.out.println(requestLine+"\r\n");
		//0method,1http,2domain,3port,4path,5version
		String splits[] = getFirst(requestLine);
		//for(String s:splits)
			//System.out.println(s);
		list.set(0, splits[0]+" "+splits[4]+" "+splits[5]);	//CARE
		list.set(list.size()-1, "Connection: Close");
		list.add("");

		//for(String s:list)
			//System.out.println(s);
		this.method = splits[0];
		this.domain = splits[2];
		this.port = Integer.parseInt(splits[3]);
		this.list = list;
		boolean isSucceed = (!"".equals(splits[2]))	//CARE:String.equals
			&& response();
		if(isSucceed)
			synchronized(succeedCount){ succeedCount++; }
		else
			synchronized(failedCount){ failedCount++; }
		String info;
		info  = client.getInetAddress().toString().substring(1)+"\t";
		info += new Date()+"\t";
		info += succeedCount+"/"+failedCount+"/"+requestCount+"\t";
		info += isSucceed+":"+requestLine;
		log.println(info);
		close();
	}
	
	private boolean response(){
		try {
			//��Ŀ����վ��������
			Socket server = new Socket(domain,port);	
			server.setSoTimeout(TIME_OUT);
			PrintWriter w=new PrintWriter(server.getOutputStream(),true);
			for(String s:list)	//��list��request���ݰ�ת����Ŀ���������
				w.println(s);	
			//TODO w.print(s+"\r\n");
			if(Http.POST_TAG.equals(this.method)){
				try {
					byte[] buffer =new byte[1024];
					int n = client.getInputStream().read(buffer);
					w.println(new String(buffer,0,n));
				} catch (Exception e) {
					//e.printStackTrace();
				}
			}
			w.flush();
			
			//��Ŀ����վ���ص�������ת���ͻ���
			int n;
			byte[] buffer = new byte[1024];
			InputStream serverIn = server.getInputStream();
			OutputStream clientOut = client.getOutputStream();
			while((n=serverIn.read(buffer))!=-1){
				clientOut.write(buffer, 0, n);
			}
			server.close();
			return true;
		} catch (IOException e) {
			//System.out.println("Exception:"+list.get(0));
			//e.printStackTrace();
			return false;
		}	
	}

	//method,http,domain,port,path,version
	private String[] getFirst(String requestLine){
		String[] splits = new String[6];
		int i,j,k;
		i = requestLine.indexOf(" ");
		splits[0] = requestLine.substring(0, i);//��ȡmethod
		j = requestLine.indexOf(" ",++i);
		splits[5] = requestLine.substring(j+1);//��ȡversion
		String url = requestLine.substring(i, j);
		String[] dp =getdp(url);
		splits[1] = dp[0];
		k = dp[1].lastIndexOf(':');	//debugged:i can't be rewrite
		if(k>0){
			splits[2] = dp[1].substring(0, k);
			splits[3] = dp[1].substring(k+1);
		}
		else{
			splits[2] = dp[1];	
			splits[3] = "80";	//Ĭ�϶˿�80
		}
		splits[4] = dp[2];
		return splits;
	}
	
	//dp[0]=http , dp[1]=domain,dp[2]=path;
	public static String[] getdp(String url){
		int i=0,j=0;
		String[] dp = new String[3];
		while(i<url.length() && url.charAt(i)!='/')i++;
		if(i==url.length()){
			dp[0] = "";
			dp[1] = url;
			dp[2] = "/";
		}
		if(i>0 && url.charAt(i-1)==':' && i+1<url.length() && url.charAt(i+1)=='/'){
			dp[0] = url.substring(0,i-1);//��ȡhttp
			j = url.indexOf('/',i+=2);
			if(j>0){
				dp[1] = url.substring(i, j);//��ȡdomain
				dp[2] = url.substring(j);//��ȡpath
			}
			else{
				dp[1] = url.substring(i);
				dp[2] = "/";
			}
		}
		else{
			dp[0] = "";
			dp[1] = url.substring(0, i);
			dp[2] = url.substring(i);
		}
		return dp;
	}
	
	private void close(){
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
