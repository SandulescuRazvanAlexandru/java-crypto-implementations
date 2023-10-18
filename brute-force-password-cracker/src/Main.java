import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Main {

    public static String getHex(byte[] array) {
        String output = "";
        for (byte value : array) {
            output += String.format("%02X", value);
        }
        return output;
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        long tStart = System.currentTimeMillis();

        File passwordsFile = new File("10-million-password-list-top-1000000.txt");
        FileReader fileReader = new FileReader(passwordsFile);
        BufferedReader bufferReader = new BufferedReader(fileReader);

        String line = bufferReader.readLine();
        while (line != null) {

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash1 = md.digest(("ism" + line).getBytes());

            md = MessageDigest.getInstance("SHA-1");
            byte[] hash2 = md.digest(hash1);

            if (getHex(hash2).equals("16F65C9EDDD37E4C05F6813062F919F50C275DC7")) {
                System.out.println("Parola este: " + line);
                line = null;
            } else {
                line = bufferReader.readLine();
            }
        }

        fileReader.close();

        long tFinal = System.currentTimeMillis();
        System.out.println("Duration is: " + (tFinal - tStart) + " ms");
    }
}
