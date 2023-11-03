package ro.ase.ism.sap.crypto;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class TestMessageDigest {
	
	public static byte[] getStringHash(String value, String algorithm, String provider) 
			throws NoSuchAlgorithmException, NoSuchProviderException {
		byte[] hashValue = null;
		
		MessageDigest messageDigest = null;
		
		if(Util.checkProvider(provider)) {
			System.out.println("Using BouncyCastle");
			messageDigest = MessageDigest.getInstance(algorithm, provider);
		}
		else {
			System.out.println("Using default provider");
			messageDigest = MessageDigest.getInstance(algorithm);
		}
		
		hashValue = messageDigest.digest(value.getBytes());
		
		return hashValue;
	}
	
	//not recommended because is slow
	public static byte[] getFileHash(String fileName, String algorithm, String provider) 
			throws NoSuchAlgorithmException, NoSuchProviderException, IOException {
		
		byte[] hashValue = null;
		
		MessageDigest messageDigest = null;
		
		if(Util.checkProvider(provider)) {
			System.out.println("Using BouncyCastle");
			messageDigest = MessageDigest.getInstance(algorithm, provider);
		}
		else {
			System.out.println("Using default provider");
			messageDigest = MessageDigest.getInstance(algorithm);
		}
		
		File file = new File(fileName);
		if(!file.exists()) {
			//throw new FileNotFoundException();
			System.out.println("***** File is not there");
			return null;
		}
		
		FileInputStream fis = new FileInputStream(file);
		byte[] buffer = new byte[1];
		
		int noBytesFromFile = fis.read(buffer);
		while(noBytesFromFile != -1) {
			messageDigest.update(buffer);
			noBytesFromFile = fis.read(buffer);
		}
		
		fis.close();
		hashValue = messageDigest.digest();
		
		return hashValue;
	}
	
	public static byte[] getFileHashInChunks(String fileName, String algorithm, String provider) 
			throws NoSuchAlgorithmException, NoSuchProviderException, IOException {
		
		byte[] hashValue = null;
		
		MessageDigest messageDigest = null;
		
		if(Util.checkProvider(provider)) {
			System.out.println("Using BouncyCastle");
			messageDigest = MessageDigest.getInstance(algorithm, provider);
		}
		else {
			System.out.println("Using default provider");
			messageDigest = MessageDigest.getInstance(algorithm);
		}
		
		File file = new File(fileName);
		if(!file.exists()) {
			//throw new FileNotFoundException();
			System.out.println("***** File is not there");
			return null;
		}
		
		FileInputStream fis = new FileInputStream(file);
		byte[] buffer = new byte[10];
		
		int noBytesFromFile = fis.read(buffer);
		while(noBytesFromFile != -1) {
			messageDigest.update(buffer, 0, noBytesFromFile);
			noBytesFromFile = fis.read(buffer);
		}
		
		fis.close();
		hashValue = messageDigest.digest();
		
		return hashValue;
	}

	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, IOException {

		String password = "123456";
		byte[] passHash = getStringHash(password, "MD5", "BC");
		System.out.println("MD5 hash = " + Util.getHex(passHash));
		
		byte[] otherHash = "e10adc3949ba59abbe56e057f20f883e".getBytes();
		
		//they are always different
		if(passHash.equals(otherHash)) {
			System.out.println("Are the same");
		}
		else
		{
			System.out.println("They are different");
		}
		
		
		
		if(Util.getHex(passHash).equals("e10adc3949ba59abbe56e057f20f883e")) {
			System.out.println("Are the same");
		}
		else
		{
			System.out.println("They are different");
		}
		
		Util.loadBCProvider();
		
		byte[] sha1Pass = getStringHash(password, "SHA-1", "BC");
		System.out.println("SHA1 hash = " + Util.getHex(sha1Pass));
		
		byte[] fileHash = getFileHash("Message.txt", "SHA-1", "BC");
		System.out.println("File SHA1 value = " + Util.getHex(fileHash));
		
		fileHash = getFileHashInChunks("Message.txt", "SHA-1", "BC");
		System.out.println("File SHA1 value = " + Util.getHex(fileHash));
		
		//using a predefined salt as a string
		String predefinedSalt = "ism_";
		passHash = getStringHash(predefinedSalt + password, "MD5", "BC");
		System.out.println("MD5 hash with salt = " + Util.getHex(passHash));
		
		//using a random salt for each user
		//you need to store it in the database
		byte[] randomSalt = Util.getRandomBytes(10, null);
		byte[] passWordBytes = password.getBytes();
		
		ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
		byteArray.writeBytes(randomSalt);
		byteArray.writeBytes(passWordBytes);
		
		byte[] passWithSalt = byteArray.toByteArray();
		System.out.println("The final pass size is " + passWithSalt.length);
		
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] md5Hash = md.digest(passWithSalt);
		
		System.out.println("Pass with random salt hash is " + Util.getHex(md5Hash));
		
		
	}

}
