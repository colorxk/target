
package common;

public class Message implements java.io.Serializable{
	//��Ϣ����
	private String mesType;
	//������
	private String sender;
	//��Ϣ����
	//private String content;
	private byte[] content;
	//����ʱ��
	private String sendTime;
	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getMesType() {
		return mesType;
	}

	public void setMesType(String mesType) {
		this.mesType = mesType;
	}
}
