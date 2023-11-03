package ro.ase.ism.sap.crypto;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class TestHashMAC {
	
	public static byte[] getHashMAC(String inputFileName, byte[] secretKey, String algorithm) 
			throws IOException, NoSuchAlgorithmException, InvalidKeyException {
		byte[] hashMac = null;
		
		File file = new File(inputFileName);
		if(!file.exists()) {
			throw new FileNotFoundException();
		}
		
		//init the Mac object
		Mac mac = Mac.getInstance(algorithm);
		mac.init(new SecretKeySpec(secretKey, algorithm));
		
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(fis);
		
		byte [] buffer = new byte[1024];
		int noBytesFromFile = bis.read(buffer);
		
		while(noBytesFromFile != -1) {
			mac.update(buffer, 0, noBytesFromFile);
			noBytesFromFile = bis.read(buffer);
		}
		
		hashMac = mac.doFinal();
		
		return hashMac;
	}

	public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, IOException, NoSuchProviderException {
		
		byte[] hmac = getHashMAC("Message.txt", "ismsecret".getBytes(), "HmacSHA1");
		
		System.out.println("HashMAC = " + Util.getHex(hmac));
		
		byte[] sha1Hash = TestMessageDigest.getFileHash("Message.txt", "SHA1", "BC");
		
		System.out.println("SHA1 = " + Util.getHex(sha1Hash));
		
		
	}

}
