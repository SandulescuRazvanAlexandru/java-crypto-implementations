import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class TestSymmetricECB {

    //generate an enc file
    public static void encryptECB(String plaintextFileName, String password, String ciphertextFile, String algorithm) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        //prepare the file
        File inputFile = new File(plaintextFileName);
        if (!inputFile.exists()){
            System.out.println("File is not present there");
            throw new FileNotFoundException();
        }

        //open the input file
        FileInputStream fis = new FileInputStream(inputFile);
        BufferedInputStream bis = new BufferedInputStream(fis);

        //open the ouput file
        File outputFile = new File(ciphertextFile);
        if(!outputFile.exists()){
            outputFile.createNewFile();
        }

        FileOutputStream fos = new FileOutputStream(outputFile);

        //prepare the enc - we have the input/output until now
        //1. create the cipher object
        //"DES/ECB/PKCS5Padding
        Cipher cipher = Cipher.getInstance(algorithm + "/ECB/PKCS5Padding");
        //2. init the cipher object
        //the cipher will not accept the direct byte array directly, so we use a wrapper for the key
        SecretKeySpec key = new SecretKeySpec(password.getBytes(), algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        //3. encrypt the input
        //we will read the file in blocks
        int blockSize = cipher.getBlockSize();//will return the blocksize in term of bytes
        byte[] buffer = new byte[blockSize];//reads  the block
        int noInputBytes = 0;
        byte[] cipherBuffer;//the result


        while( (noInputBytes = bis.read(buffer)) > -1) {
           cipherBuffer = cipher.update(buffer, 0, noInputBytes);
            fos.write(cipherBuffer);
        }

        //get the last block of ciphertext - it may need padding
        cipherBuffer = cipher.doFinal();
        fos.write(cipherBuffer);

        fis.close();
        fos.close();
    }

    public static void decryptECB(String ciphertextFileName, String password, String plaintextFileName, String algorithm) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

         //open the files
        File inputFile = new File(ciphertextFileName);
        if(!inputFile.exists()){
            System.out.println("No input file available");
            throw new FileNotFoundException();
        }

        FileInputStream fis = new FileInputStream(inputFile);
        BufferedInputStream bis = new BufferedInputStream(fis);

        File outputFile = new File(plaintextFileName);
        if(!outputFile.exists()){
            outputFile.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(outputFile);


        Cipher cipher = Cipher.getInstance(algorithm + "/ECB/PKCS5Padding");
        //create the key
        SecretKeySpec key = new SecretKeySpec(password.getBytes(), algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] buffer = new byte[cipher.getBlockSize()];
        int noInputBytes = 0;
        byte[] outputBuffer;

        //while we don't reach the end of the file
        while((noInputBytes = bis.read(buffer)) > -1){
            outputBuffer = cipher.update(buffer, 0, noInputBytes);
            fos.write(outputBuffer);
        }
        outputBuffer = cipher.doFinal();
        fos.write(outputBuffer);

        fis.close();
        fos.close();
    }

    public static void main(String []args) throws NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        //key size = block size
        //AES KEY 16B
        //DES KEY 8B
       // TestSymmetricECB.encryptECB("Message.txt", "password", "Message.enc", "DES");
       //TestSymmetricECB.decryptECB("Message.enc", "password", "Messagecopy.txt", "DES");

        TestSymmetricECB.encryptECB("Message.txt", "passwordpassword", "Message.enc", "AES");
        TestSymmetricECB.decryptECB("Message.enc", "passwordpassword", "Messagecopy.txt", "AES");

       System.out.println("Done");
    }
}
