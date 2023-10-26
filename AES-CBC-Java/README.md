## java-crypto-implementations

### CBC Encryption with Java

This repository contains implementations of the Cipher Block Chaining (CBC) mode of operation for block ciphers, using Java's cryptographic libraries. 

#### Utility Methods

- **`getHex(byte[] array)`**: Convert a byte array into its hexadecimal representation.
- **`getHexFile(String inputFileName)`**: Print the hexadecimal representation of the file's contents.
- **`getRandomBytes(int noBytes, byte[] seed)`**: Generate a series of random bytes, optionally seeded.

#### Encryption and Decryption Functions

Three different implementations for CBC are provided:

1. **Random IV at the Beginning of the File**
   - `encryptCBCbegining(String inputFileName, String password, String encryptedFileName, String algorithm)`
   - `decryptCBCbegining(String encryptedFileName, String password, String decryptedFileName, String algorithm)`

2. **Random IV at the End of the File**
   - `encryptCBCEnd(String inputFileName, String password, String encryptedFileName, String algorithm)`
   - `decryptCBCEnd(String encryptedFileName, String password, String decryptedFileName, String algorithm)`

3. **Known IV**
   - `encryptCBCivKnown(String inputFileName, String password, String encryptedFileName, String algorithm)`
   - `decryptCBCivKnown(String encryptedFileName, String password, String decryptedFileName, String algorithm)`

#### How to Use

In the `main` method, uncomment the desired encryption-decryption pair and provide the necessary filenames and passwords.

#### Notes

- Ensure the input files exist and are accessible. In case they're missing, the methods will print `"***** File is not there"`.
- Remember, the security of your encrypted data doesn't only rely on the strength of the encryption method, but also on the strength and secrecy of the key (password). Always handle keys with care and avoid hardcoding them in source code.

### Dependencies

- The Java Cryptography Extension (JCE) and Java Cryptography Architecture (JCA) are used in this implementation. Ensure you have them installed and properly set up in your Java environment.

### Disclaimer

This code is for educational purposes. In a production environment, always consult with cybersecurity experts when dealing with encryption and sensitive data.
