import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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

    public static void encryptECB(
            String inputFileName,
            String password,
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
        SecretKeySpec key = new SecretKeySpec(password.getBytes(), algorithm);
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
            String password,
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
        SecretKeySpec key = new SecretKeySpec(password.getBytes(), algorithm);
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

    public static void main(String[] args) throws NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        encryptECB("Message.txt", "passwordpassword", "MessageEncrypted.enc", "AES");
        decryptECB("MessageEncrypted.enc", "passwordpassword", "MessageDecrypted.txt", "AES");

        //can we use DES instead of AES
            //A: yes, it is in documentation
        //how to make sure the password has the required size
            //A: you have to know
            //PASSWORD MUST HAVE A SIZE = BLOCK SIZE
            //DES - 64 bits - 8 bytes - 8 caractere la parola
            //AES - 128 bits - 16 bytes - 16 caractere la parola
            //aparent, din ce am testat eu, la AES ai mereu block de 128 bits,
            //iar parola poate sa fie de 16, 24, 32 de caractere

        //when can we use no padding - when we know that the file size is a multiple of the block size
        //PKCS5Padding - este pentru DES, nu AES; este pentru 64 bits (asa a zis boja, dar nu scrie nimic de genul in documentatie)

        //O metoda de a verifica daca s-a facut totul ok este sa check the file size (gen sa fie multiplu sau divizor de blocksize)

        System.out.println("Done");
        //ECB - are pattern-uri, de asta nu e bun

        //ar trebui sa incerc sa fac asta pe un String, sa vad daca imi iese
        //encrypt
        String parola = "parola";
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKeySpec key = new SecretKeySpec("passwordpassword".getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedString = cipher.doFinal(parola.getBytes()); //criptez codurile ascii ale literelor(caracterelor)
        System.out.println(getHex(encryptedString));
        getHexFile("MessageEncrypted.enc");

        System.out.println();

        //decrypt
        Cipher cipher2 = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKeySpec key2 = new SecretKeySpec("passwordpassword".getBytes(), "AES");
        cipher2.init(Cipher.DECRYPT_MODE, key2);
        byte[] decryptedByteString = cipher2.doFinal(encryptedString); //aici am codurile ascii ale literelor (caracterelor)
        String decryptedString = new String(decryptedByteString);
        System.out.println(decryptedString);

    }
}
