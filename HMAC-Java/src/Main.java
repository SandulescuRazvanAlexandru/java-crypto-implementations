import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Main {

    public static String getHex(byte[] array) {
        String output = "";
        for (byte value : array) {
            output += String.format("%02x", value);
        }
        return output;
    }

    public static byte[] getHashMACFile(String inputFileName, byte[] secretKey, String algorithm) throws IOException, InvalidKeyException, NoSuchAlgorithmException {

        File file = new File(inputFileName);
        if(!file.exists()) {
            //throw new FileNotFoundException();
            System.out.println("***** File is not there");
            return null;
        }

        //init the Mac object
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(secretKey, algorithm));

        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);

        byte[] buffer = new byte[1024]; //pot sa am si 10 aici, nu conteaza
        int noBytesFromFile = bis.read(buffer);
        while(noBytesFromFile != -1) {
            mac.update(buffer, 0, noBytesFromFile);
            noBytesFromFile = bis.read(buffer);
        }
        byte[] hashMac = mac.doFinal();

        return hashMac;
    }

    public static byte[] getHashMACString(String message, byte[] secretKey, String algorithm) throws IOException, InvalidKeyException, NoSuchAlgorithmException {
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(secretKey, algorithm));
        byte[] hashMac = mac.doFinal(message.getBytes());
        return hashMac;
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        //hmac for a file (this is the most common scenario, they(hmacs) are associated with files)
        byte[] hmac = getHashMACFile("Message.txt", "ismsecret".getBytes(), "HmacSHA1");
        System.out.println("HashMAC for File = " + getHex(hmac));

        //hmac for a string
        String message = "This is a secret message.";
        byte[] hmacString = getHashMACString(message, "ismsecret".getBytes(), "HmacSHA1");
        System.out.println("HashMAC for String = " + getHex(hmacString));

        //for the record, e diferit de SHA-1, hmac provides integrity bla bla
        System.out.println("===SHA-1===");
        String pass = "This is a secret message.";
        MessageDigest messageDigestSHA1 = MessageDigest.getInstance("SHA-1");
        byte[] hashPass = messageDigestSHA1.digest(pass.getBytes());
        System.out.println("SHA-1 = " + getHex(hashPass));
    }
}
