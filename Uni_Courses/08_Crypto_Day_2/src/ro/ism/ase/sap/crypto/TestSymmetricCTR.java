package ro.ism.ase.sap.crypto;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class TestSymmetricCTR {

	public static void encryptCTR(
			String plaintextFileName, 
			String password,
			String ciphertextFileName,
			String algorithm) throws IOException, 
			NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException
	{
		File inputFile = new File(plaintextFileName);
		if(!inputFile.exists()) {
			System.out.println("File is not present there");
			throw new FileNotFoundException();
		}
		
		FileInputStream fis = new FileInputStream(inputFile);
		BufferedInputStream bis = new BufferedInputStream(fis);
		
		File outputFile = new File(ciphertextFileName);
		if(!outputFile.exists()) {
			outputFile.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(outputFile);
		
		//1. Create the Cipher object
		Cipher cipher = Cipher.getInstance(algorithm + "/CTR/NoPadding");
		
		//2. Define the initial value for the counter
		byte[] IV = new byte[cipher.getBlockSize()];
		IV[cipher.getBlockSize()-1] = 0x01;		//0000 0001
		
		//2. Init the Cipher object
		SecretKeySpec key = new SecretKeySpec(password.getBytes(), algorithm);
		IvParameterSpec ivSpec = new IvParameterSpec(IV);
		cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
		
		//3. Encrypt the input
		int blockSize = cipher.getBlockSize();
		byte[] buffer = new byte[blockSize];
		int noInputBytes = 0;
		byte[] cipherBuffer;
		
		while( (noInputBytes = bis.read(buffer)) > -1) {
			cipherBuffer = cipher.update(buffer, 0, noInputBytes);
			fos.write(cipherBuffer);
		}
		
		//get the last block of ciphertext
		cipherBuffer = cipher.doFinal();
		fos.write(cipherBuffer);
		
		fis.close();
		fos.close();
		
	}
	
	public static void decryptCTR(
			String ciphertextFileName, 
			String password,
			String plaintextFileName,
			String algorithm
			) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		
		File inputFile  = new File(ciphertextFileName);
		if(!inputFile.exists()) {
			System.out.println("No input file available");
			throw new FileNotFoundException();
		}
		FileInputStream fis = new FileInputStream(inputFile);
		BufferedInputStream bis = new BufferedInputStream(fis);
		
		File outputFile = new File(plaintextFileName);
		if(!outputFile.exists()) {
			outputFile.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(outputFile);
		
		Cipher cipher = Cipher.getInstance(algorithm + "/CTR/NoPadding");
		
		byte[] initialCounter = new byte[cipher.getBlockSize()];
		initialCounter[cipher.getBlockSize()-1] = 0x01;
		
		SecretKeySpec key = new SecretKeySpec(password.getBytes(), algorithm);
		IvParameterSpec counterSpec = new IvParameterSpec(initialCounter);
		
		cipher.init(Cipher.DECRYPT_MODE, key, counterSpec);
		
		byte[] buffer = new byte[cipher.getBlockSize()];
		int noInputBytes = 0;
		byte[] outputBuffer;
		while((noInputBytes = bis.read(buffer)) > -1 ) {
			outputBuffer = cipher.update(buffer, 0, noInputBytes);
			fos.write(outputBuffer);
		}
		
		outputBuffer = cipher.doFinal();
		fos.write(outputBuffer);
		
		fis.close();
		fos.close();
		
	}
	
	public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, IOException {
		
		TestSymmetricCTR.encryptCTR("Message.txt", "password", "MessageCTR.enc", "DES");
		TestSymmetricCTR.decryptCTR("MessageCTR.enc", "password", "MessageCTR.txt", "DES");
		
		System.out.println("Done");
	}

}
