package ro.ism.ase.sap.crypto;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class TestPBE {

	public static byte[] pbeEncrypt(
			String inputFileName, 
			String password, 
			String outputFileName, 
			String algorithm,
			int noIterations, 
			byte[] salt) throws IOException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

		File inputFile = new File(inputFileName);
		if (!inputFile.exists()) {
			System.out.println("File is not present there");
			throw new FileNotFoundException();
		}

		FileInputStream fis = new FileInputStream(inputFile);
		BufferedInputStream bis = new BufferedInputStream(fis);

		File outputFile = new File(outputFileName);
		if (!outputFile.exists()) {
			outputFile.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(outputFile);

		Cipher cipher = Cipher.getInstance(algorithm);
		int blockSize = cipher.getBlockSize();

		PBEKeySpec pbeKey = new PBEKeySpec(password.toCharArray(), salt, noIterations, blockSize);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
		Key key = keyFactory.generateSecret(pbeKey);

		System.out.println("Initial password: " + password);
		System.out.println("Generated key (hex): " + Util.getHex(key.getEncoded()));

		cipher.init(Cipher.ENCRYPT_MODE, key);

		// 3. Encrypt the input
		byte[] buffer = new byte[blockSize];
		int noInputBytes = 0;
		byte[] cipherBuffer;

		while ((noInputBytes = bis.read(buffer)) > -1) {
			cipherBuffer = cipher.update(buffer, 0, noInputBytes);
			fos.write(cipherBuffer);
		}

		// get the last block of ciphertext
		cipherBuffer = cipher.doFinal();
		fos.write(cipherBuffer);

		fis.close();
		fos.close();
		
		return cipher.getParameters().getEncoded();

	}
	
	public static void pbeDecrypt(
			String inputFileName, 
			String password, 
			String outputFileName, 
			String algorithm,
			int noIterations, 
			byte[] salt,
			byte[] pbeParams) 
					throws IOException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {

		File inputFile = new File(inputFileName);
		if (!inputFile.exists()) {
			System.out.println("File is not present there");
			throw new FileNotFoundException();
		}

		FileInputStream fis = new FileInputStream(inputFile);
		BufferedInputStream bis = new BufferedInputStream(fis);

		File outputFile = new File(outputFileName);
		if (!outputFile.exists()) {
			outputFile.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(outputFile);

		Cipher cipher = Cipher.getInstance(algorithm);
		int blockSize = cipher.getBlockSize();

		PBEKeySpec pbeKey = new PBEKeySpec(password.toCharArray(), salt, noIterations, blockSize);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
		Key key = keyFactory.generateSecret(pbeKey);

		System.out.println("Initial password: " + password);
		System.out.println("Generated key (hex): " + Util.getHex(key.getEncoded()));

		AlgorithmParameters algParams = AlgorithmParameters.getInstance(algorithm);	
		algParams.init(pbeParams);
		
		cipher.init(Cipher.DECRYPT_MODE, key, algParams);

		// 3. Encrypt the input
		byte[] buffer = new byte[blockSize];
		int noInputBytes = 0;
		byte[] cipherBuffer;

		while ((noInputBytes = bis.read(buffer)) > -1) {
			cipherBuffer = cipher.update(buffer, 0, noInputBytes);
			fos.write(cipherBuffer);
		}

		// get the last block of ciphertext
		cipherBuffer = cipher.doFinal();
		fos.write(cipherBuffer);

		fis.close();
		fos.close();

	}
	
	public static void pbeEncryptWithProvider(
			String inputFileName, 
			String password, 
			String outputFileName, 
			String algorithm,
			int noIterations, 
			byte[] salt,
			String provider) throws IOException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException {

		File inputFile = new File(inputFileName);
		if (!inputFile.exists()) {
			System.out.println("File is not present there");
			throw new FileNotFoundException();
		}

		FileInputStream fis = new FileInputStream(inputFile);
		BufferedInputStream bis = new BufferedInputStream(fis);

		File outputFile = new File(outputFileName);
		if (!outputFile.exists()) {
			outputFile.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(outputFile);

		Cipher cipher = Cipher.getInstance(algorithm, provider);
		int blockSize = cipher.getBlockSize();

		PBEKeySpec pbeKey = new PBEKeySpec(password.toCharArray(), salt, noIterations, blockSize);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
		Key key = keyFactory.generateSecret(pbeKey);

		System.out.println("Initial password: " + password);
		System.out.println("Generated key (hex): " + Util.getHex(key.getEncoded()));

		cipher.init(Cipher.ENCRYPT_MODE, key);

		// 3. Encrypt the input
		byte[] buffer = new byte[blockSize];
		int noInputBytes = 0;
		byte[] cipherBuffer;

		while ((noInputBytes = bis.read(buffer)) > -1) {
			cipherBuffer = cipher.update(buffer, 0, noInputBytes);
			fos.write(cipherBuffer);
		}

		// get the last block of ciphertext
		cipherBuffer = cipher.doFinal();
		fos.write(cipherBuffer);

		fis.close();
		fos.close();

	}

	public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException, IOException, NoSuchProviderException, InvalidAlgorithmParameterException {
		byte[] salt = "ism".getBytes();
		byte[] pbeParams;
		pbeParams = TestPBE.pbeEncrypt(
				"Message.txt", "1234", "PBEMessage.enc", "PBEWithHmacSHA256AndAES_128", 
				10000, salt);
		
		
		TestPBE.pbeDecrypt("PBEMessage.enc", "1234", "MessagePBE.txt", "PBEWithHmacSHA256AndAES_128", 
				10000, salt, pbeParams);
		
		System.out.println("Done");
	}

}
