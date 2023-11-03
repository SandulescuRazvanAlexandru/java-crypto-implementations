package ro.ase.ism.sap.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class TestAsymmetric {

	public static void main(String[] args) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, SignatureException {
		
		//loading the keystore and printing the content
		KeyStore keyStore = KeyStoreManager.loadKeyStore(
				"ismkeystore.ks", "passks", "pkcs12");
		KeyStoreManager.listKeyStoreContent(keyStore);
		
		//loading the public key from a X509 certificate
		PublicKey pubKeyStudentIsm = KeyStoreManager.getCertificateKey("ISM_Student.cer");
		System.out.println("student.ism.ase.ro public key: " + 
				Util.getHex(pubKeyStudentIsm.getEncoded()));
		
		PublicKey pubKeyIsmKey1 = KeyStoreManager.getCertificateKey("ISMCertificateX509.cer");
		System.out.println("ismKey1 public key: ");
		System.out.println(Util.getHex(pubKeyIsmKey1.getEncoded()));
		
		if(Arrays.equals(pubKeyIsmKey1.getEncoded(), pubKeyStudentIsm.getEncoded())) {
			System.out.println("They are the same");
		}
		else {
			System.out.println("They are different");
		}
		
		//loading the keys from the keystore
		PublicKey pubKeyIsmKey1FromStore = KeyStoreManager.getPublicKey(keyStore, "ismkey1");
		
		System.out.println("ISMKey1 public key from the keystore:");
		System.out.println(Util.getHex(pubKeyIsmKey1FromStore.getEncoded()));
		
		if(Arrays.equals(pubKeyIsmKey1.getEncoded(), pubKeyIsmKey1FromStore.getEncoded())) {
			System.out.println("They are the same");
		}
		else {
			System.out.println("They are different");
		}
		
		
		//for pkcs12 key stores we use the same keystore password
		//for jks - proprietary we can use different passwords for aliases
		PrivateKey privKeyIsmKey1 = KeyStoreManager.getPrivateKey(keyStore, "ismkey1", "passks");
		System.out.println("ISMKey1 private key is: ");
		System.out.println(Util.getHex(privKeyIsmKey1.getEncoded()));
		
		System.out.println("Done");
		
		//encrypt and decrypt with RSA
		byte[] AESSessionKey = KeyStoreManager.generateSessionKey("AES", 128);
		System.out.println("AES Random Session Key:");
		System.out.println(Util.getHex(AESSessionKey));
		
		byte[] encryptedAESKey = KeyStoreManager.encryptRSA(pubKeyIsmKey1, AESSessionKey);
		
		System.out.println("Encrypted AES key is ");
		System.out.println(Util.getHex(encryptedAESKey));
		
		byte[] decryptedAESKey = KeyStoreManager.decryptRSA(privKeyIsmKey1, encryptedAESKey);
		
		System.out.println("Decrypted AES key:");
		System.out.println(Util.getHex(decryptedAESKey));
		
		//digital signature
		byte[] digitalSignature = 
				KeyStoreManager.getDigitalSignature("Message.txt", privKeyIsmKey1);
		System.out.println("The file digital signature is: ");
		System.out.println(Util.getHex(digitalSignature));
		
//		File dsFile = new File("MessageSignature.ds");
//		if(!dsFile.exists()) {
//			dsFile.createNewFile();
//		}
//		FileOutputStream fos = new FileOutputStream(dsFile);
//		fos.write(digitalSignature);
//		fos.close();
		
		//get the signature from the file
		File dsFile = new File("MessageSignature.ds");
		FileInputStream fis = new FileInputStream(dsFile);
		byte[] dsFromFile = fis.readAllBytes();
		fis.close();
		
		if(KeyStoreManager.hasValidSignature(
				"Message.txt", pubKeyIsmKey1FromStore, dsFromFile)) {
			System.out.println("The file is valid");
		}
		else {
			System.out.println("Vader changed the file");
		}
		
		
	}

}
