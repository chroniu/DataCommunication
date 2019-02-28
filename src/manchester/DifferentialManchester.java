package manchester;

import java.io.UnsupportedEncodingException;

/**
 * 
 * @author Lucas
 */
public class DifferentialManchester {
	
	/**
	 * Transforms a String into an array of bytes, encoding the characters @charset
	 * @param str
	 * @return
	 */
	public static byte[] getByteRepresentation(String str, String charset) {
		try {
			return str.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Transforms an array of bytes into an ascii string
	 * @param codified
	 * @return
	 */
	public static String getStringFromByteArray(byte[] codified, String charset) {
		try {
			return new String(codified, charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static String toByteFormat(byte b){
		String ret = "";
		
		int mask = 128;////(((byte)128) & 0xFF);
		for(int i=0; i < 8; i++){
			boolean bx = ( ((b & 0xFF) & mask) != 0 ? true : false);
			mask = (mask >> 1);		
			ret += (bx? '1': '0');
		}
		
		return ret;
	}
	
	/**
	 * Transforms an array of bytes into a boolean array
	 * @param asciiRepresentation
	 * @return
	 */
	public static boolean[] byteArrayToBinaryArray(byte[] asciiRepresentation) {
		String binaryString = "";
		
		for (byte b : asciiRepresentation) { 
			binaryString += toByteFormat(b);
		}

		boolean[] binaryArray = new boolean[binaryString.length()];

		for (int i = 0; i < binaryString.length(); i++) {
			binaryArray[i] = (binaryString.charAt(i) == '1' ? true : false);
		}
		return binaryArray;
	}
	
	public static String booleanArrayToString(boolean [] vet){
		String byteRepresentation = "";
		for (boolean b : vet) {
			byteRepresentation+=(b?1:0);
		}
		return byteRepresentation;
		
	}
	
	/**
	 * Transforms a Boolean array into an array of bytes
	 * @param vet
	 * @return
	 */
	public static byte[] booleanArrayToByteArray(boolean[] vet) {
		byte []byteArray = new byte[vet.length/8];
		
		String byteRepresentation = booleanArrayToString(vet);
		
		for(int i=0; i <byteArray.length;i++){
			byteArray[i] = (byte) Integer.parseUnsignedInt(byteRepresentation.substring(i *8, i * 8 + 8), 2);
			
		}
		return byteArray;
	}	
	
	/**
	 * Encodes a binary array into a binary array with manchester differential encoding.
	 * @param binaryData
	 * @return
	 */
	public static boolean[] encodeToDifferentialManchester(boolean binaryData[]) {// byte[]
																					// asciiRepresentation)
																					// {
		// boolean[] binaryData = asciiToBinary(asciiRepresentation);
		boolean[] manchester = new boolean[binaryData.length * 2];

		if (binaryData[0]) {
			manchester[0] = true;
			manchester[1] = false;

		} else {
			manchester[0] = false;
			manchester[1] = true;

		}

		for (int i = 1; i < binaryData.length; i++) {
			if (binaryData[i]) {
				manchester[i * 2] = manchester[i * 2 - 1];
				manchester[i * 2 + 1] = !manchester[i * 2];
			} else {
				manchester[i * 2] = !manchester[i * 2 - 1];
				manchester[i * 2 + 1] = !manchester[i * 2];
			}
		}

		return manchester;
	}
	
	/**
	 * Decodes a binary array encoded in manchester differential.
	 * @param manchester binarry array
	 * @return
	 */
	public static boolean[] decodeFromDifferentialManchester(boolean[] manchester) {
		boolean[] binaryData = new boolean[manchester.length / 2];

		binaryData[0] = ((manchester[0] && !manchester[1]) ? true : false);

		for (int i = 1; i < manchester.length / 2; i++) {
			if ((manchester[i * 2] == manchester[i * 2 - 1]) && (manchester[i * 2 + 1] != manchester[i * 2])) {
				binaryData[i] = true;
			} else if ((manchester[i * 2] != manchester[i * 2 - 1]) && (manchester[i * 2 + 1] != manchester[i * 2])) {
				binaryData[i] = false;
			} else {
				System.out.println("Decoder Error at position: " + i);
				return null;
			}
		}

		return binaryData;
	}

	public static void main(String args[]) {
		byte[] ascciRepresentation = getByteRepresentation("abc","UTF-8");
		boolean[] binaryRepresentation = byteArrayToBinaryArray(ascciRepresentation);

		boolean[] testData = { false, false, false, true, true, true, false, true, false, true };
		boolean[] testExpectedResult = { false, true, false, true, false, true, true, false, false, true, true, false,
				true, false, false, true, false, true, true, false };
		boolean[] encodedData = encodeToDifferentialManchester(testData);
		boolean[] decodedData = decodeFromDifferentialManchester(encodedData);
		boolean[] asciiEncoded  = (encodeToDifferentialManchester(binaryRepresentation));
		byte[] asciiDecoded  = booleanArrayToByteArray(decodeFromDifferentialManchester(asciiEncoded));
		
		assert(testExpectedResult.equals(encodedData));
		assert(testData.equals(decodedData));
		assert(getStringFromByteArray(booleanArrayToByteArray(binaryRepresentation),"UTF-8").equals("abc"));
		

		System.out.println("Testes Sucesso");

	}

}
