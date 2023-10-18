import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class Main {

    public static String getHex(byte[] array) {
        String output = "";
        for (byte value : array) {
            output += String.format("%02x", value);
        }
        return output;
    }

    public static byte[] getPBKDFValue(
            String password,
            byte[] salt, //nu stiu unde il aplica daca e prefix sau sufix
            int noIterations,
            int outputSize, //in biti, nu bytes
            String algorithm)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        PBEKeySpec key = new PBEKeySpec(password.toCharArray(), salt, noIterations, outputSize);
        SecretKeyFactory secretFactory = SecretKeyFactory.getInstance(algorithm);
        byte[] result = secretFactory.generateSecret(key).getEncoded();

        return result;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String pass = "12345678";
        byte[] salt = "ism_".getBytes();
        byte[] pbkdfHash = getPBKDFValue(pass, salt, 20000, 160, "PBKDF2WithHmacSHA1");
        System.out.println("Hash value is " + getHex(pbkdfHash));

        //benchmarking
//        long tStart = System.currentTimeMillis();
//        long tFinal = System.currentTimeMillis();
//        System.out.println("New hash obtained in (ms): " + (tFinal - tStart));
    }
}
