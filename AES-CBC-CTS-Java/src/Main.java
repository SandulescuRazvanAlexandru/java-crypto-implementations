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

public class Main {

    public static byte[] getRandomBytes(int noBytes, byte[] seed) throws NoSuchAlgorithmException {
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        if (seed != null) {
            secureRandom.setSeed(seed);
        }
        byte[] randomBytes = new byte[noBytes];
        secureRandom.nextBytes(randomBytes);

        return randomBytes;
    }

    //CBC implementation with a random IV placed at the beginning of the file
    public static void encryptCBC_CTS(
            String inputFileName,
            String password,
            String encryptedFileName,
            String algorithm
    ) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

        File inputFile = new File(inputFileName);
        if(!inputFile.exists()) {
            //throw new FileNotFoundException();
            System.out.println("***** File is not there");
            return;
        }

        FileInputStream fis = new FileInputStream(inputFile);
        BufferedInputStream bis = new BufferedInputStream(fis);

        File outputFile = new File(encryptedFileName);
        if(!outputFile.exists()) {
            outputFile.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(outputFile);

        //for CTS mode we don't need padding
        //the mode is CBC -- kinda gresit imo
        Cipher cipher = Cipher.getInstance(algorithm + "/CTS/NoPadding"); //asa e CTS
        SecretKeySpec key = new SecretKeySpec(password.getBytes(), algorithm);

        //we need iv because tehnically cts is cbc
        byte[] IV = getRandomBytes(cipher.getBlockSize(), null);

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

    public static void decryptCBC_CTS(
            String encryptedFileName,
            String password,
            String decryptedFileName,
            String algorithm
    ) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

        File inputFile  = new File(encryptedFileName);
        if(!inputFile.exists()) {
            //throw new FileNotFoundException();
            System.out.println("***** File is not there");
            return;
        }
        FileInputStream fis = new FileInputStream(inputFile);
        BufferedInputStream bis = new BufferedInputStream(fis);

        File outputFile = new File(decryptedFileName);
        if(!outputFile.exists()) {
            outputFile.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(outputFile);


        Cipher cipher = Cipher.getInstance(algorithm + "/CTS/NoPadding");

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

    public static void main(String[] args) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        encryptCBC_CTS("Message.txt", "passwordpassword", "MessageEncrypted.enc", "AES");
        decryptCBC_CTS("MessageEncrypted.enc", "passwordpassword", "MessageDecrypted.txt", "AES");

        System.out.println("Done");

        //intrebari

        //atentie la lungimea parolelor

        //care e faza cu CTS?
            // de la ce vine -> Cipher Text Stealing
            // si care e faza cu padding-ul la el -> we encrypt without using the standard paddings, we use the CTS padding
            // explica boja la inceput cum se face bla bla
            //basically, you do not reveal the block size
            //adica size MessageEncrypted = size Message (daca nu avem iv scris in fisier, daca mergem pe known iv, o sa fie same size)

        //it is cbc pentru ca nu are pattern-uri (adica nu e ecb)
        //this is cbc but smarter pentru ca are chestia asta cu padding

        //"in java", it is actually a CBC implementation

        //boja zice ca de fapt asta e cbc cu padding cts,
        //nu ca ar fi cts cu nopadding
        //gen asta e parerea lui, ca asa ar fi trebuit sa fie

        //de ce am eroare daca continutul fisierului este "parola"
        //de facut si pe un string?


        //sa fac string la asta ca sa ma mai familiarizez
        //deci practic doar modific acolo gen cbc - cts si no padding
    }
}
