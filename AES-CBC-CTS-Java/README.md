## AES Encryption with CBC-CTS Mode in Java

This repository contains an implementation of AES encryption and decryption using the CBC-CTS (Cipher Block Chaining - CipherText Stealing) mode in Java. The `Main` class demonstrates encryption and decryption of a file using the specified mode.

### Methods:

- **`getRandomBytes(int noBytes, byte[] seed)`**: Generates a specified number of random bytes, which can be seeded for repeatability.

- **`encryptCBC_CTS(String inputFileName, String password, String encryptedFileName, String algorithm)`**: Encrypts a given file using the AES algorithm in CBC-CTS mode. The initialization vector (IV) is randomly generated and prepended to the encrypted file.

- **`decryptCBC_CTS(String encryptedFileName, String password, String decryptedFileName, String algorithm)`**: Decrypts a file that was encrypted using the above method. It retrieves the IV from the beginning of the file.

### How to Use:

1. The main function demonstrates the usage of the methods.
  
2. For encrypting a file:
```java
encryptCBC_CTS("PathToYourFile.txt", "YourPassword", "EncryptedFile.enc", "AES");
```
  
3. For decrypting the encrypted file:
```java
decryptCBC_CTS("EncryptedFile.enc", "YourPassword", "DecryptedFile.txt", "AES");
```

### Notes:

- CBC-CTS mode is essentially a variant of the CBC mode that allows encryption without using the standard paddings. It ensures that the encrypted output has the same size as the input, making it challenging to deduce the block size.

- The code also demonstrates how to handle IVs by generating a random IV for encryption and reading it during decryption.

- Password lengths must match the expected length for the AES key (e.g., 16 bytes for AES-128, 24 bytes for AES-192, 32 bytes for AES-256).

- Always use a secure and unique password for encryption. 

- Ensure the safety and secrecy of the IV, especially if not stored with the ciphertext.

### Dependencies:

The code uses Java's built-in `javax.crypto` package, so no additional libraries are required.

### Contribution:

Feel free to submit a pull request if you wish to make any improvements or report any issues. We appreciate all contributions!
