import org.w3c.dom.ls.LSOutput;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Arrays;

public class TestAsymmetric {

    public TestAsymmetric() throws NoSuchAlgorithmException {
    }

    public static void main(String[] args) throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, SignatureException {

        //loading the keystore and printing the content
        KeyStore keyStore = KeyStoreManager.loadKeyStore("ismkeystore.ks", "passks", "pkcs12");//or "jks"-ks type
        KeyStoreManager.listKeyStoreContent(keyStore);

        //loading the public key from a X509 certificate
        PublicKey pubKeyStudentIsm = KeyStoreManager.getCertificateKey("ISM_Student.cer");
        System.out.println("student.ism.ase.ro public key: " +
                KeyStoreManager.getHex(pubKeyStudentIsm.getEncoded()));

        PublicKey pubKeyStudentIsm1 = KeyStoreManager.getCertificateKey("ISMCertificateX509.cer");
        System.out.println("student.ism.ase.ro public key: " +
                KeyStoreManager.getHex(pubKeyStudentIsm1.getEncoded()));



        if(Arrays.equals(pubKeyStudentIsm1.getEncoded(), pubKeyStudentIsm.getEncoded())){
            System.out.println("The are the same");
        }
        else{
            System.out.println("They are different");
        }

        //loading the keys from the keystore
        PublicKey pubKeyIsmKey1FromStore = KeyStoreManager.getPublicKey(keyStore, "ismkey1");
        System.out.println("ISMKey1 public key from the keystore: ");
        System.out.println(KeyStoreManager.getHex(pubKeyIsmKey1FromStore.getEncoded()));

        if(Arrays.equals(pubKeyStudentIsm1.getEncoded(), pubKeyIsmKey1FromStore.getEncoded())){
            System.out.println("They are the same");
        }
        else{
            System.out.println("They are different");
        }

        //for pcs12 key stores we use the same keystore password
        //for jks - propietary we can use different pass for aliases
        PrivateKey privKeyIsmKey1 = KeyStoreManager.getPrivateKey(keyStore, "ismkey1", "passks");//using the pass for the ks and for each alias
        System.out.println("ISMKey1 private key is:");
        System.out.println(KeyStoreManager.getHex(privKeyIsmKey1.getEncoded()));
        System.out.println("Done");


        //enc and dec with RSA
        byte[] AESSessionKey = KeyStoreManager.generateSessionKey("AES", 128);
        System.out.println("AES Random Session Key:");
        System.out.println(KeyStoreManager.getHex(AESSessionKey));

        byte[] encryptedAESKey = KeyStoreManager.encryptRSA(privKeyIsmKey1, AESSessionKey);
        System.out.println("Encrypted key: ");
        System.out.println(KeyStoreManager.getHex(encryptedAESKey));
        byte[] decryptedAESKey = KeyStoreManager.decryptRSA(pubKeyStudentIsm1, encryptedAESKey );
        System.out.println("Decrypted AES key: ");
        System.out.println(KeyStoreManager.getHex(decryptedAESKey));

        //digital signatures
        byte[] digitalSignature = KeyStoreManager.getDigitalSignature("Message.txt", privKeyIsmKey1);
        System.out.println("The file digital signature is: ");
        System.out.println(KeyStoreManager.getHex(digitalSignature));

        /*File dsFile = new File("MessageSignature.ds");
        if(!dsFile.exists()){
            dsFile.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(dsFile);
        fos.write(digitalSignature);
        fos.close();
        */
        //get the signature from the file
        File dsFile = new File("MessageSignature.ds");
        FileInputStream fis = new FileInputStream(dsFile);
        byte[] dsFromFile = fis.readAllBytes();
        fis.close();

        if(KeyStoreManager.hasValidSignature("Message.txt", pubKeyIsmKey1FromStore, dsFromFile)){
            System.out.println("The file is valid");
        }
        else{
            System.out.println("Vader changed the file");
        }
    }
}
