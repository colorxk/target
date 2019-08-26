
package common;

public class Message implements java.io.Serializable{
	//信息类型
	private String mesType;
	//发送者
	private String sender;
	//信息内容
	//private String content;
	private byte[] content;
	//发送时间
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
