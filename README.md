### Java Hashing Implementation

This repository contains a Java implementation for various cryptographic hashing algorithms. The given source code, `Main.java`, provides an example of how to generate hash values for strings and files using popular cryptographic hash functions.

---

### Features

- Functions to convert byte arrays to hexadecimal strings.
- Methods to compute the hash of a given string using various algorithms.
- A function to compute the hash of a given file.
- Demonstration of Base64 encoding and decoding.
- Checking the equality of two hash values.
- Incorporating salts in hash computations.

---

### How to Use

1. **Compute Hash of a String**: 
    - Use the function `getStringHash` providing the string and the desired hashing algorithm.
    - For example, `getStringHash("This is a secret message.", "SHA-1")`.

2. **Compute Hash of a File**:
    - Use the function `getFileHash` providing the filename and the desired hashing algorithm.
    - For example, `getFileHash("Message.txt.", "SHA-1")`.

3. **Convert Byte Array to Hexadecimal String**:
    - Use the function `getHex` providing the byte array.

4. **Base64 Encoding & Decoding**:
    - The main method contains a demonstration of how to encode a hash value to Base64 and decode it back.

5. **Check Hash Equality**:
    - The main method provides various ways to check if two hash values are identical.

6. **Using Salts**:
    - The main method demonstrates how to incorporate salts while hashing.

---

### Supported Hashing Algorithms

- MD5
- SHA-1
- SHA-256

(Note: To add support for other hashing algorithms, utilize Java's MessageDigest class with the desired algorithm name.)

---

### Important Notes

- It's crucial to understand the security implications and vulnerabilities associated with each hashing algorithm. For example, MD5 is considered insecure and should not be used for cryptographic purposes.
- Always ensure proper error handling in your applications, especially when dealing with files or other external resources.
- While salts are demonstrated in this code, remember that using a constant salt defeats the purpose. For better security, generate a unique salt for each piece of data you're hashing.

---

### Contribution

Feel free to fork this repository and contribute by submitting pull requests for any enhancements, fixes, or features you'd like to add.

---

### License

This project is open-source and is available under the [MIT License](https://opensource.org/licenses/MIT).

