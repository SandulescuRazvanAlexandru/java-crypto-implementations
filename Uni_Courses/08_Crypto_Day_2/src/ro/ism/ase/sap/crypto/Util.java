package ro.ism.ase.sap.crypto;

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
	
	public static boolean checkProvider(String providerName) {
		Provider provider = Security.getProvider(providerName);
		if(provider != null) {
			return true;
		}
		else{
			return false;
		}
	}
	
	public static void loadBCProvider() {
		Provider provider = Security.getProvider("BC");
		if(provider == null) {
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		}
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
