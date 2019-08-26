package file;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import java.awt.event.*;;

public class sendFile extends Thread implements ActionListener{
	private ServerSocket  connectSocket;
	private Socket socket;
	private DataOutputStream dos;
	private DataInputStream dis;
	private RandomAccessFile rad;
	private Container contentPanel;
    private JFrame frame;
    private JProgressBar progressbar;
    private JLabel label;
    String path;
    String IP;
	
	public sendFile(String path,String IP) {
		this.path = path;
		this.IP = IP;
		System.out.println(this.path);
		try {
			socket = new Socket(IP, 9998);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		frame=new JFrame("文件传输");
	}
	
	public void run(){
		//JFileChooser fc = new JFileChooser();	
		//int status=fc.showOpenDialog(null);
		//if (status==JFileChooser.APPROVE_OPTION) {
		if (true) {
			//String path=fc.getSelectedFile().getPath();
			System.out.println("下载的文件是: "+this.path);
			try {
				dos=new DataOutputStream(socket.getOutputStream());
				dis=new DataInputStream(socket.getInputStream());
				dos.writeUTF("ok");//发送OK
				rad=new RandomAccessFile(this.path, "r");
				File file=new File(this.path);
				
				byte[] buf=new byte[1024];
				dos.writeUTF(file.getName());//发送文件名称
				dos.flush();//清空缓冲区的数据流
				String rsp=dis.readUTF();//接收OK
				
				if (rsp.equals("ok")) {
					long size=dis.readLong();//读取文件已发送的大小
					dos.writeLong(rad.length());//发送文件的总长度
					dos.writeUTF("ok");//发送OK
					dos.flush();
					
					long offset=size;//字节偏移量
					int barSize=(int) (rad.length()/1024);//文件的总量
					int barOffset=(int)(offset/1024);//上次发送到的位置偏移量
					
					//传输界面
					frame.setSize(380,120);
					frame.setLocationRelativeTo(null);
					contentPanel = frame.getContentPane();
					contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
					progressbar = new JProgressBar();//进度条
					
					//label=new JLabel(file.getName()+" 发送中");
					//contentPanel.add(label);
					
					progressbar.setOrientation(JProgressBar.HORIZONTAL);
					progressbar.setMinimum(0);
					progressbar.setMaximum(barSize);
					progressbar.setValue(barOffset);
				    progressbar.setStringPainted(true);
				    progressbar.setPreferredSize(new Dimension(150, 20));
				    progressbar.setBorderPainted(true);
				    progressbar.setBackground(Color.pink);
				    
				    JButton cancel=new JButton("取消");
				    JPanel barPanel=new JPanel();
				    barPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
				    barPanel.add(progressbar);
				    barPanel.add(cancel);
				    contentPanel.add(barPanel);			    
				    cancel.addActionListener(this);
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setVisible(false);
					
					//从文件指定位置开始传输
					int length;
					if (offset<rad.length()) {
						rad.seek(offset);//跳到size的位置
						while((length=rad.read(buf))>0){
							dos.write(buf,0,length);							
							progressbar.setValue(++barOffset);
							dos.flush();
						}
					}
					//label.setText(file.getName()+" 发送完成");
				}
				dis.close();
				dos.close();
				rad.close();
				frame.dispose();
			}
			catch(IOException e) {
				e.printStackTrace();
				//label.setText(" 取消发送,连接关闭");
				frame.dispose();
			}

			
		}
		
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		try {
			label.setText(" 取消发送,连接关闭");
			JOptionPane.showMessageDialog(frame, "取消发送给，连接关闭!", "提示：", JOptionPane.INFORMATION_MESSAGE);				
			dis.close();
			dos.close();
			rad.close();
			frame.dispose();
		} 
		catch (IOException e1) {
			e1.printStackTrace();
			}
	}

}
