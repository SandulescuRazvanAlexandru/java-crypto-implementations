import javax.crypto.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Enumeration;

public class Main {

    public static String getHex(byte[] array) {
        String output = "";
        for (byte value : array) {
            output += String.format("%02x", value);
        }
        return output;
    }

    public static KeyStore loadKeyStore(String keystoreFileName, String keystorePass, String keystoreType) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        File file = new File(keystoreFileName);
        if(!file.exists()) {
            //throw new FileNotFoundException();
            System.out.println("Missing keystore file");
            return null;
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
            //throw new FileNotFoundException();
            System.out.println("No certificate file available");
            return null;
        }
        FileInputStream fis = new FileInputStream(file);

        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate) certFactory.generateCertificate(fis);

        fis.close();
        return certificate.getPublicKey();
    }

    //pentru cazul in care nu avem a certificate file for it
    public static PublicKey getPublicKey(KeyStore keyStore, String aliasName) throws KeyStoreException {
        if(keyStore == null) {
            System.out.println("KeyStore not loaded");
            //throw new UnsupportedOperationException();
            return null;
        }

        if(keyStore.containsAlias(aliasName)) {
            return keyStore.getCertificate(aliasName).getPublicKey();
        }
        else {
            System.out.println("Alias not present");
            //throw new UnsupportedOperationException();
            return null;
        }
    }

    public static PrivateKey getPrivateKey(KeyStore keyStore, String aliasName, String aliasPass) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException {
        if(keyStore == null) {
            System.out.println("KeyStore not loaded");
            //throw new UnsupportedOperationException();
            return null;
        }

        if(keyStore.containsAlias(aliasName)) {
            return (PrivateKey) keyStore.getKey(aliasName, aliasPass.toCharArray());
        }
        else {
            System.out.println("Alias not present");
            //throw new UnsupportedOperationException();
            return null;
        }
    }

    public static byte[] generateSessionKey(String algorithm, int keySize) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
        keyGenerator.init(keySize);
        return keyGenerator.generateKey().getEncoded();
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

    public static byte[] getDigitalSignature(String fileName, PrivateKey key) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        File file  = new File(fileName);
        if(!file.exists()) {
            //throw new FileNotFoundException();
            System.out.println("***** File is not there");
            return null;
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
            //throw new FileNotFoundException();
            System.out.println("***** File is not there");
            return false;
        }
        FileInputStream fis = new FileInputStream(file);
        byte[] fileContent = fis.readAllBytes();

        fis.close();

        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initVerify(key);

        signature.update(fileContent);
        return signature.verify(digitalSignature);
    }

    public static void main(String[] args) throws CertificateException, KeyStoreException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, SignatureException {

        //loading the keystore and printing the content
        KeyStore keyStore = loadKeyStore("ismkeystore.ks", "passks", "pkcs12");
        listKeyStoreContent(keyStore);

        //loading the public key from a X509 certificate
        PublicKey pubKeyIsmKey1 = getCertificateKey("ISMCertificateX509.cer");
        System.out.println("ismKey1 public key: ");
        System.out.println(getHex(pubKeyIsmKey1.getEncoded()));

        //loading the keys from the keystore
        PublicKey pubKeyIsmKey1FromStore = getPublicKey(keyStore, "ismkey1");
        System.out.println("ISMKey1 public key from the keystore:");
        System.out.println(getHex(pubKeyIsmKey1FromStore.getEncoded()));

        if(Arrays.equals(pubKeyIsmKey1.getEncoded(), pubKeyIsmKey1FromStore.getEncoded())) {
            System.out.println("They are the same");
        }
        else {
            System.out.println("They are different");
        }

        //for pkcs12 key stores we use the same keystore password
        //for jks - proprietary we can use different passwords for aliases
        PrivateKey privKeyIsmKey1 = getPrivateKey(keyStore, "ismkey1", "passks");
        System.out.println("ISMKey1 private key is: ");
        System.out.println(getHex(privKeyIsmKey1.getEncoded()));

        //===RSA===//

        //asta e input, aici poate sa fie orice, asa a decis el sa faca un exemplu
        //in loc de AESSessionKey, pot sa am Message.txt .getBytes() gen
        byte[] AESSessionKey = generateSessionKey("AES", 128);
        System.out.println("AES Random Session Key:");
        System.out.println(getHex(AESSessionKey));

        byte[] encryptedAESKey = encryptRSA(pubKeyIsmKey1, AESSessionKey);
        System.out.println("Encrypted AES key is ");
        System.out.println(getHex(encryptedAESKey));

        byte[] decryptedAESKey = decryptRSA(privKeyIsmKey1, encryptedAESKey);
        System.out.println("Decrypted AES key:");
        System.out.println(getHex(decryptedAESKey));


        //===DIGITAL SIGNATURE===//
        byte[] digitalSignature = getDigitalSignature("Message.txt", privKeyIsmKey1);
        System.out.println("The file digital signature is: ");
        System.out.println(getHex(digitalSignature));

        //store the digital signature in a binary file
		File dsFile = new File("MessageSignature.ds");
		if(!dsFile.exists()) {
			dsFile.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(dsFile);
		fos.write(digitalSignature);
		fos.close();

        //get the signature from the binary file
        //File dsFile = new File("MessageSignature.ds");
        FileInputStream fis = new FileInputStream(dsFile);
        byte[] dsFromFile = fis.readAllBytes();
        fis.close();

        //if the file is valid based on its digital signature
        if(hasValidSignature("Message.txt", pubKeyIsmKey1FromStore, dsFromFile)) {
            System.out.println("The file is valid");
        }
        else {
            System.out.println("Vader changed the file");
        }

        System.out.println("Done");
    }
}
