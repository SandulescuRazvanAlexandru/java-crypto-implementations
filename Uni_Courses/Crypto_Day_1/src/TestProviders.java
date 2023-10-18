import java.security.Provider;
import java.security.Security;

public class TestProviders {
//the providers contain the actual implementation of the crypto algorithms
// = libraries which implement the algs that we need

   public static void checkProvider(String providerName){
       Provider provider = Security.getProvider(providerName);
       if(provider != null){
           System.out.println(providerName + " is available");
       }
       else{
           System.out.println(providerName+ " is missing");
       }
   }

   public static void loadProvider(String providerName){
       Provider provider = Security.getProvider(providerName);
       if(provider == null){
           Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
       }
   }

    public static void main(String[] args){
        //standard providers for java
        final String standardProvider = "SUN";
        final String standardProviderForRsa = "SunRsaSign";
        final String standardProviderForCrypto = "SunJCE";
       //bouncy castle provider
        final String bouncyCastleProvider = "BC";

        //checking at runtime if a provider is available
        checkProvider(standardProvider);
        checkProvider(standardProviderForRsa);
        checkProvider(standardProviderForCrypto);
        checkProvider(bouncyCastleProvider);

        //load BC
        loadProvider(bouncyCastleProvider);
        checkProvider(bouncyCastleProvider);
    }
}
