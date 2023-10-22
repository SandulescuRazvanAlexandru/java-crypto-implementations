# User File Finder

This is a Java utility program that helps in finding a user file in a directory based on its SHA-256 hash value.

## Features

- Compute the SHA-256 hash for each file in a directory.
- Compare it with a predefined hash value.
- Find the exact match and print details such as base64 representation and SHA-256 hash.

## Usage

1. Place your user files in the `userFiles` directory.
2. Execute the main class.
3. The program will search for the file with the hash value `xNmqhL2yII9OE+Oi/lRuRC8m46cuYXf8gtDyYJgFNN8=` by default.
4. Once a match is found, the program will display the file name along with the base64 and SHA-256 values.

## Prerequisites

- Java SDK (Recommended version: Java 8 or later)

## Installation and Running

1. Clone this repository:
```bash
git clone [your-repo-url]
```
2. Navigate to the directory:
```bash
cd [your-directory-name]
```
3. Compile the program:
```bash
javac Main.java
```
4. Run the program:
```bash
java Main
```

## Customization

You can easily customize the hash value by modifying the `userBase64Value` in the `main` method.

## License

This project is open-sourced under the MIT license.
