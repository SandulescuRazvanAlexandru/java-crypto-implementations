package ro.ase.ism.sap.crypto;

import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;

public class Util {
	public static String getHex(byte[] array) {
		String output = "";
		for(byte value : array) {
			output += String.format("%02x", value);
		}
		return output;
	}
	
	

	public static byte[] getRandomBytes(int noBytes, byte[] seed) throws NoSuchAlgorithmException {
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		if (seed != null) {
			secureRandom.setSeed(seed);
		}
		byte[] randomBytes = new byte[noBytes];
		secureRandom.nextBytes(randomBytes);

		return randomBytes;
	}
}
