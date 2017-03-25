
public class Util {
	public static byte[] range (byte[] src, int init, int end){
		byte[] buff = new byte[end - init];
	     
	    for (int i = init; i < end; i++){
	      buff[i-init] = src[i];
	    }
	    return buff;
	}
	
	public static boolean compareArrays(byte [] a1, byte[] a2){
		if(a1 == null || a2 == null) return false;
		if(a1.length != a2.length) return false;
		for(int i=0;i<a1.length;i++){
			if(a1[i]!=a2[i]) return false;
		}
		return true;
	}
}
