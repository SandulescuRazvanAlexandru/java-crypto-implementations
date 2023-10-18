import java.security.*;

public class TestRandomBytes {
//we use PRNG for generating keys of any size, choosing the no of bytes
    //we can create OTP - the key itself becomes a message (disadvantage)
    //seed are not safe; vulnerability

    //we used the standard provider bcs we didnt ask for any --- oracle or sun
    public static byte[] getRandomBytes(int noBytes, byte[] seed) throws NoSuchAlgorithmException {
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        if(seed!=null){
            secureRandom.setSeed(seed);
        }
        byte[] randomBytes = new byte[noBytes];
        secureRandom.nextBytes(randomBytes);

        return randomBytes;
    }

    public static void loadBCProvider(){
        Provider provider = Security.getProvider("BC");
        if(provider == null){
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        }
    }

    public static byte[] getRandomBytesWithAnyprovider(int noBytes, byte[] seed, String providerName, String algorithm) throws NoSuchAlgorithmException, NoSuchProviderException {
        SecureRandom secureRandom = SecureRandom.getInstance(algorithm, providerName);
        if(seed!=null){
            secureRandom.setSeed(seed);
        }
        byte[] randomBytes = new byte[noBytes];
        secureRandom.nextBytes(randomBytes);

        return randomBytes;
    }

    public static boolean checkProvider(String providerName){
        Provider provider = Security.getProvider(providerName);
        if(provider != null){
            return true;
        }
        else{
            return false;
        }
    }

    public static byte[] getRandomBytesWithProvider(int noBytes, byte[] seed, String providerName, String algorithm) throws NoSuchAlgorithmException, NoSuchProviderException {
        SecureRandom secureRandom = null;
        if(checkProvider(providerName)) {

            secureRandom = SecureRandom.getInstance(algorithm, providerName);
        }
        else{
            secureRandom = SecureRandom.getInstance("SHA1PRNG");
        }
        if(seed!=null){
            secureRandom.setSeed(seed);
        }
        byte[] randomBytes = new byte[noBytes];
        secureRandom.nextBytes(randomBytes);

        return randomBytes;
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

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException {
        //generate random bytes using a secure PRNG
        byte[] randomBytes = getRandomBytes(5, null);
        System.out.println("Random bytes: " + getHex(randomBytes));
        //the attacker can guess the seed this way
        byte[] randomBytesWithSeed = getRandomBytes(5, "1234".getBytes());
        System.out.println("Random bytes with seed: " + getHex(randomBytesWithSeed));

        //different from the first output
        randomBytes = getRandomBytes(5, null);
        System.out.println("Random bytes: " + getHex(randomBytes));

        //the same output as the first one <=> seed
        randomBytesWithSeed = getRandomBytes(5, "1234".getBytes());
        System.out.println("Random bytes with seed: " + getHex(randomBytesWithSeed));

        String BouncyCastleProvider = "BC";
        loadBCProvider();
        try {
            randomBytes = getRandomBytesWithAnyprovider(5, null, BouncyCastleProvider, "SHA1");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("SHA1PRNG is not available");
        } catch (NoSuchProviderException e) {
            System.out.println("Bouncy Castle Provider is not available");
            randomBytes = getRandomBytes(5, null);
            //randomBytes = getRandomBytesWithAnyprovider(5, null, "SUN");
        }
        System.out.println("Random bytes: " + getHex(randomBytes));
        try {
            randomBytes = getRandomBytesWithProvider(5, null, BouncyCastleProvider, "SHA1");
            System.out.println("Random  bytes: " + getHex(randomBytes));
        }
        catch(NoSuchAlgorithmException ex){
            System.out.println("No algorithm available");
        }
    }
}
