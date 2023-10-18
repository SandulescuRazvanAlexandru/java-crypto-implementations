import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Main {

    public static String getHex(byte[] array) {
        String output = "";
        for (byte value : array) {
            output += String.format("%02x", value);
        }
        return output;
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

    public static void main(String[] args) throws NoSuchAlgorithmException {
        // generate random bytes using a secure PRNG
        byte[] randomBytes = getRandomBytes(5, null);
        System.out.println("Random bytes:" + getHex(randomBytes));

        byte[] randomBytesWithSeed = getRandomBytes(5, "1234".getBytes());
        System.out.println("Random bytes with seed:" + getHex(randomBytesWithSeed));

        randomBytes = getRandomBytes(5, null);
        System.out.println("Random bytes:" + getHex(randomBytes));

        randomBytesWithSeed = getRandomBytes(5, "1234".getBytes());
        System.out.println("Random bytes with seed:" + getHex(randomBytesWithSeed));

        //no seed -> uses computer entropy (like date, hour, etc)
        //with seed -> same seed => always the same result
    }
}
