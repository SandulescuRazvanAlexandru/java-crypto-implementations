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
import java.security.SecureRandom;

public class TestSymmetricCTS {
    //mode of using CBC
    //needs no padding

    public static void encryptCBC_CTS(String plaintextFileName, String password, String ciphertextFileName, String algorithm) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        File inputFile = new File(plaintextFileName);
        if (!inputFile.exists()){
            System.out.println("File is not present there");
            throw new FileNotFoundException();
        }

        FileInputStream fis = new FileInputStream(inputFile);
        BufferedInputStream bis = new BufferedInputStream(fis);


        File outputFile = new File(ciphertextFileName);
        if(!outputFile.exists()){
            outputFile.createNewFile();
        }

        FileOutputStream fos = new FileOutputStream(outputFile);
        //PKCS5Padding common for java standard + bouncy castle

        //for CTS mode we dont need padding
        //the mode is CB
        Cipher cipher = Cipher.getInstance(algorithm + "/CTS/NoPadding");

        SecretKeySpec key = new SecretKeySpec(password.getBytes(), algorithm);
        byte[] IV = getRandomBytes(cipher.getBlockSize(), null);
        IvParameterSpec ivSpec = new IvParameterSpec(IV);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

        //write the IV value in the output file
        fos.write(IV);

        byte[] buffer = new byte[cipher.getBlockSize()];
        int noInputBytes = 0;
        byte[] outputBuffer;

        while((noInputBytes = bis.read(buffer)) > -1 ){
            outputBuffer = cipher.update(buffer, 0, noInputBytes);
            fos.write(outputBuffer);
        }

        outputBuffer = cipher.doFinal();
        fos.write(outputBuffer);

        fis.close();
        fos.close();
    }

    public static void decryptCBC_CTS(String ciphertextInputFile, String password, String plaintextOutputFile, String algorithm) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        File inputFile = new File(ciphertextInputFile);
        if(!inputFile.exists()){
            System.out.println("No input file available");
            throw new FileNotFoundException();
        }

        FileInputStream fis = new FileInputStream(inputFile);
        BufferedInputStream bis = new BufferedInputStream(fis);

        File outputFile = new File(plaintextOutputFile);
        if(!outputFile.exists()){
            outputFile.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(outputFile);

        //we have to take out the IV before dec
        Cipher cipher = Cipher.getInstance(algorithm + "/CTS/NoPadding");
        int blockSize = cipher.getBlockSize();

        //read the IV from the file
        byte[] IV = new byte[blockSize];
        bis.read(IV); //now we have it

        SecretKeySpec key = new SecretKeySpec(password.getBytes(), algorithm);
        IvParameterSpec ivSpec = new IvParameterSpec(IV);

        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);

        byte[] buffer = new byte[blockSize];
        byte[] outputBuffer;
        int noInputBytes = 0;
        while((noInputBytes = bis.read(buffer)) != -1){
            outputBuffer = cipher.update(buffer, 0, noInputBytes);
            fos.write(outputBuffer);
        }
        outputBuffer = cipher.doFinal();
        fos.write(outputBuffer);

        fos.close();
        fis.close();
    }


    public static byte[] getRandomBytes(int noBytes, byte[] seed) throws NoSuchAlgorithmException {
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        if(seed!=null){
            secureRandom.setSeed(seed);
        }
        byte[] randomBytes = new byte[noBytes];
        secureRandom.nextBytes(randomBytes);

        return randomBytes;
    }

    public static void main(String[] args) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        TestSymmetricCTS.encryptCBC_CTS("Message.txt", "passwordpassword", "MessageCTS.enc", "AES");
        TestSymmetricCTS.decryptCBC_CTS("MessageCTS.enc", "passwordpassword", "MessageCTS.txt", "AES");
        System.out.println("Done");
    }

}
