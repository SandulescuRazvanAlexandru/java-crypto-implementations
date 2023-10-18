# Password-Based Encryption (PBE) Utility

This utility provides an implementation for Password-Based Encryption (PBE) using Java's `javax.crypto` package. It showcases both file-based and string-based encryption and decryption using PBE.

## Directory Structure

- **Main.java**: The main implementation file containing encryption and decryption routines.

## Features

1. **Hex Conversion**:
   - Convert byte arrays to their hexadecimal representation for easy visualization.

2. **File-Based Operations**:
   - **getHexFile**: Reads a file and prints its content in hexadecimal format.
   - **pbeEncrypt**: Encrypts a file using a password, algorithm, iterations, and salt. Outputs the encrypted content to a new file.
   - **pbeDecrypt**: Decrypts an encrypted file using the password, algorithm, iterations, salt, and PBE parameters. Outputs the decrypted content to a new file.

3. **String-Based Operations**:
   - Encrypts and decrypts strings directly.

## Usage

The main method provides a demonstrative flow of the operations:

1. File-based encryption and decryption.
2. String-based encryption and decryption.

## Concepts Clarified

- **Password-Based Encryption (PBE)**:
  PBE is a method of generating encryption keys based on a password. A derived key is generated, which can then be used for encryption and decryption.

- **Algorithm Naming**:
  E.g., `PBEWithHmacSHA256AndAES_128` indicates:
   - **PBE**: Password-Based Encryption
   - **HmacSHA256**: The HMAC with SHA-256
   - **AES_128**: AES algorithm with a 128 bits key length.

- **getEncoded Method**:
  Used to transform a key into a byte array for visualization in hexadecimal format.

## Note

Understanding encryption and its underlying concepts is crucial. This utility is for demonstrative purposes. In a real-world scenario, it's essential to follow best practices and ensure the secure storage of keys, passwords, and other sensitive data.

