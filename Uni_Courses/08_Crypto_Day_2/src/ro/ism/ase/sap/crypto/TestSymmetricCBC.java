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

public class TestSymmetricCBC {

	//CBC implementation with a random IV placed at the beginning of the file
	public static void encryptCBC(
			String plaintextFileName,
			String password,
			String ciphertextFileName,
			String algorithm
			) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		
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
		
		Cipher cipher = Cipher.getInstance(algorithm + "/CBC/PKCS5Padding");
		SecretKeySpec key = new SecretKeySpec(password.getBytes(), algorithm);
		
		byte[] IV = Util.getRandomBytes(cipher.getBlockSize(), null);
		
		IvParameterSpec ivSpec = new IvParameterSpec(IV);
		cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
		
		//write the IV value in the output file
		fos.write(IV);
		
		byte[] buffer = new byte[cipher.getBlockSize()];
		int noInputBytes = 0;
		byte[] outputBuffer;
		
		while((noInputBytes = bis.read(buffer)) > -1) {
			outputBuffer = cipher.update(buffer, 0, noInputBytes);
			fos.write(outputBuffer);
		}
		
		outputBuffer = cipher.doFinal();
		fos.write(outputBuffer);
		
		fis.close();
		fos.close();
	}
	
	public static void decryptCBC(
			String ciphertextInputFile, 
			String password,
			String plaintextOutputFile,
			String algorithm
			) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		
		File inputFile  = new File(ciphertextInputFile);
		if(!inputFile.exists()) {
			System.out.println("No input file available");
			throw new FileNotFoundException();
		}
		FileInputStream fis = new FileInputStream(inputFile);
		BufferedInputStream bis = new BufferedInputStream(fis);
		
		File outputFile = new File(plaintextOutputFile);
		if(!outputFile.exists()) {
			outputFile.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(outputFile);
		
		
		Cipher cipher = Cipher.getInstance(algorithm + "/CBC/PKCS5Padding");
		
		int blockSize = cipher.getBlockSize();
		
		//read the IV from the file
		byte[] IV = new byte[blockSize];
		bis.read(IV);
		
		SecretKeySpec key = new SecretKeySpec(password.getBytes(), algorithm);
		IvParameterSpec ivSpec = new IvParameterSpec(IV);
		
		cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
		
		byte[] buffer = new byte[blockSize];
		byte[] outputBuffer;
		int noInputBytes = 0;
		while((noInputBytes = bis.read(buffer)) != -1) {
			outputBuffer = cipher.update(buffer,0,noInputBytes);
			fos.write(outputBuffer);
		}
		outputBuffer = cipher.doFinal();
		fos.write(outputBuffer);
		
		fos.close();
		fis.close();
		
	}
	
	public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException {
		
		TestSymmetricCBC.encryptCBC(
				"Message.txt", "passwordpassword", "MessageCBC.enc", "AES");
		TestSymmetricCBC.decryptCBC(
				"MessageCBC.enc", "passwordpassword", "Message2.txt", "AES");
		
		System.out.println("Done");
	}

}
