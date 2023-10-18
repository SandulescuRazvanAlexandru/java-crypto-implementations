# Keystore Management, RSA Asymmetric Encryption & Digital Signature Utility

This utility provides functionality for:
- Loading a KeyStore and listing its contents.
- Retrieving public and private keys from a given KeyStore.
- Generating session keys.
- Encrypting and decrypting data using RSA.
- Generating and verifying digital signatures for files.

## Features

### Hexadecimal Converter
Convert byte arrays to their hexadecimal representation.

### KeyStore Loader
Load a KeyStore of specified type with given password.

### KeyStore Content Lister
List all the aliases in a KeyStore along with their type.

### Certificate Key Retriever
Extract public key from a X509 certificate.

### Session Key Generator
Generate session keys for specified algorithms and key sizes.

### RSA Encryption & Decryption
Encrypt and decrypt data using RSA and provided keys.

### Digital Signature Generator & Verifier
Sign files and verify their signatures.

## How to Use

1. Ensure you have all necessary libraries imported.
2. Instantiate the utility.
3. Use the provided methods for respective tasks.

## Methods

- `getHex(byte[] array)`: Returns hexadecimal representation of a byte array.
- `loadKeyStore(...)`: Loads a KeyStore with the given parameters.
- `listKeyStoreContent(KeyStore keyStore)`: Lists content of a KeyStore.
- `getCertificateKey(...)`: Gets public key from a certificate.
- `getPublicKey(...)`, `getPrivateKey(...)`: Retrieve public and private keys from a KeyStore.
- `generateSessionKey(...)`: Generate a session key.
- `encryptRSA(...)`, `decryptRSA(...)`: Encrypt and decrypt data using RSA.
- `getDigitalSignature(...)`, `hasValidSignature(...)`: Generate and verify digital signatures.

## Example

In the `main` method, a sample flow is provided which demonstrates:
1. Loading a KeyStore and listing its contents.
2. Loading public key from a certificate and comparing it with the one in the KeyStore.
3. RSA encryption and decryption of a session key.
4. Digital signature generation and verification of a file named "Message.txt".

## Notes

- Make sure to handle exceptions properly in your implementation. This utility has a few `System.out.println` calls for errors instead of throwing them.
- For the given code, the Keystore file, certificate, and message file paths are hardcoded. Make sure they exist or replace them with your file paths.
- The utility has been written with a sense of humor, as indicated by messages such as "Vader changed the file".
