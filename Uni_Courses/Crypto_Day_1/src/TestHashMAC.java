import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class TestHashMAC {

    public static byte[] getHashMAC(String inputFileName, byte[] secretKey, String algorithm) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        byte[] hashMac = null;

        File file = new File(inputFileName);
        if(!file.exists()){
            throw new FileNotFoundException();
        }

        //init the Mac object
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(secretKey, algorithm));

        //open the file - at byte level
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);

        byte[] buffer = new byte[1024]; //read 1k at a time
        int noBytesFromFile = bis.read(buffer) ;//how many B does the file have

        while(noBytesFromFile != -1){
            mac.update(buffer, 0, noBytesFromFile);
            noBytesFromFile = bis.read(buffer);//fill up the buffer
        }

        hashMac = mac.doFinal();

        return hashMac;
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException {
        //hmac depends on the secretkey and on the file content

        byte[] hmac = getHashMAC("Message.txt", "ismsecret".getBytes(), "HmacSHA1");
        System.out.println("HashMAC = " + TestMessageDigest.getHex(hmac));

        byte[] sha1Hash = TestMessageDigest.getFileHash("Message.txt", "SHA1", "BC");
        System.out.println("HashMAC = " + TestMessageDigest.getHex(sha1Hash));
    }
}
