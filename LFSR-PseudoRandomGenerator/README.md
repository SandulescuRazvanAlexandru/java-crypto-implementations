# Linear Feedback Shift Register (LFSR) Pseudo-Random Generator

This Java utility demonstrates the principles of a Linear Feedback Shift Register (LFSR) to generate pseudo-random bytes based on an initial seed, which in this case is a password.

## Directory Structure

- **TestLFSR.java**: The main implementation file that encompasses the LFSR mechanism and demonstrates its use.

## Features

1. **Hex Conversion**:
   - `getHex()`: Converts a byte array to its hexadecimal representation.

2. **Shift and Insert Register**:
   - `shiftAndInsertRegister()`: Conducts a shift operation on the byte array register and inserts a next bit value, returning the random bit.

3. **Pseudo Random Value Generation**:
   - `getPseudoRandomValues()`: Generates pseudo-random values based on the provided password.

## Usage

The `main` method initializes with a password and calls the `getPseudoRandomValues` method to generate pseudo-random bytes, which are then printed as a hexadecimal string.

## Concepts Highlighted

- **LFSR**:
  LFSRs are shift registers wherein the input bit is a linear function of the previous state. They're commonly used in pseudo-random number generators, error detection, and digital wireless communication systems.

- **Taps**:
  In the LFSR context, "taps" are specific bits in the register that determine the input bit for the next state.

- **Pseudo-Random Number Generation**:
  The generator creates a sequence of bits that appear random but is determined entirely by an initial value, or seed. In this utility, a password is used as the seed.

## Note

The use of passwords as seeds for random number generation can introduce vulnerabilities if the set of possible passwords is small. It's always recommended to use cryptographically secure methods for critical applications.

