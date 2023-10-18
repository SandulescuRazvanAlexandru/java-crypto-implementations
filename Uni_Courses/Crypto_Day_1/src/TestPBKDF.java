import org.bouncycastle.jcajce.provider.symmetric.OpenSSLPBKDF;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;

public class TestPBKDF {

    public static byte[] getPBKDFValue(String password, byte[] salt, int noIterations, int outputSize, String algorithm) throws NoSuchAlgorithmException, InvalidKeySpecException {

        byte[] result = null;

        PBEKeySpec key = new PBEKeySpec(password.toCharArray(), salt, noIterations,outputSize);
        SecretKeyFactory secretFactory = SecretKeyFactory.getInstance(algorithm);
        result = secretFactory.generateSecret(key).getEncoded();
        return result;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
        //benchmarking the speed of a SHA1 hash
        String pass = "12345678";
        long tStart = System.currentTimeMillis();
       // byte[] hash = TestMessageDigest.getStringHash(pass, "SHA-1", "BC");
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] hash =  md.digest(pass.getBytes());
        long tFinal = System.currentTimeMillis();

        System.out.println("Hash obtained in (ms): " + (tFinal-tStart));
        System.out.println("Hash value is " + TestMessageDigest.getHex(hash));

        //benchmarking the PBKDF solution
        byte[] salt = "ism_".getBytes();
         tStart = System.currentTimeMillis();
        byte[] pbkdfHash = getPBKDFValue(pass, salt, 20000, 160, "PBKDF2WithHMacSha1");
         tFinal = System.currentTimeMillis();

        System.out.println("New hash obtained in (ms): " + (tFinal-tStart));
        System.out.println("Hash value is " + TestMessageDigest.getHex(pbkdfHash));

    }
}
