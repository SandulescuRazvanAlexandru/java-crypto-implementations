import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

public class Main {

    public static String getHex(byte[] array) {
        String output = "";
        for (byte value : array) {
            output += String.format("%02x", value);
        }
        return output;
    }

    public static void getHexFile(String inputFileName) throws IOException {
        File inputFile = new File(inputFileName);
        if (!inputFile.exists()) {
            //throw new FileNotFoundException();
            System.out.println("***** File is not there");
            return;
        }
        FileInputStream fis = new FileInputStream(inputFile);
        BufferedInputStream bis = new BufferedInputStream(fis);

        byte[] buffer = new byte[1];
        int noInputBytes = bis.read(buffer);
        while (noInputBytes != -1) {
            System.out.printf(String.format("%02x", buffer[0]));
            noInputBytes = bis.read(buffer);
        }
    }

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
    public static void encryptCBCbegining(
            String inputFileName,
            String password,
            String encryptedFileName,
            String algorithm
    ) throws IOException, InvalidAlgorithmParameterException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException {

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
        FileOutputStream fos = new FileOutputStream(outputFile, false);

        Cipher cipher = Cipher.getInstance(algorithm + "/CBC/PKCS5Padding");
        SecretKeySpec key = new SecretKeySpec(password.getBytes(), algorithm);

        byte[] IV = getRandomBytes(cipher.getBlockSize(), null); //size-ul IV-ului trebuie sa fie = cipher block
// ca gen e si el un wannabe block si trebuie sa aiba aceeasi dimensiune (again, DES - 64 bits - 8 bytes, AES - 128 bits - 16 bytes)
        IvParameterSpec ivSpec = new IvParameterSpec(IV);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

        //write the IV value in the output file
        //aici o scriem la inceput
        fos.write(IV);

        byte[] buffer = new byte[cipher.getBlockSize()];
        int noInputBytes = bis.read(buffer);
        byte[] outputBuffer;
        while (noInputBytes != -1) {
            outputBuffer = cipher.update(buffer, 0, noInputBytes);
            fos.write(outputBuffer);
            noInputBytes = bis.read(buffer);
        }

        outputBuffer = cipher.doFinal();
        fos.write(outputBuffer);

        fis.close();
        fos.close();
    }

    public static void decryptCBCbegining(
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
        FileOutputStream fos = new FileOutputStream(outputFile, false);

        Cipher cipher = Cipher.getInstance(algorithm + "/CBC/PKCS5Padding");
        int blockSize = cipher.getBlockSize();

        //read the IV from the file
        //pentru ca am criptat cu IV-ul la inceput
        byte[] IV = new byte[blockSize];
        bis.read(IV);

        SecretKeySpec key = new SecretKeySpec(password.getBytes(), algorithm);
        IvParameterSpec ivSpec = new IvParameterSpec(IV);
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);

        byte[] buffer = new byte[blockSize];
        int noInputBytes = bis.read(buffer);
        byte[] outputBuffer;
        while (noInputBytes != -1) {
            outputBuffer = cipher.update(buffer,0,noInputBytes);
            fos.write(outputBuffer);
            noInputBytes = bis.read(buffer);
        }

        outputBuffer = cipher.doFinal();
        fos.write(outputBuffer);

