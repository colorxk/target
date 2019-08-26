package encryption;

public class AES {
	
	private static String AESPrivateKey = "00000000000000000";
	
	public static String getAESPrivateKey() {
		return AESPrivateKey;
	}
	
	public static void setAESPrivateKey(String key) {
		AESPrivateKey = key;
	}
	
}
