# File Integrity Verifier

This project provides a Java solution that leverages the Java Cryptographic Architecture classes to verify the integrity of files on a given folder or drive. 

## Description

The primary goal of this solution is to compute the HashMAC (Message Authentication Code) of all the files within a given directory, and subsequently compare these values to detect any file changes. It provides the ability to:
- Compute and store the HashMAC of all the files.
- Check the integrity of monitored files to see if any file has been changed since the last status update.

## Features

- **HashMAC Computation**: This solution allows you to select the algorithm to compute the HashMAC.
- **Operation Mode**: The user can select the operation mode either through command-line arguments or by console input.
- **Secure Secret Key**: The secret key is either passed through command-line arguments or asked to the user at the console.
- **Logging**: The HashMAC values of each file can be stored in a chosen text or binary file format.
- **Reporting**: After each integrity check, a report is generated indicating whether each file's integrity is "OK" or "CORRUPTED".

## Setup & Usage

1. Ensure you have Java installed and set up on your machine.
2. Clone this repository to your local machine.
3. Navigate to the directory containing the source code.
4. Compile and run the Java program.
5. Follow on-screen instructions to either compute & store the HashMAC values or to perform an integrity check.

## Algorithms

The current default algorithm used for HashMAC computation is `HmacSHA1`, but you can easily modify and choose your preferred algorithm.

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change. Ensure to update tests as appropriate.

## License

[MIT](https://choosealicense.com/licenses/mit/)