        fos.close();
        fis.close();
    }

    //CBC implementation with a random IV placed at the end of the file
    public static void encryptCBCEnd(
            String inputFileName,
            String password,
            String encryptedFileName,
            String algorithm
    ) throws IOException, InvalidAlgorithmParameterException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException {

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
        FileOutputStream fos = new FileOutputStream(outputFile, false);

        Cipher cipher = Cipher.getInstance(algorithm + "/CBC/PKCS5Padding");
        SecretKeySpec key = new SecretKeySpec(password.getBytes(), algorithm);
        byte[] IV = getRandomBytes(cipher.getBlockSize(), null);
        IvParameterSpec ivSpec = new IvParameterSpec(IV);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

        byte[] buffer = new byte[cipher.getBlockSize()];
        int noInputBytes = bis.read(buffer);
        byte[] outputBuffer;
        while (noInputBytes != -1) {
            outputBuffer = cipher.update(buffer, 0, noInputBytes);
            fos.write(outputBuffer);
            noInputBytes = bis.read(buffer);
        }

        outputBuffer = cipher.doFinal(); //asta e un fel de "inca e faucet-ul deschis"
        fos.write(outputBuffer);

        fos.write(IV);

        fis.close();
        fos.close();
    }

    public static void decryptCBCEnd(
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
        FileOutputStream fos = new FileOutputStream(outputFile, false);

        Cipher cipher = Cipher.getInstance(algorithm + "/CBC/PKCS5Padding");
        int blockSize = cipher.getBlockSize();

        //detectam IV-ul
        byte[] buffer = new byte[blockSize];
        int noInputBytes = bis.read(buffer);
        int k = 0;
        while (noInputBytes != -1) {
            noInputBytes = bis.read(buffer);
            k++;
        }
        byte[] IV = Arrays.copyOf(buffer, blockSize);

        SecretKeySpec key = new SecretKeySpec(password.getBytes(), algorithm);
        IvParameterSpec ivSpec = new IvParameterSpec(IV);
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);

        fis.close();
        fis = new FileInputStream(inputFile);
        bis = new BufferedInputStream(fis);
        byte[] outputBuffer;
        for(int i = 0; i < k - 1; i++) {
            bis.read(buffer);
            outputBuffer = cipher.update(buffer);
            fos.write(outputBuffer);
        }

        outputBuffer = cipher.doFinal();
        fos.write(outputBuffer);

        fos.close();
        fis.close();
    }

    //CBC implementation with a known IV
    public static void encryptCBCivKnown(
            String inputFileName,
            String password,
            String encryptedFileName,
            String algorithm
    ) throws IOException, InvalidAlgorithmParameterException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException {

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
        FileOutputStream fos = new FileOutputStream(outputFile, false);

        Cipher cipher = Cipher.getInstance(algorithm + "/CBC/PKCS5Padding");
        SecretKeySpec key = new SecretKeySpec(password.getBytes(), algorithm);

        int blockSize = cipher.getBlockSize();

        //init known IV
        byte[] IV = new byte[blockSize];

//        //Scenario 1: all bits/bytes are 0, except for the last one which is 0000 0001 (or the last bit -from left to right- is 1)
//        //By default in Java, data types like int, short, byte, and long arrays are initialized with 0
//        IV[cipher.getBlockSize()-1] = 0x01;		//0000 0001
//        //IV[cipher.getBlockSize()-1] = 0b00000001;;		//0000 0001

        //Scenario 2: all bits are 1
        for (int i = 0; i < blockSize; i++) {
            IV[i] = (byte) 0x11;
        }

        IvParameterSpec ivSpec = new IvParameterSpec(IV);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

        byte[] buffer = new byte[blockSize];
        int noInputBytes = bis.read(buffer);
        byte[] outputBuffer;
        while (noInputBytes != -1) {
            outputBuffer = cipher.update(buffer, 0, noInputBytes);
            fos.write(outputBuffer);
            noInputBytes = bis.read(buffer);
        }

        outputBuffer = cipher.doFinal(); //asta e un fel de "inca e faucet-ul deschis"
        fos.write(outputBuffer);

        fis.close();
        fos.close();
    }

    public static void decryptCBCivKnown(
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
        FileOutputStream fos = new FileOutputStream(outputFile, false);

        Cipher cipher = Cipher.getInstance(algorithm + "/CBC/PKCS5Padding");
        int blockSize = cipher.getBlockSize();

        SecretKeySpec key = new SecretKeySpec(password.getBytes(), algorithm);

        //init known IV
        byte[] IV = new byte[blockSize];

        //Scenario 1:
        //IV[cipher.getBlockSize()-1] = 0x01;

        //Scenario 2:
        for (int i = 0; i < blockSize; i++) {
            IV[i] = (byte) 0x11;
        }

        IvParameterSpec ivSpec = new IvParameterSpec(IV);
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);

        byte[] buffer = new byte[blockSize];
        int noInputBytes = bis.read(buffer);
        byte[] outputBuffer;
        while (noInputBytes != -1) {
            outputBuffer = cipher.update(buffer,0,noInputBytes);
            fos.write(outputBuffer);
            noInputBytes = bis.read(buffer);
        }

        outputBuffer = cipher.doFinal();
        fos.write(outputBuffer);

        fos.close();
        fis.close();
    }

    public static void main(String[] args) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        //===BEGINING===//random IV at the begining
        //encryptCBCbegining("Message.txt", "passwordpassword", "MessageEncrypted.enc", "AES");
        //decryptCBCbegining("MessageEncrypted.enc", "passwordpassword", "MessageDecrypted.txt", "AES");

        //===END===//random IV at the end
        //encryptCBCEnd("Message.txt", "passwordpassword", "MessageEncrypted.enc", "AES");
        //decryptCBCEnd("MessageEncrypted.enc", "passwordpassword", "MessageDecrypted.txt", "AES");

        //===known IV===//
        encryptCBCivKnown("Message.txt", "passwordpassword", "MessageEncrypted.enc", "AES");
        decryptCBCivKnown("MessageEncrypted.enc", "passwordpassword", "MessageDecrypted.txt", "AES");


        //================
        //este aceeasi regula la parole:
            //PASSWORD MUST HAVE A SIZE = BLOCK SIZE
            //DES - 64 bits - 8 bytes - 8 caractere la parola
            //AES - 128 bits - 16 bytes - 16 caractere la parola
            //aparent, din ce am testat eu, la AES ai mereu block de 128 bits,
            //iar parola poate sa fie de 16, 24, 32 de caractere

        //care e faza cu IV - poate sa fie cunoscut, it is not a secret (doar parola trebuie sa fie secreta)
        //unde si cum il pozitionez - este pozitia 99% din cazuri la inceput pentru ca the receiver need the IV value in order
