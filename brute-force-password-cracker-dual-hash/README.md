# Brute Force Password Cracker for Adversary Database

This Java application is designed to brute force the adversary password by leveraging a list of the top 1 million commonly used passwords. The adversary is known to utilize a specific technique of prefixing each password with "ism" and hashing it twice using MD5 (1st run) and SHA1 (2nd run).

## Features
- Utilizes a list of the top 1 million commonly used passwords, sourced from [this repository](https://raw.githubusercontent.com/danielmiessler/SecLists/master/Passwords/Common-Credentials/10-million-password-list-top-1000000.txt).
- Efficiently reads and processes the password list from a file.
- Uses Java's cryptographic utilities to hash passwords.
- Benchmarks the solution by printing the time taken in milliseconds.

## Usage
1. Download the `10-million-password-list-top-1000000.txt` file from the provided link.
2. Place the file in the same directory as the Java application or update the file path in the code accordingly.
3. Run the Java application. If the password is found in the list, the application will print it; otherwise, it will finish without any findings.

## Implementation Details
The Java application does the following:
1. Starts a timer to measure performance.
2. Reads the password list file line by line.
3. For each password:
   - Prefixes it with "ism".
   - Hashes it using MD5.
   - Hashes the result using SHA1.
   - Compares the final hash with the adversary's hash.
4. If a match is found, the password is printed, and the program exits.
5. Finally, the program prints the time taken to execute.

## Note
This tool is for educational purposes only. Ensure you have permission to use it in any given scenario.
