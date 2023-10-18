import javax.crypto.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.sql.SQLOutput;
import java.util.Enumeration;

public class KeyStoreManager {
//we load into memory the entire content of the keystore
    //keystore: only the public keys/ pairs: pub-priv
    //database for keys
    public static KeyStore loadKeyStore (String keystoreFileName, String keystorePassword,String keystoreType) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException {
        File file = new File(keystoreFileName);
        if (!file.exists()){
            System.out.println("Missing keystore file");
            throw new FileNotFoundException();
        }

        FileInputStream fis = new FileInputStream(file);
        KeyStore ks = KeyStore.getInstance(keystoreType);
        ks.load(fis, keystorePassword.toCharArray());

        fis.close();

        return ks;
    }

    //prints the input in the console
    public static void listKeyStoreContent(KeyStore keyStore) throws KeyStoreException {
        System.out.println("Keystore content: ");

        Enumeration<String> aliases = keyStore.aliases();
        while(aliases.hasMoreElements()){
            String alias = aliases.nextElement();
            System.out.println("Alias: " + alias);
            if(keyStore.isCertificateEntry(alias)){
                System.out.println("It's a certificate - public key");
            }
            if(keyStore.isKeyEntry(alias)){
                System.out.println("It's a private-public pair of keys");
            }
        }

    }


    public static String getHex(byte[] array)
    {
        String output = "";
        for(byte value : array)
        {
            output += String.format("%02x", value);
        }
        return output;
    }

    //get the keys from the certificate - public
    //no need pass <=> its public
    public static PublicKey getCertificateKey(String certificateFile) throws IOException, CertificateException {
        File file = new File(certificateFile);
        if(!file.exists()){
            System.out.println("No certificate file available");
            throw new FileNotFoundException();
        }
        FileInputStream fis = new FileInputStream(file);
        //open the certificate and extract the X509 certificate
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        //store the certificate (type X509)
        X509Certificate certificate = (X509Certificate) certFactory.generateCertificate(fis);
        //=> Certificate object

        fis.close();
        return certificate.getPublicKey();
    }

    public static PrivateKey getPrivateKey(KeyStore keyStore, String aliasName, String aliasPass) throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException {
        //we need access to the keystore
        //=> we need the ks pass

        if(keyStore == null){
            System.out.println("KeyStore not loaded");//we dont have it
            throw new UnsupportedOperationException();
        }

        //check if we have the alias
        if(keyStore.containsAlias(aliasName)){
            return (PrivateKey) keyStore.getKey(aliasName, aliasPass.toCharArray());
        }
        else{
            System.out.println("Alias not present");
            throw new UnsupportedOperationException();
        }

    }

    //get the public key from the key store; the prev was extracting it from the certificate
    public static PublicKey getPublicKey(KeyStore keyStore, String aliasName) throws KeyStoreException {
        if(keyStore == null){
            System.out.println("KeyStore not loaded");
            throw new UnsupportedOperationException();
        }
        if(keyStore.containsAlias(aliasName)){
            return keyStore.getCertificate(aliasName).getPublicKey();
        }
        else{
            System.out.println("Alias not present");
            throw new UnsupportedOperationException();
        }
    }

    public static byte[] encryptRSA(Key key, byte[] input) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        //=stream cipher
        //blocks -> update + doFinal
        //entire buffer at once -> doFinal
        return cipher.doFinal(input);
    }

    public static byte[] decryptRSA(Key key, byte[] input) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);
        //=stream cipher
        //blocks -> update + doFinal
        //entire buffer at once -> doFinal
        return cipher.doFinal(input);
    }

    //generate random key for a specific alg - like getRandomBytes
    public static byte[] generateSessionKey(String algorithm, int keySize) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
        keyGenerator.init(keySize);
        return keyGenerator.generateKey().getEncoded();
    }

    public static byte[] getDigitalSignature(String fileName, PrivateKey key) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        File file = new File(fileName);
        if(!file.exists()){
            System.out.println("File not present");
            throw new FileNotFoundException();
        }
        FileInputStream fis = new FileInputStream(file);

        //read the entire file - only if we have enough RAM
        byte[] fileContent = fis.readAllBytes();

        fis.close();

        //compute the signature
        //1 run => call sign()
        //steps => update()

        //init the signature for signing
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initSign(key);

        //sign the document
        signature.update(fileContent);
        return signature.sign();
    }
    //check if the file is valid based on the digital
    //only with the public key
    //for the source to check
    public static boolean hasValidSignature(String fileName, PublicKey key, byte[] digitalSignature) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        File file = new File(fileName);
        if(!file.exists()){
            System.out.println("File missing");
            throw new FileNotFoundException();
        }
        //read the file
        FileInputStream fis = new FileInputStream(file);
        //get the file content
        byte[] fileContent = fis.readAllBytes();

        fis.close();

        //init signature for verifying
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initVerify(key);

        //the source will recompute the sign so it can check it
        signature.update(fileContent);
        //check it against the one passed in the method's param
        return signature.verify(digitalSignature);
    }
}
