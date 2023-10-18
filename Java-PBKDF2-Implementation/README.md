## Password-Based Key Derivation Function (PBKDF2) Implementation in Java

This repository provides a simple implementation of the PBKDF2 (Password-Based Key Derivation Function 2) in Java. The `Main` class contains utility methods to derive a cryptographic key from a password using a salt and iterate over it a specific number of times. The resultant hash can be used as a key or simply to store a password securely.

### Methods:

- **`getHex(byte[] array)`**: Transforms a byte array into its hexadecimal representation. Useful for visualizing the output of the key derivation.

- **`getPBKDFValue(String password, byte[] salt, int noIterations, int outputSize, String algorithm)`**: Derives a key from the provided password using PBKDF2. The function requires a salt, number of iterations, desired output size (in bits), and the specific PBKDF2 algorithm variant.

### Usage:

1. The main function showcases the utilization of the methods.
  
2. To derive a cryptographic key from a password:
```java
String password = "your_password_here";
byte[] salt = "your_salt_here".getBytes();
int iterations = desired_number_of_iterations;
int outputSizeInBits = desired_output_size_in_bits;
byte[] derivedKey = getPBKDFValue(password, salt, iterations, outputSizeInBits, "PBKDF2WithHmacSHA1");
```

3. To get the hexadecimal representation of the derived key:
```java
String hexRepresentation = getHex(derivedKey);
```

### Notes:

- Using a salt in PBKDF2 is essential to defend against precomputed attacks and should ideally be unique per user or per use.

- The number of iterations should be chosen based on a balance between security and performance. A higher number of iterations means it's more computationally expensive to derive the key, which can deter brute-force attacks. 

- The provided benchmarking code (commented out) can be helpful in assessing the time it takes to compute the hash. Adjusting the number of iterations and observing the effect on time can provide insights on setting a secure and practical iteration count.

### Dependencies:

The Java Cryptography Extension (JCE) framework is used, which comes bundled with standard Java distributions. No additional libraries are required.

### Contribution:

Contributions to enhance performance, add extra features, or update cryptographic practices are always welcome. Open an issue or submit a pull request to discuss potential modifications or to report any issues.
