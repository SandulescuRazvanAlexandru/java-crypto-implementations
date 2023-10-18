import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

public class TestPBE {
    //PBE - the pass size is the user's choice
    //sends additional data in the file after the enc => return a byte[] array
    public static byte[] pbeEncrypt(String inputFileName, String password, String outputFileName, String algorithm, int noIteration, byte[] salt) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        File inputFile = new File(inputFileName);
        if (!inputFile.exists()) {
            System.out.println("File is not present there");
            throw new FileNotFoundException();
        }

        FileInputStream fis = new FileInputStream(inputFile);
        BufferedInputStream bis = new BufferedInputStream(fis);


        File outputFile = new File(outputFileName);
        if (!outputFile.exists()) {
            outputFile.createNewFile();
        }

        FileOutputStream fos = new FileOutputStream(outputFile);

        Cipher cipher = Cipher.getInstance(algorithm);
        int blockSize = cipher.getBlockSize(); // = keysize

        PBEKeySpec pbeKey = new PBEKeySpec(password.toCharArray(), salt, noIteration, blockSize);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
        Key key = keyFactory.generateSecret(pbeKey);//generates the key based on the password

        System.out.println("Initial password: "+password);
        System.out.println("Generated key(hex): " + getHex(key.getEncoded()));//get encoded - get the binary valye

        cipher.init(Cipher.ENCRYPT_MODE, key);

        //encrypt the input
        byte[] buffer = new byte[cipher.getBlockSize()];
        int noInputBytes = 0;
        byte[] outputBuffer;

        while((noInputBytes = bis.read(buffer)) > -1 ){
            outputBuffer = cipher.update(buffer, 0, noInputBytes);
            fos.write(outputBuffer);
        }

        outputBuffer = cipher.doFinal();
        fos.write(outputBuffer);

        fis.close();
        fos.close();

        //returning the params that will be used for decryption
        return cipher.getParameters().getEncoded();
    }

    public static void pbeDecrypt(String inputFileName, String password, String outputFileName, String algorithm, int noIteration, byte[] salt, byte[] pbeParams) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        File inputFile = new File(inputFileName);
        if (!inputFile.exists()) {
            System.out.println("File is not present there");
            throw new FileNotFoundException();
        }

        FileInputStream fis = new FileInputStream(inputFile);
        BufferedInputStream bis = new BufferedInputStream(fis);


        File outputFile = new File(outputFileName);
        if (!outputFile.exists()) {
            outputFile.createNewFile();
        }

        FileOutputStream fos = new FileOutputStream(outputFile);

        Cipher cipher = Cipher.getInstance(algorithm);
        int blockSize = cipher.getBlockSize(); // = keysize

        PBEKeySpec pbeKey = new PBEKeySpec(password.toCharArray(), salt, noIteration, blockSize);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
        Key key = keyFactory.generateSecret(pbeKey);//generates the key based on the password

        System.out.println("Initial password: "+password);
        System.out.println("Generated key(hex): " + getHex(key.getEncoded()));//get encoded - get the binary valye

        AlgorithmParameters algParams = AlgorithmParameters.getInstance(algorithm);
        algParams.init(pbeParams);
        cipher.init(Cipher.DECRYPT_MODE, key, algParams);

        //encrypt the input
        byte[] buffer = new byte[cipher.getBlockSize()];
        int noInputBytes = 0;
        byte[] outputBuffer;

        while((noInputBytes = bis.read(buffer)) > -1 ){
            outputBuffer = cipher.update(buffer, 0, noInputBytes);
            fos.write(outputBuffer);
        }

        outputBuffer = cipher.doFinal();
        fos.write(outputBuffer);

        fis.close();
        fos.close();

    }


    public static void pbeEncryptWithProvider(String inputFileName, String password, String outputFileName, String algorithm, int noIteration, byte[] salt, String provider) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException {
        File inputFile = new File(inputFileName);
        if (!inputFile.exists()) {
            System.out.println("File is not present there");
            throw new FileNotFoundException();
        }

        FileInputStream fis = new FileInputStream(inputFile);
        BufferedInputStream bis = new BufferedInputStream(fis);


        File outputFile = new File(outputFileName);
        if (!outputFile.exists()) {
            outputFile.createNewFile();
        }

        FileOutputStream fos = new FileOutputStream(outputFile);

        Cipher cipher = Cipher.getInstance(algorithm, provider);
        int blockSize = cipher.getBlockSize(); // = keysize

        PBEKeySpec pbeKey = new PBEKeySpec(password.toCharArray(), salt, noIteration, blockSize);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
        Key key = keyFactory.generateSecret(pbeKey);//generates the key based on the password

        System.out.println("Initial password: "+password);
        System.out.println("Generated key(hex): " + getHex(key.getEncoded()));//get encoded - get the binary valye

        cipher.init(Cipher.ENCRYPT_MODE, key);

        //encrypt the input
        byte[] buffer = new byte[cipher.getBlockSize()];
        int noInputBytes = 0;
        byte[] outputBuffer;

        while((noInputBytes = bis.read(buffer)) > -1 ){
            outputBuffer = cipher.update(buffer, 0, noInputBytes);
            fos.write(outputBuffer);
        }

        outputBuffer = cipher.doFinal();
        fos.write(outputBuffer);

        fis.close();
        fos.close();

    }

    public static void loadBCProvider() {
        Provider provider = Security.getProvider("BC");
        if(provider == null) {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
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
        public static void main(String[] args) throws NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        byte[] salt = "ism".getBytes();
        byte[] pbeParams;

       pbeParams =  TestPBE.pbeEncrypt("Message.txt", "1234", "PBEMessage.enc", "PBEWithHmacSHA256AndAES_128", 10000, salt);

        //loadBCProvider();

        //TestPBE.pbeEncrypt("Message.txt", "1234", "PBEMessage.enc", "PBEWithSHA1AndDESede", 10000, salt);

        TestPBE.pbeDecrypt("PBEMessage.enc", "1234", "MessagePBE.txt", "PBEWithHmacSHA256AndAES_128", 10000, salt, pbeParams );

        System.out.println("Done");

    }
}