//to decrypt the first block;; daca totusi il pun la final, trebuie sa parcurg tot, sa il iau si apoi sa reincep sa citesc
//also, aici am generat un IV random, dar e ok pentru ca il scriu la inceputul fisierului, ca atare the receiver il stie
//so we have 2 options: chestia asta cu scrisul la inceput saaaaau merg pe predefined value pe care the destination knows
//si nu mai este cazul sa scriu in fisier ca e deja stiut

        //IV sa fie plin de 1 or smth all bits 0 or all bits 1
        //sau sa stiu sa fac daca imi zice "al n-lea bit de la dreapta la stanga sa fie 1, restul 0 or smth like that

        //cbc nu are patterns spre deosebire de ecb


        //String (TREBUIE IV: The main issue in your code was caused by a failure to specify an IV value. You must specify an IV value when doing CBC-mode encryption and use that same value when performing the CBC-mode decryption.)

        //encrypt
        String parola = "parola";
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec key = new SecretKeySpec("passwordpassword".getBytes(), "AES");
        byte[] IV = new byte[16]; //AES are 128 biti (16 bytes)
        //IV[15] = (byte) 0x01;
        for (int i = 0; i < 16; i++) {
            IV[i] = (byte) 0x11;
        }
        IvParameterSpec ivSpec = new IvParameterSpec(IV);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
        byte[] encryptedString = cipher.doFinal(parola.getBytes()); //criptez codurile ascii ale literelor(caracterelor)
        System.out.println(getHex(encryptedString));
        getHexFile("MessageEncrypted.enc"); //ca sa ma verific

        System.out.println();

        //decrypt
        Cipher cipher2 = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec key2 = new SecretKeySpec("passwordpassword".getBytes(), "AES");
        cipher2.init(Cipher.DECRYPT_MODE, key2, ivSpec);
        byte[] decryptedByteString = cipher2.doFinal(encryptedString); //aici am codurile ascii ale literelor (caracterelor)
        String decryptedString = new String(decryptedByteString);
        System.out.println(decryptedString);

        System.out.println("\nDone"); //asta e important to make sure i dont have infinite loops
    }
}
