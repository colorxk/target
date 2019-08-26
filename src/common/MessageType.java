package common;

public interface MessageType {
	String message_all_End ="0";
	String message_tcp="1";
	String message_http="2";
	String message_comm="3";
	String message_shellcode="4";
	String message_upFile="5";
	String message_downFile="6";
	String message_remote="7";
	String message_End_remote="8";
	String message_start_proxy="9";
	String message_End_proxy="10";
	String message_diskList_fileManager = "11";
	String message_directoryList_fileManager = "12";
	String message_respondDiskList_fileManager = "13";
	String message_respondDirectoryList_fileManager = "14";
}
