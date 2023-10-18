import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

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

    public static byte[] pbeEncrypt(
            String inputFileName,
            String password,
            String encryptedFileName,
            String algorithm,
            int noIterations,
            byte[] salt
    ) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        File inputFile = new File(inputFileName);
        if (!inputFile.exists()) {
            //throw new FileNotFoundException();
            System.out.println("***** File is not there");
            return null;
        }

        FileInputStream fis = new FileInputStream(inputFile);
        BufferedInputStream bis = new BufferedInputStream(fis);

        File outputFile = new File(encryptedFileName);
        if (!outputFile.exists()) {
            outputFile.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(outputFile);

        Cipher cipher = Cipher.getInstance(algorithm);
        int blockSize = cipher.getBlockSize();

        PBEKeySpec pbeKey = new PBEKeySpec(password.toCharArray(), salt, noIterations, blockSize);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
        Key key = keyFactory.generateSecret(pbeKey); //it is binary

        System.out.println("Initial password: " + password);
        System.out.println("Generated key (hex): " + getHex(key.getEncoded())); //get the byte[]

        cipher.init(Cipher.ENCRYPT_MODE, key);

        // 3. Encrypt the input
        byte[] buffer = new byte[blockSize];
        int noInputBytes = bis.read(buffer);
        byte[] cipherBuffer;
        while (noInputBytes != -1) {
            cipherBuffer = cipher.update(buffer, 0, noInputBytes);
            fos.write(cipherBuffer);
            noInputBytes = bis.read(buffer);
        }

        // get the last block of ciphertext
        cipherBuffer = cipher.doFinal();
        fos.write(cipherBuffer);

        fis.close();
        fos.close();

        return cipher.getParameters().getEncoded(); //this encryption also generates some data (pbe spec) which we need at the destination
        //i cant decrypt it without this
    }

    public static void pbeDecrypt(
            String encryptedFileName,
            String password,
            String decryptedFileName,
            String algorithm,
            int noIterations,
            byte[] salt,
            byte[] pbeParams
    ) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {

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
        FileOutputStream fos = new FileOutputStream(outputFile);

        Cipher cipher = Cipher.getInstance(algorithm);
        int blockSize = cipher.getBlockSize();

        PBEKeySpec pbeKey = new PBEKeySpec(password.toCharArray(), salt, noIterations, blockSize);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
        Key key = keyFactory.generateSecret(pbeKey);

        System.out.println("Initial password: " + password);
        System.out.println("Generated key (hex): " + getHex(key.getEncoded()));

        AlgorithmParameters algParams = AlgorithmParameters.getInstance(algorithm);
        algParams.init(pbeParams);

        cipher.init(Cipher.DECRYPT_MODE, key, algParams);

        byte[] buffer = new byte[blockSize];
        int noInputBytes = bis.read(buffer);
        byte[] cipherBuffer;
        while (noInputBytes != -1) {
            cipherBuffer = cipher.update(buffer, 0, noInputBytes);
            fos.write(cipherBuffer);
            noInputBytes = bis.read(buffer);
        }

        // get the last block of ciphertext
        cipherBuffer = cipher.doFinal();
        fos.write(cipherBuffer);

        fis.close();
        fos.close();
    }

    public static void main(String[] args) throws NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        byte[] salt = "ism".getBytes();
        byte[] pbeParams;
        pbeParams = pbeEncrypt("Message.txt", "parola", "MessageEncrypted.enc", "PBEWithHmacSHA256AndAES_128", 10000, salt);
        pbeDecrypt("MessageEncrypted.enc", "parola", "MessageDecrypted.txt", "PBEWithHmacSHA256AndAES_128", 10000, salt, pbeParams);

        //intrebari

        //ce inseamna mai exact
            //PBE - password based encryption
            //This is the way we should deal with users passwords which are smaller or bigger than the required size
            //PBE is not an encryption algorithm, it is a way to extend or reduce the users passwords and make it more secure
                //for the algorithm (for when the password doesnt have the required size) --asta nu prea are sens, gen
                //literalmente are in nume encryption, deci este un algoritm de envryption care se ocupa si de faza cu parola
            //A user can choose a password of any size and this is the problem PBE solves

        //the hash function generates the binary key (nu inteleg nici eu cum probabil nici proful)

        //ce face .getEncoded() -> returneaza byte[] al parolei care este binara
            // (este pur si simplu alt tip gen KEY... care nu este byte[] iar eu am nevoie de byte[] ca sa afisez in hexa)

        //acum are sens de ce boja a zis ca nu stie ce foloseste pentru AES, gen nu intelege nici el de ce
            //nu cere iv asa ca te gandesti ca foloseste ecb, dar nu gasesti pattern-uri, deci ar folosi cbc.
            //mister

        //sau sa combin cu AES sa vad daca ar merge ???????????? -> deja folosesti.
            // the hash function generates the binary key (nu inteleg nici eu cum probabil nici proful)
            //si apoi ai algoritmul de criptare


        //pe String

        //encrypt
        String parola = "parola";
        Cipher cipher = Cipher.getInstance("PBEWithHmacSHA256AndAES_128");
        PBEKeySpec pbeKey = new PBEKeySpec(parola.toCharArray(), salt, 10000, 16);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithHmacSHA256AndAES_128");
        Key key = keyFactory.generateSecret(pbeKey);

        System.out.println("Initial password: " + parola);
        System.out.println("Generated key (hex): " + getHex(key.getEncoded()));

        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedString = cipher.doFinal(parola.getBytes());
        System.out.println(getHex(encryptedString));
        getHexFile("MessageEncrypted.enc"); //o sa returneze mereu ceva diferit (atat fata de linia de mai sus, cat si
            //de la rulare la rulare; pana la urma de asta am mereu nevoie de PBE_params --plm, asa lucreaza in spate)

        System.out.println();

        //decrypt
        Cipher cipher2 = Cipher.getInstance("PBEWithHmacSHA256AndAES_128");
        AlgorithmParameters algParams = AlgorithmParameters.getInstance("PBEWithHmacSHA256AndAES_128");
        algParams.init(cipher.getParameters().getEncoded());
        cipher2.init(Cipher.DECRYPT_MODE, key, algParams);
        byte[] decryptedByteString = cipher2.doFinal(encryptedString);
        String decryptedString = new String(decryptedByteString);
        System.out.println(decryptedString);

        System.out.println("\nDone"); //asta e important to make sure i dont have infinite loops


        //TEMAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
        //There is an alternative to PBE
        //we can use PBKDF 5:19:00 practic apply PBKDF on the password, nu uitam ca la PBKDF putem sa setam output-ul
        //in functie de block size-ul algoritmului
        //putem sa integram PBKDF cu ECB sau CBC etc
        //si practic jmekeria asta e alternativa la PBE (la pbe nu ii place that dependency between enc-dec cu acei parametri)
        //in the end it is the same security level
    }
}
