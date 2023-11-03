package ro.ase.ism.sap.crypto;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class TestOTP {
	
	public static byte[] OTPEncrypt(String inputFileName, String outputFileName) 
			throws NoSuchAlgorithmException, IOException {
		byte[] randomKey = null;
		
		File inputFile = new File(inputFileName);
		if(!inputFile.exists()) {
			throw new FileNotFoundException();
		}
		
		randomKey = Util.getRandomBytes((int) inputFile.length(), null);
		
		//open the file for reading
		FileInputStream fis = new FileInputStream(inputFile);
		BufferedInputStream bis = new BufferedInputStream(fis);

		//open the output file for writing
		File outputFile = new File(outputFileName);
		if(!outputFile.exists()) {
			outputFile.createNewFile();
		}
		
		FileOutputStream fos = new FileOutputStream(outputFile);
		
		byte[] buffer = new byte[1];
		
		for(long i = 0; i < inputFile.length(); i++) {
			bis.read(buffer);
			buffer[0] = (byte) (buffer[0] ^ randomKey[(int) i]);
			fos.write(buffer);
		}
		
		fis.close();
		fos.close();
		
		
		return randomKey;
	}
	
	public static void OTPDecrypt(String inputFileName, String outputFileName, byte[] randomKey) 
			throws Exception {
		
		File inputFile = new File(inputFileName);
		if(!inputFile.exists()) {
			throw new FileNotFoundException();
		}
		
		if(randomKey.length < inputFile.length()) {
			throw new Exception("The key is too short");
		}
		
		//open the file for reading
		FileInputStream fis = new FileInputStream(inputFile);
		BufferedInputStream bis = new BufferedInputStream(fis);

		//open the output file for writing
		File outputFile = new File(outputFileName);
		if(!outputFile.exists()) {
			outputFile.createNewFile();
		}
		
		FileOutputStream fos = new FileOutputStream(outputFile);
		
		byte[] buffer = new byte[1];
		
		for(long i = 0; i < inputFile.length(); i++) {
			bis.read(buffer);
			buffer[0] = (byte) (buffer[0] ^ randomKey[(int) i]);
			fos.write(buffer);
		}
		
		fis.close();
		fos.close();
		
	}

	public static void main(String[] args) throws Exception {
		
		byte[] otpKey = OTPEncrypt("Message.txt", "Message.otp");
		OTPDecrypt("Message.otp", "MessageClone.txt", otpKey);
		
		System.out.println("Done !");
		
	}

}
