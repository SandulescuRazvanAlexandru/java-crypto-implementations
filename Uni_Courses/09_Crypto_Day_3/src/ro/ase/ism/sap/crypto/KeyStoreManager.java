package ro.ase.ism.sap.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;

public class KeyStoreManager {

	public static KeyStore loadKeyStore(
			String keystoreFileName, String keystorePass, String keystoreType) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		File file = new File(keystoreFileName);
		if(!file.exists()) {
			System.out.println("Missing keystore file");
			throw new FileNotFoundException();
		}
		
		FileInputStream fis = new FileInputStream(file);
		KeyStore ks = KeyStore.getInstance(keystoreType);
		ks.load(fis, keystorePass.toCharArray());
		
		fis.close();
		
		return ks;
	}
	
	public static void listKeyStoreContent(KeyStore keyStore) throws KeyStoreException {
		System.out.println("Keystore content:");
		
		Enumeration<String> aliases = keyStore.aliases();
		while(aliases.hasMoreElements()) {
			String alias = aliases.nextElement();
			System.out.println("Alias: " + alias);
			if(keyStore.isCertificateEntry(alias)) {
				System.out.println("It's a certificate - public key");
			}
			if(keyStore.isKeyEntry(alias)) {
				System.out.println("It's a private-public pair of keys");
			}
		}
	}
	
	public static PublicKey getCertificateKey(String certificateFile) throws CertificateException, IOException {
		
		File file = new File(certificateFile);
		if(!file.exists()) {
			System.out.println("No certificate file available");
			throw new FileNotFoundException();
		}
		FileInputStream fis = new FileInputStream(file);
		
		CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
		X509Certificate certificate = (X509Certificate) certFactory.generateCertificate(fis);
		
		fis.close();
		return certificate.getPublicKey();
	}
	
	public static PrivateKey getPrivateKey(
			KeyStore keyStore, String aliasName, String aliasPass) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException {
		
		if(keyStore == null) {
			System.out.println("KeyStore not loaded");
			throw new UnsupportedOperationException();
		}
		
		if(keyStore.containsAlias(aliasName)) {
			return (PrivateKey) keyStore.getKey(aliasName, aliasPass.toCharArray());
		}
		else {
			System.out.println("Alias not present");
			throw new UnsupportedOperationException();
		}
	}
	
	public static PublicKey getPublicKey(
			KeyStore keyStore, String aliasName) throws KeyStoreException {
		if(keyStore == null) {
			System.out.println("KeyStore not loaded");
			throw new UnsupportedOperationException();
		}
		
		if(keyStore.containsAlias(aliasName)) {
			return keyStore.getCertificate(aliasName).getPublicKey();
		}
		else {
			System.out.println("Alias not present");
			throw new UnsupportedOperationException();
		}
	}
	
	
	public static byte[] encryptRSA(Key key, byte[] input) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(input);
	}
	
	public static byte[] decryptRSA(Key key, byte[] input) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, key);
		return cipher.doFinal(input);
	}
	
	public static byte[] generateSessionKey(String algorithm, int keySize) throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
		keyGenerator.init(keySize);
		return keyGenerator.generateKey().getEncoded();
	}
	
	public static byte[] getDigitalSignature(String fileName, PrivateKey key) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		File file  = new File(fileName);
		if(!file.exists()) {
			System.out.println("File not present");
			throw new FileNotFoundException();
		}
		FileInputStream fis = new FileInputStream(file);
		
		//read the entire file - only if we have enough RAM
		byte[] fileContent = fis.readAllBytes();
		
		fis.close();
		
		Signature signature  = Signature.getInstance("SHA1withRSA");
		signature.initSign(key);
		
		signature.update(fileContent);
		return signature.sign();
	}
	
	public static boolean hasValidSignature(String filename, PublicKey key, byte[] digitalSignature) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		File file = new File(filename);
		if(!file.exists()) {
			System.out.println("File missing");
			throw new FileNotFoundException();
		}
		FileInputStream fis = new FileInputStream(file);
		byte[] fileContent = fis.readAllBytes();
		
		fis.close();
		
		Signature signature = Signature.getInstance("SHA1withRSA");
		signature.initVerify(key);
		
		signature.update(fileContent);
		return signature.verify(digitalSignature);
		
	}
	
	
}
