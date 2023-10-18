import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class TestSymmetricCTR {
    //we can use a block cipher as a stream cipher = process each byte at a time
    //with DES or AES
    //reaction registry - 128bits -> initial value; everytime we process a B, it will be incremented
    //the counter is encrypted - value enc with AES/DES => XOR with the plaintext
    // => generates a key everytime (the counter is changing everytime)

    public static void encryptCTR(String plaintextFileName, String password, String ciphertextFile, String algorithm) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
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
        Cipher cipher = Cipher.getInstance(algorithm + "/CTR/NoPadding");
        //2. define the initial value for the counter
        //its like a cbc iv - 128b register
        byte[] IV = new byte[cipher.getBlockSize()];
        //the counter will be incremented - now its 1
        IV[cipher.getBlockSize() -1] = 0x01; //0000 0001

        //3. init the cipher object
        //the cipher will not accept the direct byte array directly, so we use a wrapper for the key
        SecretKeySpec key = new SecretKeySpec(password.getBytes(), algorithm);
        IvParameterSpec ivSpec = new IvParameterSpec(IV);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
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

    public static void decryptCTR(String ciphertextFileName, String password, String plaintextFileName, String algorithm) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {

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


        Cipher cipher = Cipher.getInstance(algorithm + "/CTR/NoPadding");

       byte[] initialCounter = new byte[cipher.getBlockSize()];
       initialCounter[cipher.getBlockSize()-1] = 0x01;

        //create the key
        SecretKeySpec key = new SecretKeySpec(password.getBytes(), algorithm);
        IvParameterSpec counterSpec = new IvParameterSpec(initialCounter);

        cipher.init(Cipher.DECRYPT_MODE, key, counterSpec);

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

    public static void main(String[] args) throws NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        TestSymmetricCTR.encryptCTR("Message.txt", "password", "MessageCTR.enc", "DES");
        TestSymmetricCTR.decryptCTR("MessageCTR.enc", "password", "MessageCTR.txt", "DES");

        System.out.println("Done");
    }
}
