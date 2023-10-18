## One-Time Pad (OTP) Encryption in Java

This repository presents an implementation of the One-Time Pad (OTP) encryption method in Java. The `Main` class provides utilities for encryption and decryption using randomly generated keys.

### Methods:

- **`getRandomBytes(int noBytes, byte[] seed)`**: Produces a specified number of random bytes. It can also be seeded if deterministic randomness is required.

- **`OTPEncrypt(String inputFileName, String outputFileName)`**: Encrypts a given file using OTP. This method generates a random key, equivalent in length to the file, and returns this key after encryption. The method employs bitwise XOR for encryption.

- **`OTPDecrypt(String inputFileName, String outputFileName, byte[] randomKey)`**: Decrypts a file that was previously encrypted using the OTP method. Since OTP employs symmetric encryption, the same key is utilized for decryption.

### Usage:

1. The main function offers a demonstration on how to use the methods.
  
2. For encrypting a file:
```java
byte[] otpKey = OTPEncrypt("PathToYourFile.txt", "EncryptedFile.otp");
```
  
3. For decrypting the encrypted file:
```java
OTPDecrypt("EncryptedFile.otp", "DecryptedFile.txt", otpKey);
```

### Notes:

- The One-Time Pad encryption method is theoretically unbreakable if the key is truly random, as long as the key is kept secret and used only once.

- The generated random key needs to be securely stored and shared with the intended recipient. Loss of the key means the data is irretrievable.

- This implementation reads files byte-by-byte, which might not be the most efficient method for larger files. Enhancements can be made to handle larger buffer sizes.

- Always ensure the generated key is kept secret and never reused. A One-Time Pad is named so because each pad (or key) should be used one time only.

### Dependencies:

Java's built-in packages are the only requirements for this code (`java.io` and `java.security`). No additional libraries are needed.

### Contribution:

Contributions to improve performance or to add additional features are always welcome. Feel free to submit a pull request or open an issue to discuss potential changes or problems.
