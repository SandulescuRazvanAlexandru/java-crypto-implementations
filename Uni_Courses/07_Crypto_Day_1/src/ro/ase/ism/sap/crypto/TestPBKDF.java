package ro.ase.ism.sap.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class TestPBKDF {
	
	public static byte[] getPBKDFValue(
			String password, byte[] salt, int noIterations, int outputSize, String algorithm) 
					throws NoSuchAlgorithmException, InvalidKeySpecException {
		
		byte[] result = null;
		
		PBEKeySpec key = new PBEKeySpec(password.toCharArray(), salt, noIterations, outputSize);
		SecretKeyFactory secretFactory = SecretKeyFactory.getInstance(algorithm);
		result = secretFactory.generateSecret(key).getEncoded();
		
		return result;
	}

	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
		
		Util.loadBCProvider();
		
		//benchmarking the speed of a SHA1 hash
		String pass = "12345678";
		long tStart = System.currentTimeMillis();
		//byte[] hash = TestMessageDigest.getStringHash(pass, "SHA-1", "BC");
		
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		byte[] hash = md.digest(pass.getBytes());
		
		long tFinal = System.currentTimeMillis();
		
		System.out.println("Hash obtained in (ms): " + (tFinal - tStart));
		System.out.println("Hash value is " + Util.getHex(hash));
		
		//benchmarking the PBKDF solution
		byte[] salt = "ism_".getBytes();
		
		tStart = System.currentTimeMillis();
		
		byte[] pbkdfHash = getPBKDFValue(pass, salt, 20000, 160, "PBKDF2WithHmacSHA1");
		
		tFinal = System.currentTimeMillis();
		
		System.out.println("New hash obtained in (ms): " + (tFinal - tStart));
		System.out.println("Hash value is " + Util.getHex(pbkdfHash));
	}

}
