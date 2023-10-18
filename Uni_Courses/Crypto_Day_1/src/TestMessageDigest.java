import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;

//HASH - if we use the same algorithm and provider, the output will always be the same
//=> the hash is not random
//CASE SENSITIVE
public class TestMessageDigest {
    //they return byte[]

    public static byte[] getStringHash(String value, String algorithm, String provider) throws NoSuchAlgorithmException, NoSuchProviderException {
        byte[] hashValue = null;

        MessageDigest messageDigest = null;

        if(checkProvider(provider)){
            System.out.println("Using BC");
            messageDigest = MessageDigest.getInstance(algorithm, provider);
        }
        else {
            System.out.println("Using default provider");
            messageDigest = MessageDigest.getInstance(algorithm);
        }

        //we have the value in memory, so we skip update
        hashValue = messageDigest.digest(value.getBytes());

        return hashValue;
    }

    public static boolean checkProvider(String providerName){
        Provider provider = Security.getProvider(providerName);
        if (provider != null){
            return true;
        }
        else{
            return false;
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

    //we should never read a file 1 byte at a time
    public static byte[] getFileHash(String fileName, String algorithm, String provider) throws NoSuchAlgorithmException, NoSuchProviderException, IOException {
        byte[] hashValue = null;
        MessageDigest messageDigest = null;

        if(checkProvider(provider)){
            System.out.println("Using BC");
            messageDigest = MessageDigest.getInstance(algorithm, provider);
        }
        else {
            System.out.println("Using default provider");
            messageDigest = MessageDigest.getInstance(algorithm);
        }

        File file = new File(fileName);
        if(!file.exists()){
            //best practice - throw new FileNotFoundException();
            System.out.println("***** File is not there");
            return null;
        }

        //as stream level bcs we dont need the text, but the bytes
        FileInputStream fis = new FileInputStream(file);
        //read one byte at a time
        byte[] buffer = new byte[1];

        int noBytesFromFile = fis.read(buffer);

        while(noBytesFromFile != -1){ //while we don't reach the end of the file
            messageDigest.update(buffer); //change the inner state
            noBytesFromFile = fis.read(buffer);
        }
        fis.close();

        hashValue = messageDigest.digest();
        return hashValue;
    }

    public static byte[] getFileHashInChunks(String fileName, String algorithm, String provider) throws NoSuchAlgorithmException, NoSuchProviderException, IOException {
        byte[] hashValue = null;
        MessageDigest messageDigest = null;

        if(checkProvider(provider)){
            System.out.println("Using BC");
            messageDigest = MessageDigest.getInstance(algorithm, provider);
        }
        else {
            System.out.println("Using default provider");
            messageDigest = MessageDigest.getInstance(algorithm);
        }

        File file = new File(fileName);
        if(!file.exists()){
            //best practice - throw new FileNotFoundException();
            System.out.println("***** File is not there");
            return null;
        }

        //as stream level bcs we dont need the text, but the bytes
        FileInputStream fis = new FileInputStream(file);
        //read one byte at a time
        byte[] buffer = new byte[10]; //the file has 25B

        int noBytesFromFile = fis.read(buffer);

        while(noBytesFromFile != -1){ //while we don't reach the end of the file
            messageDigest.update(buffer, 0, noBytesFromFile); //
            noBytesFromFile = fis.read(buffer);
        }
        fis.close();

        hashValue = messageDigest.digest();
        return hashValue;
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

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, IOException {

    String password = "123456";
    byte[] passHash = getStringHash(password, "MD5", "BC");
        System.out.println("MD5 Hash = " + getHex(passHash));

        byte[] otherHash = "e10adc3949ba59abbe56e057f20f883e".getBytes();//here we get the ascii codes of the letters
        //they are always different
        if(passHash.equals(otherHash)){
            System.out.println("Are the same");
        }
        else{
            System.out.println("They are different");
        }

        //they are the same
        if(getHex(passHash).equals("e10adc3949ba59abbe56e057f20f883e")){
            System.out.println("Are the same");
        }
        else{
            System.out.println("They are different");
        }

        byte[] sha1Pass = getStringHash(password, "SHA-1", "BC");
        System.out.println("SHA1 Hash = " + getHex(sha1Pass));

        byte[] fileHash = getFileHash("Message.txt", "SHA-1", "BC");
        System.out.println("File SHA1 value = " + getHex(fileHash));

        fileHash = getFileHashInChunks("Message.txt", "SHA-1", "BC");
        System.out.println("File SHA1 value = " + getHex(fileHash));

        //using a predefined salt as a string
        //less secure than a random salt
        String predefinedSalt = "ism_";
        passHash = getStringHash(predefinedSalt + password, "MD5", "BC");
        System.out.println("MD5 Hash with salt = " + getHex(passHash));

        //using a random salt for each user
        //you need to store it in the database
        byte[] randomSalt = getRandomBytes(10, null);
        byte[] passwordBytes = password.getBytes();

        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        byteArray.writeBytes(randomSalt);
        byteArray.writeBytes(passwordBytes);

        byte[] passWithSalt = byteArray.toByteArray();
        System.out.println("The final pass size is " + passWithSalt.length);

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] md5Hash = md.digest(passWithSalt);
        System.out.println("Pass with random salt hash " + getHex(md5Hash));

    }
}
