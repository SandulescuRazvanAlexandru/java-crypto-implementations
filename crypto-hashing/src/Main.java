import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class Main {

    public static String getHex(byte[] array) {
        String output = "";
        for (byte value : array) {
            output += String.format("%02x", value);
        }
        return output;
    }

    public static byte[] getStringHash(String password, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);;
        byte[] hashValue = messageDigest.digest(password.getBytes());
        return hashValue;
    }

    public static byte[] getFileHash(String fileName, String algorithm) throws NoSuchAlgorithmException, IOException {

        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);

        File file = new File(fileName);
        if(!file.exists()) {
            //throw new FileNotFoundException();
            System.out.println("***** File is not there");
            return null;
        }

        //we process it at binary level pentru ca pentru hard nu e nicio diferenta daca este .txt sau binary
        //plus, ca nu ma intereseaza continutul, doar vreau sa calculez hash
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[10];
        int noBytesFromFile = fis.read(buffer);
        while(noBytesFromFile != -1) {
            messageDigest.update(buffer, 0, noBytesFromFile); //from offset 0 with the length
            noBytesFromFile = fis.read(buffer);
        }

        fis.close();
        byte[] hashValue = messageDigest.digest();

        return hashValue;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {

        //compute the hash of a string
        //all those functions will return byte arrays (remember we can't store them in strings because we lose the value)
        //we can? store getHex or base64?????? nu imi e clar aici

        //===MD5===//
        System.out.println("===MD5===");
        String password = "123456";
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] hashPassword = messageDigest.digest(password.getBytes());
        System.out.println("MD5 = " + getHex(hashPassword));

        //Do NOT do this
        String passwordCopy = new String(hashPassword);
        System.out.println("The password copy is " + passwordCopy);

        //DO this
        String base64Password = Base64.getEncoder().encodeToString(hashPassword);
        System.out.println("Base64 encoding of the password is " + base64Password);
        byte[] reversePassword = Base64.getDecoder().decode(base64Password);
        System.out.println("Reversed password is " + getHex(reversePassword));


        //===SHA-128===//
        System.out.println("===SHA-1===");
        String pass = "This is a secret message.";
        MessageDigest messageDigestSHA1 = MessageDigest.getInstance("SHA-1");
        byte[] hashPass = messageDigestSHA1.digest(pass.getBytes());
        System.out.println("SHA-1 = " + getHex(hashPass));

        //===SHA-256===//
        System.out.println("===SHA-256===");
        String parola = "123456";
        MessageDigest messageDigestSHA256 = MessageDigest.getInstance("SHA-256");
        byte[] hashParola = messageDigestSHA256.digest(parola.getBytes()); //here is the binary value of that hash which is printed in hexa
        System.out.println("SHA-256 = " + getHex(hashParola));

//        //afiseaza total alte chestii gen -115 -106 ???
//        System.out.println("################");
//        for (byte b : hashParola) {
//            System.out.println(b);
//        }
//        System.out.println("################");

        //check if 2 hash values are identical
        byte[] otherHash = "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92".getBytes(); //here is a string and getBytes() gets the ascii codes of those letters

        //they are always different
        if (hashParola.equals(otherHash)) {
            System.out.println("Are the same");
        } else {
            System.out.println("They are different");
        }
        //they are always different
        if (Arrays.equals(hashParola, otherHash)) {
            System.out.println("Are the same");
        } else {
            System.out.println("They are different");
        }

        //this is the correct version
        if (getHex(hashParola).equals("8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92")) {
            System.out.println("Are the same");
        } else {
            System.out.println("They are different");
        }

        //using functions
        String parolapass = "This is a secret message.";
        byte[] sha1Pass = getStringHash(parolapass, "SHA-1");
        System.out.println("SHA1 hash = " + getHex(sha1Pass));

        //for Files
        byte[] hashForFile= getFileHash("Message.txt.", "SHA-1");
        System.out.println("SHA-128 for file = " + getHex(hashForFile));

        //===SALT===//
        //using a predefined salt as a string
        String anotherPassword = "12345678";
        String predefinedSalt = "ism_";
        byte[] anotherHash = getStringHash(anotherPassword, "SHA-1");
        System.out.println("SHA-1 hash without salt = " + getHex(anotherHash));
        byte[] anotherHash2 = getStringHash(predefinedSalt + anotherPassword, "SHA-1");
        System.out.println("SHA-1 hash with salt at the begining = " + getHex(anotherHash2));
        byte[] anotherHash3 = getStringHash(anotherPassword + predefinedSalt, "SHA-1");
        System.out.println("SHA-1 hash with salt at the end = " + getHex(anotherHash3));


    }
}
