package ro.ase.ism.sap.crypto;

import java.security.Provider;
import java.security.Security;

public class TestProviders {
	
	public static void checkProvider(String providerName) {
		Provider provider = Security.getProvider(providerName);
		if(provider != null) {
			System.out.println(providerName + " is available");
		}
		else{
			System.out.println(providerName + " is missing !");
		}
	}
	
	public static void loadProvider(String providerName) {
		Provider provider = Security.getProvider(providerName);
		if(provider == null) {
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		}
	}

	public static void main(String[] args) {
		
		final String standardProvider = "SUN";
		final String standardProviderForRSASignature = "SunRsaSign";
		final String standardProviderForCrypto = "SunJCE";
		final String bouncyCastleProvider = "BC";
		
		//checking at runtime if a provider is available
		checkProvider(standardProvider);
		checkProvider(standardProviderForRSASignature);
		checkProvider(standardProviderForCrypto);
		checkProvider(bouncyCastleProvider);
		
		//load BC
		loadProvider(bouncyCastleProvider);
		checkProvider(bouncyCastleProvider);
	}

}
