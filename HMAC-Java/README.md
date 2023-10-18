## HMAC Implementation in Java

This repository contains a simple implementation of HMAC (Hash-Based Message Authentication Code) using Java. The `Main` class provides methods to compute HMAC values for both files and strings using the specified HMAC algorithm.

### Methods:

- **`getHex(byte[] array)`**: Converts a byte array to a hexadecimal string.
  
- **`getHashMACFile(String inputFileName, byte[] secretKey, String algorithm)`**: Computes the HMAC of a given file using the provided secret key and HMAC algorithm.
  
- **`getHashMACString(String message, byte[] secretKey, String algorithm)`**: Computes the HMAC of a given string message using the provided secret key and HMAC algorithm.

### How to Use:

1. The main function demonstrates how to use the above methods.
  
2. For HMAC computation of a file, use:
```java
byte[] hmac = getHashMACFile("PathToYourFile.txt", "YourSecretKey".getBytes(), "HmacSHA1");
System.out.println("HashMAC for File = " + getHex(hmac));
```
  
3. For HMAC computation of a string, use:
```java
String message = "YourMessageHere";
byte[] hmacString = getHashMACString(message, "YourSecretKey".getBytes(), "HmacSHA1");
System.out.println("HashMAC for String = " + getHex(hmacString));
```

### Notes:

- Always ensure your secret key is kept secure.
  
- HMACs are commonly associated with files to provide both authenticity and integrity.
  
- While the sample code demonstrates HMAC with `HmacSHA1`, it's recommended to use more secure algorithms like `HmacSHA256` in production environments.

- The sample also includes a basic SHA-1 hashing example for comparison purposes. However, note that SHA-1 is considered weak due to vulnerabilities and should not be used for cryptographic security in new systems.

### Dependencies:

The code uses Java's built-in `javax.crypto` package, so no additional libraries are required.

### Contribution:

Feel free to submit a pull request if you wish to make any improvements or report any issues. We appreciate all contributions!
