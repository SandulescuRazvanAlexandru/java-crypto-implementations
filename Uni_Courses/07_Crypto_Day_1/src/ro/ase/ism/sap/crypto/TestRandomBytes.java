package ro.ase.ism.sap.crypto;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

public class TestRandomBytes {

	public static byte[] getRandomBytes(int noBytes, byte[] seed) throws NoSuchAlgorithmException {
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		if (seed != null) {
			secureRandom.setSeed(seed);
		}
		byte[] randomBytes = new byte[noBytes];
		secureRandom.nextBytes(randomBytes);

		return randomBytes;
	}

	public static byte[] getRandomBytesWithAnyProvider(
			int noBytes, byte[] seed, String providerName, String algorithm)
			throws NoSuchAlgorithmException, NoSuchProviderException {
		
		SecureRandom secureRandom = SecureRandom.getInstance(algorithm, providerName);
		if (seed != null) {
			secureRandom.setSeed(seed);
		}
		byte[] randomBytes = new byte[noBytes];
		secureRandom.nextBytes(randomBytes);

		return randomBytes;
	}

	
	public static byte[] getRandomBytesWithProvider(
			int noBytes, byte[] seed, String providerName, String algorithm)
			throws NoSuchAlgorithmException, NoSuchProviderException {

		SecureRandom secureRandom = null;
		
		if(Util.checkProvider(providerName)) {
			secureRandom = SecureRandom.getInstance(algorithm, providerName);
		}
		else {
			secureRandom = SecureRandom.getInstance("SHA1PRNG");
		}
		if (seed != null) {
			secureRandom.setSeed(seed);
		}
		byte[] randomBytes = new byte[noBytes];
		secureRandom.nextBytes(randomBytes);

		return randomBytes;
	}

	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException {

		// generate random bytes using a secure PRNG
		byte[] randomBytes = getRandomBytes(5, null);
		System.out.println("Random bytes:" + Util.getHex(randomBytes));

		byte[] randomBytesWithSeed = getRandomBytes(5, "1234".getBytes());
		System.out.println("Random bytes with seed:" + Util.getHex(randomBytesWithSeed));

		randomBytes = getRandomBytes(5, null);
		System.out.println("Random bytes:" + Util.getHex(randomBytes));

		randomBytesWithSeed = getRandomBytes(5, "1234".getBytes());
		System.out.println("Random bytes with seed:" + Util.getHex(randomBytesWithSeed));

		String BouncyCastleProvider = "BC";
		
		Util.loadBCProvider();
		
		try {
			randomBytes = getRandomBytesWithAnyProvider(5, null, BouncyCastleProvider, "SHA-1");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("SHA1PRNG is not available");
		} catch (NoSuchProviderException e) {
			System.out.println("Bouncy Castle provider is not available");
			// randomBytes = getRandomBytesWithAnyProvider(5, null, "SUN");
			randomBytes = getRandomBytes(5, null);
		}

		System.out.println("Random bytes:" + Util.getHex(randomBytes));
		
		try {
			randomBytes = getRandomBytesWithProvider(5, null, BouncyCastleProvider,"SHA-1");
			System.out.println("Random bytes:" + Util.getHex(randomBytes));
		}
		catch(NoSuchAlgorithmException ex) {
			System.out.println("No algorithm available");
		}

	}

}
