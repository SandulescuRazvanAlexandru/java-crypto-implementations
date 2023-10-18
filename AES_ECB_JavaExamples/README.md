java-crypto-implementations
ECB Encryption and Decryption with Java
This repository contains various implementations related to cryptography in Java. In this specific code, we implement Electronic Codebook (ECB) mode for symmetric encryption and decryption using both AES and DES as encryption algorithms.

Implementation Details
Utility Functions

getHex(byte[] array): Convert a byte array to its hexadecimal string representation.
getHexFile(String inputFileName): Reads a file byte-by-byte and prints its hexadecimal representation.
Encryption

encryptECB(String inputFileName, String password, String encryptedFileName, String algorithm): This function reads an input file, encrypts it using the provided algorithm (AES or DES) in ECB mode and writes the encrypted content to the output file.
Decryption

decryptECB(String encryptedFileName, String password, String decryptedFileName, String algorithm): Reads an encrypted file, decrypts it using the given algorithm in ECB mode, and writes the decrypted content to an output file.
Main Execution

The main function demonstrates the encryption of a "Message.txt" file using AES in ECB mode and then its decryption.
Requirements
The password length should match the block size of the chosen algorithm:
DES: 8 characters (64 bits)
AES: 16 characters (128 bits)
Notes
Using ECB mode is not recommended for most real-world applications due to its susceptibility to pattern recognition. However, it serves educational purposes in this context.
The utility functions are handy for debugging and understanding the underlying hexadecimal representations.
Always ensure that the input file exists before attempting to read from it. The code handles the case where the file doesn't exist and prints a warning.
Sample Execution
To encrypt and decrypt a file using this code:

Create a file named Message.txt with the content you wish to encrypt.
Run the Main class. This will produce an encrypted file MessageEncrypted.enc and then a decrypted file MessageDecrypted.txt.
Verify the contents of MessageDecrypted.txt to ensure the encryption and decryption processes were successful.
