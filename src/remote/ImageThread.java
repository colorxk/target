package remote;
//��Ļ��ȡ���ͷ�������������Ҫ�õ�socket��out��
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.sun.image.codec.jpeg.*;
/**
* ������ͼƬ���ݷ���
* @author ��Ԫ
*
*/
public class ImageThread implements Runnable{
  DataOutputStream dos = null;    //���������
  public static boolean b = true;
  
  public ImageThread(DataOutputStream dos){
      this.dos = dos;
      b = true;
  }
  public ImageThread() {
	  
  }
  
  @Override
  public void run() {
      try {
          Robot robot = new Robot();
          //��ȡ������Ļ
          Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
          /*
          int width = (int)dimension.getWidth();
          int height = (int)dimension.getHeight();
          Rectangle rec = new Rectangle(0,0,width,height);
          */
          Rectangle rec = new Rectangle(dimension);
          BufferedImage image;
          byte imageBytes[];
          while(b){
              image = robot.createScreenCapture(rec);
              imageBytes = getImageBytes(image);
              dos.writeInt(imageBytes.length);
              dos.write(imageBytes);
              dos.flush();
              Thread.sleep(50);   //�߳�˯��
          }

      } catch (AWTException e) {
          e.printStackTrace();
          System.exit(0);
      } catch (ImageFormatException e) {
          e.printStackTrace();
          System.exit(0);
      } catch (IOException e) {
          e.printStackTrace();
          System.exit(0);
      } catch (InterruptedException e) {
          e.printStackTrace();
          System.exit(0);
      }finally{
          try {
              if(dos!= null)  dos.close();
          } catch (IOException e) {
              e.printStackTrace();
          }
      }
  }
  /**
   *  ѹ��ͼƬ
   * @param ��Ҫѹ����ͼƬ
   * @return ѹ�����byte����
   * @throws IOException 
   * @throws ImageFormatException 
   */
  public byte[] getImageBytes(BufferedImage image) throws ImageFormatException, IOException{
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      //ѹ����ѹ�������õ���ŵ�byte�������
      JPEGImageEncoder jpegd = JPEGCodec.createJPEGEncoder(baos);
      //��iamgeѹ��
      jpegd.encode(image);
      //ת����byte����
      return baos.toByteArray();  
  }

}
