# A5 Pseudo Random Number Generator (PRNG) Implementation

This repository contains an implementation of the A5 Pseudo Random Number Generator (PRNG) based on Linear Feedback Shift Registers (LFSRs), commonly used in cryptographic applications.

## Overview

The A5 PRNG algorithm is historically significant and was utilized in mobile communications, notably in the GSM mobile telephony standard for voice privacy.

## Features

- **Initialization**: Initializes the three LFSRs based on an initial password.
- **XOR Operations**: Incorporates XORing of bits from tapped positions to produce feedback.
- **Shifting and Feedback**: Implements shifting of each register to accommodate the feedback bit.
- **Output Generation**: Produces an output sequence based on the least significant bit of each LFSR.
- **Clocking Mechanism**: Employs a majority function for clocking determination.

## Usage

1. **Initialization**: Initialize the generator with a password.
2. **Generation**: Call the `A5Generator` function to produce a sequence.
3. **Conversion**: Utilize byte sequences to produce integer values.

## Dependencies

- Java SE Development Kit (JDK)

## Installation

1. Clone the repository.
2. Navigate to the project directory.
3. Compile and run the Java program.

## Issues

- Current implementation might have deviations from the original A5/1 specification. Contributions for improvement are welcome.

## License

This project is licensed under the MIT License.

## Contributions

Pull requests and feedback are welcome!

