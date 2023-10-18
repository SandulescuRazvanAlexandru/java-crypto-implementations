# java-crypto-implementations

## ECB Encryption & Decryption Implementation in Java

This Java program demonstrates the encryption and decryption of files using the Electronic Codebook (ECB) mode of operation for block ciphers.

### Key Methods:

- **`getHex(byte[] array)`**: Convert a byte array to a hexadecimal string.
- **`getHexFile(String inputFileName)`**: Print the hexadecimal representation of a file.
- **`encryptECB(String inputFileName, String password, String encryptedFileName, String algorithm)`**: Encrypts a file in ECB mode using the specified algorithm and password.
- **`decryptECB(String encryptedFileName, String password, String decryptedFileName, String algorithm)`**: Decrypts a file encrypted in ECB mode using the specified algorithm and password.

### Usage:

The program reads from an input file called `Message.txt`, encrypts it using the AES algorithm (or DES, if chosen) in ECB mode, and then writes the encrypted output to `MessageEncrypted.enc`. Afterwards, it decrypts the encrypted file and writes the decrypted output to `MessageDecrypted.txt`.

### Password Considerations:

Ensure that the password used matches the block size of the chosen encryption algorithm:
- **DES**: 8 characters (64 bits)
- **AES**: 16 characters (128 bits). For AES, password lengths of 16, 24, or 32 characters can be used based on AES-128, AES-192, or AES-256 respectively.

### Implementation:

The program uses Java's `javax.crypto` library to handle encryption and decryption tasks. It works with files, reading them in chunks based on the block size of the selected encryption algorithm.

### Important Considerations:

1. ECB mode can reveal patterns in encrypted data, making it less secure for many types of data. This program is intended for educational purposes and may not be suitable for production-level encryption needs.
2. Always ensure the correct padding is used. While PKCS5Padding is mentioned for DES, it's generally compatible with both DES and AES in Java's crypto library.
3. The `encrypt` and `decrypt` methods provide basic error handling, printing a message if the input file does not exist. This can be expanded upon based on the application's requirements.
