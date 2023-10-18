import java.io.*;
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

    public static byte[] OTPEncrypt(String inputFileName, String outputFileName) throws NoSuchAlgorithmException, IOException {

        File inputFile = new File(inputFileName);
        if(!inputFile.exists()) {
            //throw new FileNotFoundException();
            System.out.println("***** File is not there");
            return null;
        }

        byte[] randomKey = getRandomBytes((int) inputFile.length(), null);

        //open the output file for writing
        File outputFile = new File(outputFileName);
        if(!outputFile.exists()) {
            outputFile.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(outputFile);

        //open the file for reading
        FileInputStream fis = new FileInputStream(inputFile);
        BufferedInputStream bis = new BufferedInputStream(fis);
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

    public static void OTPDecrypt(String inputFileName, String outputFileName, byte[] randomKey) throws Exception {

        File inputFile = new File(inputFileName);
        if(!inputFile.exists()) {
            //throw new FileNotFoundException();
            System.out.println("***** File is not there");
            return;
        }

        if(randomKey.length < inputFile.length()) {
            //throw new Exception("The key is too short");
            System.out.println("The key is too short");
            return;
        }

        //open the output file for writing
        File outputFile = new File(outputFileName);
        if(!outputFile.exists()) {
            outputFile.createNewFile();
        }

        FileOutputStream fos = new FileOutputStream(outputFile);

        //open the file for reading
        FileInputStream fis = new FileInputStream(inputFile);
        BufferedInputStream bis = new BufferedInputStream(fis);
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
        byte[] otpKey = OTPEncrypt("Message.txt", "MessageOTPEncrypted.otp");
        OTPDecrypt("MessageOTPEncrypted.otp", "MessageOTPDecrypted.txt", otpKey);

        System.out.println("Done!");
    }
}
