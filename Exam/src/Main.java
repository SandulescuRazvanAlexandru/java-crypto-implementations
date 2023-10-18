import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
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

    public static byte[] getHashMACString(String message, byte[] secretKey, String algorithm) throws IOException, InvalidKeyException, NoSuchAlgorithmException {
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(secretKey, algorithm));
        byte[] hashMac = mac.doFinal(message.getBytes());
        return hashMac;
    }

    public static void printFolderContent(String path) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        File folder = new File(path);
        if (folder.exists() && folder.isDirectory()) {
            File[] entries = folder.listFiles();
            for (File entry : entries) {
                System.out.println(entry.getName());
                File textMessage = new File(entry.getAbsolutePath());
                FileReader reader = new FileReader(textMessage);
                BufferedReader bufferReader = new BufferedReader(reader);
                System.out.println("\tPrima linie este: ");
                String line = bufferReader.readLine();
                System.out.println(line);

                //hmac for a string
                byte[] hmacString = getHashMACString(line, "ismsecret".getBytes(), "HmacSHA256");
                System.out.println("\tHashMAC for the first line in " + entry.getName() + " is = " + getHex(hmacString));

                if (getHex(hmacString).equals("003235b3c3cf8b2214be800f130567c7c55bd349743051376c61029a73720352")) {
                    System.out.println("Are the same");
                    System.out.println("===============================================");
                    System.out.println("\nMY FILE IS: " + entry.getName());
                    System.out.println("===============================================");
                    break;
                } else {
                    System.out.println("They are different");
                }

                reader.close();
            }
        }
    }

    public static void decryptCTR(
            String encryptedFile,
            byte[] password,
            String decryptedFile,
            String algorithm
    ) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {

        File inputFile = new File(encryptedFile);
        if (!inputFile.exists()) {
            //throw new FileNotFoundException();
            System.out.println("***** File is not there");
            return;
        }
        FileInputStream fis = new FileInputStream(inputFile);
        BufferedInputStream bis = new BufferedInputStream(fis);

        File outputFile = new File(decryptedFile);
        if (!outputFile.exists()) {
            outputFile.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(outputFile);

        Cipher cipher = Cipher.getInstance(algorithm + "/CTR/NoPadding");
        int blockSize = cipher.getBlockSize();

        byte[] IV = new byte[blockSize];
        IV[cipher.getBlockSize() - 1] = 0b00110011;

        SecretKeySpec key = new SecretKeySpec(password, algorithm);
        IvParameterSpec counterSpec = new IvParameterSpec(IV);

        cipher.init(Cipher.DECRYPT_MODE, key, counterSpec);

        byte[] buffer = new byte[cipher.getBlockSize()];
        int noInputBytes = 0;
        byte[] outputBuffer;
        while ((noInputBytes = bis.read(buffer)) > -1) {
            outputBuffer = cipher.update(buffer, 0, noInputBytes);
            fos.write(outputBuffer);
        }

        outputBuffer = cipher.doFinal();
        fos.write(outputBuffer);

        fis.close();
        fos.close();
    }

    public static void encryptECB(
            String inputFileName,
            byte[] password,
            String encryptedFileName,
            String algorithm
    ) throws IOException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {

        File inputFile = new File(inputFileName);
        if (!inputFile.exists()) {
            //throw new FileNotFoundException();
            System.out.println("***** File is not there");
            return;
        }

        FileInputStream fis = new FileInputStream(inputFile);
        BufferedInputStream bis = new BufferedInputStream(fis);

        File outputFile = new File(encryptedFileName);
        if (!outputFile.exists()) {
            outputFile.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(outputFile, false); //aici as putea sa pun false ca parametru? or whatever

        //1. Create the Cipher object
        //"DES/ECB/PKCS5Padding"
        Cipher cipher = Cipher.getInstance(algorithm + "/ECB/PKCS5Padding"); //aleg din documentatie
        //2. Init the Cipher object
        SecretKeySpec key = new SecretKeySpec(password, algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        //3. Encrypt the input
        int blockSize = cipher.getBlockSize(); //for DES is 64 bits, for AES is 128 bits for example; return in bytes not bits
        byte[] buffer = new byte[blockSize];
        int noInputBytes = bis.read(buffer);
        byte[] encryptedBuffer;

        while (noInputBytes != -1) {
            encryptedBuffer = cipher.update(buffer, 0, noInputBytes); // cipher.update returneaza un byte array which is actually the output, this is the cypher text block; this is what we need to write into the cipher text file
            fos.write(encryptedBuffer);
            noInputBytes = bis.read(buffer);
        }

        //get the last block of ciphertext ???? whatever
        encryptedBuffer = cipher.doFinal();
        fos.write(encryptedBuffer);

        fis.close();
        fos.close();
    }

    public static void decryptECB(
            String encryptedFileName,
            byte[] password,
            String decryptedFileName,
            String algorithm
    ) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        File inputFile = new File(encryptedFileName);
        if (!inputFile.exists()) {
            //throw new FileNotFoundException();
            System.out.println("***** File is not there");
            return;
        }
        FileInputStream fis = new FileInputStream(inputFile);
        BufferedInputStream bis = new BufferedInputStream(fis);

        File outputFile = new File(decryptedFileName);
        if (!outputFile.exists()) {
            outputFile.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(outputFile, false);

        Cipher cipher = Cipher.getInstance(algorithm + "/ECB/PKCS5Padding");
        SecretKeySpec key = new SecretKeySpec(password, algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] buffer = new byte[cipher.getBlockSize()];
        int noInputBytes = bis.read(buffer);
        byte[] decryptedBuffer;
        while (noInputBytes != -1) {
            decryptedBuffer = cipher.update(buffer, 0, noInputBytes);
            fos.write(decryptedBuffer);
            noInputBytes = bis.read(buffer);
        }

        decryptedBuffer = cipher.doFinal();
        fos.write(decryptedBuffer);

        fis.close();
        fos.close();
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

        //ex 1
        File entry = new File("C:\\Users\\Razvan\\Desktop\\Java_Projects\\Exam_SAP_Java\\Clues");
        if (entry.exists() && entry.isDirectory()) {
            System.out.println("Folder content: ");
            printFolderContent(entry.getAbsolutePath());
        }

        System.out.println();
        System.out.println();

        //ex 2
        //989+895 = 1884
        System.out.println("===MD5===");
        String password = "1884";
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] hashPassword = messageDigest.digest(password.getBytes());
        System.out.println("MD5 = " + getHex(hashPassword));

        System.out.println();
        System.out.println();

        //ex 3
        decryptCTR("Question_611.enc", hashPassword, "MessageCTR.txt", "AES");

        //ex 4
        encryptECB("Name.txt", hashPassword, "response.enc", "AES");

        //just to check
        decryptECB("response.enc", hashPassword, "MessageDecrypted.txt", "AES");


    }
}
