import java.io.*;
import java.security.NoSuchAlgorithmException;

public class TestOTP {

    //generate a key with secure random and return it
    //OTP - the key size should be at least equal to the file size
    public static byte[] OTPEncrypt(String inputFileName, String outputFileName) throws NoSuchFieldException, NoSuchAlgorithmException, IOException {
        byte[] randomKey = null;

        File inputFile = new File(inputFileName);
        if (!inputFile.exists()){
            throw new NoSuchFieldException();
        }

        randomKey = TestMessageDigest.getRandomBytes((int) inputFile.length(), null);

        //open the file for reading
        FileInputStream fis = new FileInputStream(inputFile);
        BufferedInputStream bis = new BufferedInputStream(fis);

        //create and open the output file for wrting
        File outputFile = new File(outputFileName);
        if(!outputFile.exists()){
            outputFile.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(outputFile);

        byte[] buffer = new byte[1];

        for(long i = 0; i < inputFile.length(); i++){
            bis.read(buffer);
            buffer[0] = (byte) (buffer[0]^randomKey[(int) i]);
            fos.write(buffer);
        }

        fis.close();
        fos.close();

        return randomKey;
    }

    public static void OTPDecrypt(String inputFileName, String outputFileName, byte[] randomKey) throws Exception {

        File inputFile = new File(inputFileName);
        if (!inputFile.exists()){
            throw new NoSuchFieldException();
        }

        //check the key's and inputfilename's sizes
        if(randomKey.length < inputFileName.length()){
            throw new Exception("The key length is too short");
        }

        //open the file for reading
        FileInputStream fis = new FileInputStream(inputFile);
        BufferedInputStream bis = new BufferedInputStream(fis);

        //create and open the output file for writing
        File outputFile = new File(outputFileName);
        if(!outputFile.exists()){
            outputFile.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(outputFile);

        byte[] buffer = new byte[1];

        for(long i = 0; i < inputFile.length(); i++){
            bis.read(buffer);
            buffer[0] = (byte) (buffer[0]^randomKey[(int) i]);
            fos.write(buffer);
        }

        fis.close();
        fos.close();
    }

    public static void main(String[] args) throws Exception {

        byte[] otpKey = OTPEncrypt("Message.txt", "Message.otp");
        OTPDecrypt("Message.otp", "MessageClone.txt", otpKey);
        System.out.println("Done");


    }
}
