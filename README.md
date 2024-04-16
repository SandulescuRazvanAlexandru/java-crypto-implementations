# java-crypto-implementations

# Cryptography Projects Repository

This repository is a comprehensive collection of various cryptographic implementations and utilities developed in Java. Each directory contains a specific project or utility focused on a cryptographic concept or algorithm.

## Projects Overview

1. **A5-LFSR-PRNG-Java**: An implementation of the A5 LFSR Pseudo Random Number Generator.
2. **brute-force-password-cracker-dual-hash**: A tool for brute-forcing password hashes using dual hash functions.
3. **FileIntegrity-HashMACVerifier**: A utility to verify the integrity of files using HMAC (Hash-Based Message Authentication Code).
4. **AES Implementations**: Multiple variations and demonstrations of the Advanced Encryption Standard.
5. **RSA**: Implementations and utilities related to the RSA encryption algorithm.
6. **PBE**: Password-based encryption utilities and demonstrations.
7. **PBKDF2**: A demonstration of the Password-Based Key Derivation Function 2.
8. **OTP**: One-time password generator and verifier.
9. **SecureRandom**: Utility for generating cryptographically secure random numbers.
10. **crypto-hashing**: A Java implementation showcasing various cryptographic hashing algorithms.

# Cryptography Concepts Overview

This document provides an overview of several key cryptographic and data handling concepts discussed in a conversation about securing and managing data effectively in software applications.

## Table of Contents
- [AES (Advanced Encryption Standard)](#aes-advanced-encryption-standard)
- [RSA (Rivest–Shamir–Adleman)](#rsa-rivestshamiradleman)
- [PBE (Password-Based Encryption)](#pbe-password-based-encryption)
- [PBKDF2 (Password-Based Key Derivation Function 2)](#pbkdf2-password-based-key-derivation-function-2)
- [OTP (One-Time Password)](#otp-one-time-password)
- [SecureRandom](#securerandom)
- [ECDSA (Elliptic Curve Digital Signature Algorithm)](#ecdsa-elliptic-curve-digital-signature-algorithm)
- [HMAC (Hash-Based Message Authentication Code)](#hmac-hash-based-message-authentication-code)
- [Encoding vs Encryption vs Hashing vs Obfuscation](#encoding-vs-encryption-vs-hashing-vs-obfuscation)

## AES (Advanced Encryption Standard)
AES is a symmetric encryption algorithm used widely for secure data encryption. It is known for its efficiency and strong security credentials, supporting key sizes of 128, 192, or 256 bits.

### Modes of AES
- **AES-ECB (Electronic Codebook Mode)**: Simple but vulnerable to pattern analysis.
- **AES-CBC (Cipher Block Chaining Mode)**: Encrypts blocks of plaintext with XOR operations, using a previous ciphertext block.

## RSA (Rivest–Shamir–Adleman)
RSA is an asymmetric cryptographic algorithm used for secure data transmission, utilizing a pair of keys (public and private) to encrypt and decrypt data.

## PBE (Password-Based Encryption)
This method uses passwords to derive cryptographic keys, combining the password with a salt and a repetition count to generate a key for data encryption.

## PBKDF2 (Password-Based Key Derivation Function 2)
A method that applies a pseudorandom function to derive a key from a password, using a salt and multiple iterations to enhance security against brute-force attacks.

## OTP (One-Time Password)
OTP is a password that is valid for only one login session or transaction, commonly used in two-factor authentication systems.

## SecureRandom
`SecureRandom` is a class used in programming to generate cryptographically strong random numbers or keys.

## ECDSA (Elliptic Curve Digital Signature Algorithm)
A variant of the Digital Signature Algorithm which uses elliptic curve cryptography to create digital signatures.

## HMAC (Hash-Based Message Authentication Code)
A construction for calculating a message authentication code using a cryptographic hash function and a secret key, providing data integrity and authenticity.

## Encoding vs Encryption vs Hashing vs Obfuscation
- **Encoding**: Converts data into a different format for interoperability (not secure).
- **Encryption**: Secures data by making it unreadable without the correct key (secure).
- **Hashing**: Transforms data into a fixed-size string to check data integrity (irreversible).
- **Obfuscation**: Makes software difficult to understand to protect against reverse engineering (partially reversible).

This README is designed to be a quick reference guide for understanding these important concepts and applying them in software development for data security and management.


## Usage

Each directory contains its own README.md file detailing the specific usage, requirements, and additional details of the project. Navigate to the desired directory and refer to its README for more information.

## Contribution

Contributions are welcome! If you'd like to add a new project, improve an existing one, or report an issue, please feel free to submit a pull request or open an issue.

## License

All projects within this repository are open-sourced under the MIT license. Please refer to the `LICENSE` file in the root directory or individual project directories for more details.
