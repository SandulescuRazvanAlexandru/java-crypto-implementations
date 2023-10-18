# Secure Random Byte Generator

This utility provides an implementation for generating random bytes using Java's `SecureRandom` class. It allows for the generation of random bytes with or without a seed, showcasing the difference in behavior between the two methods.

## Directory Structure

- **Main.java**: The main implementation file containing random byte generation routines.

## Features

1. **Hex Conversion**:
   - Convert byte arrays to their hexadecimal representation for easy visualization.

2. **Random Byte Generation**:
   - **getRandomBytes**: Generates random bytes using the `SecureRandom` class. The function allows for both seeded and unseeded random number generation. 

## Usage

The main method provides a demonstrative flow of the operations:

1. Generates 5 random bytes without a seed.
2. Generates 5 random bytes using a fixed seed.
3. Repeats the above two steps to showcase the difference in randomness between the two methods.

## Concepts Highlighted

- **SecureRandom**:
  A class in Java that provides a cryptographically strong random number generator (RNG).

- **Seeded vs Unseeded Random Generation**:
  - **No Seed**: Utilizes computer entropy (like date, hour, etc.) to generate a truly random number each time.
  - **With Seed**: Using the same seed will always produce the same random result, hence it's deterministic in nature.

## Note

When working with cryptographic applications, the random number generation process is crucial. Seeded RNGs can be useful for testing or deterministic processes, while unseeded RNGs are typically desired for cryptographic operations to ensure the unpredictability of generated values.

